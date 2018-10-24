package cn.lili.core.controller;

import cn.lili.core.bean.TestTb;
import cn.lili.core.service.TestTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * 后台管理
 */
@Controller
@RequestMapping(value = "/control")
public class CenterController {

    //入口
    @RequestMapping(value = "/index.do")
    public String index(Model model){
        return "index";
    }
    /**首页头*/
    @RequestMapping(value = "/top.do")
    public String top(Model model){
        return "top";
    }
    /**首页身体*/
    @RequestMapping(value = "/main.do")
    public String main(Model model){
        return "main";
    }
    /** 身体左*/
    @RequestMapping(value = "/left.do")
    public String left(Model model){
        return "left";
    }
    /**身体右*/
    @RequestMapping(value = "/right.do")
    public String right(Model model){
        return "right";
    }
    /**商品身体*/
    @RequestMapping(value = "frame/product_main.do")
    public String product_main(Model model){
        return "frame/product_main";
    }
    /**商品 业务功能*/
    @RequestMapping(value = "frame/product_left.do")
    public String product_left(Model model){
        return "frame/product_left";
    }


  /*  搭建时测试：*/
   @Autowired
    private TestTbService testTbService;
   /* //入口
    *
     * ModelAndView  跳转视图+数据  不用
     * void  ：异步时 ajax
     * String  ：只是做跳转视图 用Model带数据  常用
     * */
    @RequestMapping(value = "test/index.do")
    public String test(Model model){
        TestTb testTb=new TestTb();
        testTb.setName("测试spring和duboo");
        testTb.setBirthday(new Date());
        testTbService.insertTestTb(testTb);
        return "test";
    }


}
