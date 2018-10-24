package cn.lili.core.service.cms.staticpage;

import cn.lili.core.service.staticpage.StaticPageService;
import freemarker.template.Template;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Map;


/**
 * 静态化
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

    //声明 注入
    private Configuration conf;  //从这里来的模板路径，故此对象创建时也需要设置默认编码
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.conf = freeMarkerConfigurer.getConfiguration();
    }
    //静态化 --商品详情   id是MQ发过来的，是String类型的
    public void productStaticPage(Map<String,Object> root,String id){
        //输出路径   全路径
        String path = getPath("/html/product/"+id+".html");
        File f  = new File(path);
        File parentFile = f.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        Writer out = null;
        try {//考虑乱码
            //读文件  UTF-8  导入模板
            Template template = conf.getTemplate("product.html");
            //输出   UTF-8
            out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            //out = new FileWriter(f);
            //处理
            template.process(root,out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if (null != out){  //没实例化不用关
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //获取路径
    public String getPath(String name){
        return servletContext.getRealPath(name);
    }
    //声明
    private  ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) { //实现这个ServletContextAware接口 ，然后获取上下文，根据上下文得到全路径
           this.servletContext = servletContext;
    }
    // 静态化 订单 商品等

}
