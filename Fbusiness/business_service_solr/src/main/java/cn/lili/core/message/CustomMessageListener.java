package cn.lili.core.message;

import cn.lili.core.bean.product.Color;
import cn.lili.core.bean.product.Product;
import cn.lili.core.bean.product.Sku;
import cn.lili.core.service.CmsService;
import cn.lili.core.service.SearchService;
import cn.lili.core.service.staticpage.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.*;

/**
 * 处理类
 * Created by ABEL on 2018/6/4.
 */
public class CustomMessageListener implements MessageListener {

    @Autowired
    private SearchService searchService;
    @Autowired
    private StaticPageService staticPageService;//在本项目中
    @Autowired
    private CmsService cmsService;
    public void onMessage(Message message) {
        ActiveMQTextMessage am = (ActiveMQTextMessage) message;
        try {
            System.out.println("ActiveMQ消息是："+am.getText());
            /** 处理真正业务 保存商品信息到solr服务器*/
            searchService.insertProductToSolr(Long.parseLong(am.getText()));
            /** 静态化 */
            String id = am.getText();
            //数据
            Map<String,Object> root = new HashMap<>();
            //商品
            Product product = cmsService.selectProductById(Long.parseLong(id));
            //sku
            List<Sku> skus = cmsService.selectSkuListByProductId(Long.parseLong(id));
            root.put("product", product);
            root.put("skus", skus);
            /** 遍历skus，去重，查库存表时，相同颜色不同尺码都单独为一个对象，会造成页面遍历时，颜色重复出现 */
            Set<Color> colors  = new HashSet<>();/**  注意  hashset去重 ，存对象的情况下 比较的是对象的地址  因此我们需要重写 equals方法和hashcode方法  Color类中*/
            for (Sku sku:skus){
                colors.add(sku.getColor());
            }
            root.put("colors",colors);
            //静态化生成
            staticPageService.productStaticPage(root,id);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }



}
