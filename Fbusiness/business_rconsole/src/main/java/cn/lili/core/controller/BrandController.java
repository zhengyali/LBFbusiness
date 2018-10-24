package cn.lili.core.controller;

import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.Brand;
import cn.lili.core.service.product.BrandService;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理品牌管理
 * 列表
 * 删除
 * 修改
 * 添加
 * 删除（单）
 * Created by ABEL on 2018/5/19.
 */
@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;
    //查询
    @RequestMapping(value = "/brand/list.do")
    public String list(String name, Integer isDisplay, Integer pageNo, Model model){
        Pagination pagination=brandService.selectPaginationByQuery(name,isDisplay,pageNo);
        model.addAttribute("pagination",pagination);
        model.addAttribute("name",name);
        if(null!=isDisplay){  //默认显示为  是
            model.addAttribute("isDisplay",isDisplay);
        }else  model.addAttribute("isDisplay",1);

        return "brand/list";
    }

    /** 去修改页面*/
    @RequestMapping(value = "/brand/toEdit.do")
    public String toEdit(Long id, Model model,String name,Integer isDisplay,Integer pageNo){
        Brand brand = brandService.selectBrandById(id);
        model.addAttribute("brand",brand);
        model.addAttribute("name",name);
        model.addAttribute("isDisplay",isDisplay);
        model.addAttribute("pageNo",pageNo);
        return "brand/edit";
    }

    /**修改*/
    @RequestMapping(value = "/brand/edit.do")
    public String edit(Brand brand, Model model,String tname,Integer tisDisplay,Integer tpageNo){
       brandService.updateBrandById(brand);
        model.addAttribute("name",tname);
        model.addAttribute("isDisplay",tisDisplay);
        model.addAttribute("pageNo",tpageNo);
      // return "redirect:/brand/list.do";
       return list(tname,tisDisplay,tpageNo,model);
    }
    /***/
    @RequestMapping(value = "/brand/toadd.do")
    public String toAdd(){
        return "brand/add";
    }

    @RequestMapping(value = "/brand/add.do")
    public String add(Brand brand){
        brandService.addBrand(brand);
        return "redirect:/brand/list.do";
    }

    @RequestMapping(value = "/brand/deletes.do")
    public String deletes(Long[] ids){
        brandService.deletes(ids);
        return "forward:/brand/list.do";
    }

}
