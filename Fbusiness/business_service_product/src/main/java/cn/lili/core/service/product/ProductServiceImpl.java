package cn.lili.core.service.product;

import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.*;
import cn.lili.core.dao.product.ColorDao;
import cn.lili.core.dao.product.ProductDao;

import cn.lili.core.dao.product.SkuDao;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;



/**
 * Created by ABEL on 2018/5/28.
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductDao productDao;

    //分页对象
    public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow) {
        ProductQuery productQuery = new ProductQuery();
        productQuery.setPageNo(Pagination.cpn(pageNo));
        //排序
        productQuery.setOrderByClause("id desc");
        ProductQuery.Criteria criteria = productQuery.createCriteria();
        StringBuilder params = new StringBuilder();
        if (null != name) {
            params.append("name=").append(name);
            criteria.andNameLike("%" + name + "%");
        }
        if (null != brandId) {
            criteria.andBrandIdEqualTo(brandId);
            params.append("&brandId=").append(brandId);
        }
        if (null != isShow) {
            params.append("&isShow=").append(isShow);
            criteria.andIsShowEqualTo(isShow);
        } else {
            params.append("&isShow=").append(false);
            criteria.andIsShowEqualTo(false);
        }
        Pagination pagination = new Pagination(
                productQuery.getPageNo(), productQuery.getPageSize(),
                productDao.countByExample(productQuery),
                productDao.selectByExample(productQuery)
        );
        String url = "/product/list.do";
        pagination.pageView(url, params.toString());
        return pagination;
    }

    //加载颜色
    @Autowired
    private ColorDao colorDao;

    //颜色结果集
    public List<Color> selectColorList() {
        ColorQuery query = new ColorQuery();
        query.createCriteria().andParentIdNotEqualTo(0L);
        return colorDao.selectByExample(query);
    }

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private Jedis jedis;

    //保存商品，返回id
    public void insertProduct(Product product) {
        //保存商品
        //商品编号 全国唯一
        Long id = jedis.incr("pno");
        product.setId(id);
        /** 下架状态  和 删除 */
        product.setIsShow(false);
        product.setIsDel(true);
        productDao.insertSelective(product);
        //返回商品id
        //保存sku
        String[] colors = product.getColors().split(",");
        String[] sizes = product.getSizes().split(",");
        for (String color : colors) {
            for (String size : sizes) {
                Sku sku = new Sku();
                //商品id
                sku.setProductId(product.getId());
                sku.setColorId(Long.parseLong(color));
                sku.setSize(size);
                sku.setMarketPrice(999f);
                sku.setPrice(666f);
                //运费
                sku.setDeliveFee(8f);
                //库存
                sku.setStock(0);
                //购买限制
                sku.setUpperLimit(200);
                //时间
                sku.setCreateTime(new Date());
                skuDao.insertSelective(sku);
            }
        }
    }

    /**
     * 上架
     */
    @Autowired
    private JmsTemplate jmsTemplate;
    public void isShow(Long[] ids) {
        Product product = new Product();
        product.setIsShow(true);
        for (final Long id : ids) {
            //商品状态的更改
            product.setId(id);
            productDao.updateByPrimaryKeySelective(product);

            /** 发送消息到ActiveMQ */
           // jmsTemplate.send("brandId",messageCreator) //往其他管道发送，不写找配置中的默认
            jmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }
            });
        }
    }
}
