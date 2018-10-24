package cn.lili.core.controller;

import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.Brand;
import cn.lili.core.bean.product.Color;
import cn.lili.core.bean.product.Product;
import cn.lili.core.service.product.BrandService;
import cn.lili.core.service.product.ProductService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 商品管理
 * 列表
 * 添加
 * 上架
 * Created by ABEL on 2018/5/28.
 */
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @RequestMapping(value = "/product/list.do")
    public String list(Integer pageNo, String name, Long brandId, Boolean isShow, Model model){
       /** 品牌结果集 */
        List<Brand> brands = brandService.selectBrandListByQuery(1);
        model.addAttribute("brands",brands);

        Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
        /**数据回显*/
        model.addAttribute("pagination",pagination);

        model.addAttribute("name",name);
        model.addAttribute("brandId",brandId);
        if(null!=isShow){
            model.addAttribute("isShow",isShow);
        }else {
            model.addAttribute("isShow",false);
        }
        return "product/list";
    }
    /**去商品添加页面*/
    @RequestMapping(value = "/product/toadd.do")
    public String toadd(Model model){
        //颜色结果集
        List<Color> colors = productService.selectColorList();
        model.addAttribute("colors",colors);
        /** 品牌结果集 */
        List<Brand> brands = brandService.selectBrandListByQuery(1);
        model.addAttribute("brands",brands);
        return "/product/add";
    }

    /** 保存商品和sku */
    @RequestMapping(value = "/product/add.do")
    public String add(Product product){
        productService.insertProduct(product);
        return "redirect:/product/list.do";
    }
    /** 上架 */
    @RequestMapping(value = "/product/isShow.do")
    public String isShow(Long[] ids){
        productService.isShow(ids);
        return "forward:/product/list.do";
    }

}
