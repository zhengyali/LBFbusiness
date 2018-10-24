package cn.lili.core.controller;

import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.Brand;
import cn.lili.core.bean.product.Color;
import cn.lili.core.bean.product.Product;
import cn.lili.core.bean.product.Sku;
import cn.lili.core.service.CmsService;
import cn.lili.core.service.SearchService;
import cn.lili.core.service.product.BrandService;

import java.util.*;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前台商品
 * Created by ABEL on 2018/6/1.
 */
@Controller
public class ProductController {
    //去首页  入口
    @RequestMapping(value = "/")
    public String index(){
        return "index";
    }

    @Autowired
    private SearchService searchService;
    @Autowired
    private BrandService brandService;

    /** 搜索 */
    @RequestMapping(value = "/search")
    public String search(Integer pageNo,String keyword,Long brandId,String price, Model model) throws SolrServerException {
        Pagination pagination = searchService.selectPaginationByQuery(pageNo,brandId,price,keyword);
        model.addAttribute("pagination",pagination);

        List<Brand> brands = brandService.selectBrandListFromRedis();
        model.addAttribute("brands",brands);
        model.addAttribute("brandId",brandId);
        model.addAttribute("price",price);
        //已选条件 容器 Map
        Map<String,String> map = new HashMap<String,String>();
        //品牌
        if(null != brandId){
            for (Brand brand : brands) {
                if(brandId == brand.getId()){
                    map.put("品牌", brand.getName());
                    break;
                }
            }
        }
        //价格  0-99     1600
        if(null != price){
            if(price.contains("-")){
                map.put("价格", price);
            }else{
                map.put("价格", price + "以上");
            }
        }
        model.addAttribute("map", map);
        return "search";
    }

    @Autowired
    private CmsService cmsService;
    //去商品详情页面
    @RequestMapping(value = "/product/detail")
    public String detail(Long id,Model model){
        System.out.println("hdakshdkajheuirucnk"+id);
      //商品
        Product product = cmsService.selectProductById(id);
        //sku
        List<Sku> skus = cmsService.selectSkuListByProductId(id);
        System.out.println(skus.size());
        model.addAttribute("product", product);
        model.addAttribute("skus", skus);
        /** 遍历skus，去重，查库存表时，相同颜色不同尺码都单独为一个对象，会造成页面遍历时，颜色重复出现 */
        Set<Color> colors  = new HashSet<>();/**  注意  hashset去重 ，存对象的情况下 比较的是对象的地址  因此我们需要重写 equals方法和hashcode方法  Color类中*/
        for (Sku sku:skus){
            colors.add(sku.getColor());
        }
        model.addAttribute("colors",colors);
        return "product";
    }




}
