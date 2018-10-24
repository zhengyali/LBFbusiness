package cn.lili.core.service.product;

import cn.lili.core.bean.BuyerCart;
import cn.lili.core.bean.BuyerItem;
import cn.lili.core.bean.product.Color;
import cn.lili.core.bean.product.Sku;
import cn.lili.core.bean.product.SkuQuery;
import cn.lili.core.dao.product.ColorDao;
import cn.lili.core.dao.product.ProductDao;
import cn.lili.core.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ABEL on 2018/5/31.
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;
    /**通过商品 id 查询库存结果集 */
    public List<Sku> selectSkuListByProductId(Long id){
        SkuQuery skuQuery=new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(id);
        List<Sku> skus = skuDao.selectByExample(skuQuery);//结果集为15条
        //向数据库只发送三条sql   原因是一级缓存
        for (Sku sku:skus){
            Long colorId = sku.getColorId();
            Color color= colorDao.selectByPrimaryKey(colorId);
            sku.setColor(color);
        }
       return skus;
    }

    /**修改*/
    public void updateSkuById(Sku sku){
        skuDao.updateByPrimaryKeySelective(sku);
    }
    @Autowired
    private ProductDao productDao;
    /** 购物车：根据skuid获取Sku对象 （商品对象，颜色对象） */
    public Sku selectSkuById(Long id){
        //Sku 对象
        //商品对象
        //颜色对象
        Sku sku = skuDao.selectByPrimaryKey(id);
        sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        return sku;
    }

    @Autowired
    private Jedis jedis;
    /**  购物车之登陆之保存购物车到redis中  此处使用到dubbo 就必须实现序列化接口 */
    public void insertBuyerCartToRedis(BuyerCart buyerCart,String username){
        //判断购物项的长度大于0
        List<BuyerItem> items = buyerCart.getItems();
        if (items.size()>0){
            for(BuyerItem buyerItem : items){
                /** 判断redis中是否已经存在 */
                if(jedis.hexists("buyerCart:"+username,
                        String.valueOf(buyerItem.getSku().getId()))){
                   //加数量
                    jedis.hincrBy("buyerCart:"+username,
                            String.valueOf(buyerItem.getSku().getId()),
                            buyerItem.getAmount());
                }else {
                jedis.hset("buyerCart:"+username,
                        String.valueOf(buyerItem.getSku().getId()),
                        String.valueOf(buyerItem.getAmount()));
                }
            }

        }
    }

    /**从redis中取出所有购物车 */
    public BuyerCart selectBuyerCartFromRedis(String username){
        BuyerCart buyerCart = new BuyerCart();
        Map<String, String> map = jedis.hgetAll("buyerCart:" + username);
        if (null != map){
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String ,String> entry : entrySet){
                /**
                 * K : Skuid   V : amount
                 * */
                Sku sku = new Sku();
                sku.setId(Long.valueOf(entry.getKey()));
                BuyerItem buyerItem = new BuyerItem();
                buyerItem.setSku(sku);
                buyerItem.setAmount(Integer.parseInt(entry.getValue()));
                //追加商品到购物车
                buyerCart.addItem(buyerItem);
            }
        }
        return buyerCart;
    }

}
