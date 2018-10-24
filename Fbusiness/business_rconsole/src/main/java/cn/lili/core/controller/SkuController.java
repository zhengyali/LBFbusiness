package cn.lili.core.controller;

import cn.lili.core.bean.product.Sku;
import cn.lili.core.service.product.SkuService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by ABEL on 2018/5/31.
 */
@Controller
public class SkuController {

    @Autowired
    private SkuService skuService;
    /** 去库存页面 */
    @RequestMapping(value = "/sku/list.do")
    public String list(Long productId,Model model){
        List<Sku> skus = skuService.selectSkuListByProductId(productId);
        model.addAttribute("skus",skus);
        return "sku/list";
    }


    @RequestMapping(value = "/sku/addSku.do")
    public void addSku(Sku sku,HttpServletResponse response) throws IOException {
        skuService.updateSkuById(sku);

        JSONObject jo = new JSONObject();
        jo.put("message", "保存成功!");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jo.toString());

    }


}
