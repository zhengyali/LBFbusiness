package cn.lili.core.service;

import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.Product;
import cn.lili.core.bean.product.ProductQuery;
import cn.lili.core.bean.product.Sku;
import cn.lili.core.bean.product.SkuQuery;
import cn.lili.core.dao.product.ProductDao;
import cn.lili.core.dao.product.SkuDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全文检索---solr
 * Created by ABEL on 2018/6/2.
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService{

    @Autowired
    private SolrServer solrServer;
    /** 全文检索 */
    public Pagination selectPaginationByQuery(Integer pageNo,Long brandId,String price,String keyword) throws SolrServerException {
        //创建包装类
        ProductQuery productQuery = new ProductQuery();
        //设置当前页
        productQuery.setPageNo(Pagination.cpn(pageNo));
        //每页显示多少条
        productQuery.setPageSize(15);

      /** 分页：页面展示的条件*/
        StringBuilder params = new StringBuilder();

      /** 结果集 */
        List<Product> products = new ArrayList<Product>();
        SolrQuery solrQuery= new SolrQuery();
        /** 关键词 */
        solrQuery.set("q","name_ik:"+keyword);
        params.append("keyword=").append(keyword);
        /**过滤条件*/
        //品牌
        if(null != brandId){
            solrQuery.addFilterQuery("brandId:" + brandId);
        }
        //价格   0-99 1600
        if(null != price){
            String[] p = price.split("-");
            if(p.length == 2){
                solrQuery.addFilterQuery("price:[" + p[0] + " TO " + p[1] + "]");
            }else{
                solrQuery.addFilterQuery("price:[" + p[0] + " TO *]");
            }
        }

        /**高亮*/
        /*开关*/
        solrQuery.setHighlight(true);
        // 高亮字段
        solrQuery.addHighlightField("name_ik");
        // 样式  <span style='color:red'>  2016 </span>
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");

        /**排序*/
        solrQuery.addSort("price", SolrQuery.ORDER.asc);
       /** 设置solr分页  solr默认查十条  limit开始行   每页显示多少行*/
        solrQuery.setStart(productQuery.getStartRow());
        solrQuery.setRows(productQuery.getPageSize());
        /** 执行查询  */
        QueryResponse response = solrServer.query(solrQuery);
        /** 取高亮   */
        /** Map K : V    442 : Map  外层Map   442处为对象id
         *  Map K : V    name_ik : List<String>   内层Map  String--存的是标题   list.get(0)
         * */
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        //结果集
        SolrDocumentList docs = response.getResults();
        //发现条数 (总条数)构建分页时用到
        long numFound = docs.getNumFound();
        for (SolrDocument doc:docs){
            //创建商品对象
            Product product=new Product();
            //商品ID
            String id = (String) doc.get("id");
            product.setId(Long.valueOf(id));
            //商品名称  ik
          /*  String name= (String) doc.get("name_ik");   这样的方式取出的不是高亮
            product.setName(name);*/
            Map<String, List<String>> map = highlighting.get(id);
            List<String> list = map.get("name_ik");
            product.setName(list.get(0));
            //图片
            String url = (String) doc.get("url");
            product.setImgUrl(url);
            //价格 售价   select price from bbs_sku where product_id =442 order by price asc limit 0,1   product中附加属性
            product.setPrice((Float) doc.get("price"));
            //品牌ID Long  long类型不好使，用int
            product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));

            products.add(product);
        }
        /**分页对象*/
        Pagination pagination = new Pagination(
            productQuery.getPageNo(),
            productQuery.getPageSize(),
            (int)numFound,
            products
        );
        //页面展示
        String url="/search";
        pagination.pageView(url, params.toString());
        return pagination;
    }

    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;
    /** 保存商品信息到Solr服务器  */
    public void insertProductToSolr(Long id){
        //保存商品信息到solr服务器 两个service层进行交互需要用到消息队列
        //前台系统 搜索使用--solr全文检索，查询速度压秒级别
        SolrInputDocument doc = new SolrInputDocument();
        /** Field(key,value)-----key必须是在schema.xml中有的才可以设置值 */
        //商品id
        doc.setField("id",id);
        //商品名称--作为关键词搜索  ik分词器设置的
        Product p = productDao.selectByPrimaryKey(id);
        doc.setField("name_ik",p.getName());   //name_ik  solr中schema.xml中配置过
        //图片
        doc.setField("url",p.getImages()[0]);
        //价格 --售价  显示最便宜的  select price from bbs_sku where product_id = 442 order by price asc limit 1
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(id);
        skuQuery.setOrderByClause("price asc");
        skuQuery.setPageNo(1);
        skuQuery.setPageSize(1); //pagesize 和 pageno控制只查询一条语句
        skuQuery.setFields("price"); //控制只查询那个属性的
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        doc.setField("price",skus.get(0).getPrice());
        //品牌ID  schema.xml中没有，需要创建字段
        doc.setField("brandId",p.getBrandId());
        //时间 可选项
        try {
            solrServer.add(doc);
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //静态化（DMS）  ---前台系统中商品详情静态页在此处进行生成  对前台压力大进行的优化操作
    }

}


