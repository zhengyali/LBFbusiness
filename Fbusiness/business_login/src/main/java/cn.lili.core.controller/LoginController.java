package cn.lili.core.controller;

import cn.lili.common.utils.RequestUtils;
import cn.lili.core.bean.user.Buyer;
import cn.lili.core.service.cob.BuyerService;
import cn.lili.core.service.cob.user.SessionProvider;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 单点登陆系统
 * 去登陆页面  GET
 * 提交登陆表单 POST
 * 加密MD5+十六进加密 最终杀手- 加盐（密码过于简单）有规则密码
 */
@Controller
public class LoginController {

	/**去登陆页面*/
    @RequestMapping(value = "/login.aspx",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    /**判断用户是否登陆*/
    @RequestMapping(value = "/isLogin.aspx")
    public @ResponseBody
    MappingJacksonValue isLogin(String callback, HttpServletRequest request, HttpServletResponse response){
        Integer result = 0 ;
        //判断用户是否已经登陆、
        String username = sessionProvider.getAtrributeForUsername(RequestUtils.getCSESSIONID(request, response));
        if(null != username){
            result = 1;
        }
        /**  由spring提供的一种将数据返回去的方式 */
        MappingJacksonValue mjv = new MappingJacksonValue(result);//是个包装类
        // 将jsonp 的传过来的再传回去
        mjv.setJsonpFunction(callback);
        //将这个对象以json串的格式传回去
        return mjv;
    }


    @Autowired
    private BuyerService buyerService;
    @Autowired
    private SessionProvider sessionProvider;
    //提交登录
    @RequestMapping(value = "/login.aspx",method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl , HttpServletRequest request, HttpServletResponse response, Model model){
        //1:用户名不能为空
        if(null != username){
            //2:密码不能为空
            if (null != password){
                //3:用户名必须正确  数据库查
                Buyer buyer = buyerService.selectBuyerByUsername(username);
                if(null != buyer){
                    //4:密码必须正确   加密 比较
                    if(buyer.getPassword().equals(encodePassword(password))){
                        //5:保存用户名到Session中(Redis中）
                         sessionProvider.setAtrribuerForUsername(RequestUtils.getCSESSIONID(request,response),buyer.getUsername());
                        //6:跳转到之前访问页面
                        if (null != returnUrl){
                            return "redirect:" + returnUrl;
                        }
                        return "redirect:http://localhost:8097";
                    }else {
                        model.addAttribute("error", "密码必须正确");
                    }
                }else {
                   model.addAttribute("error","用户名必须正确");
                }
            }else {
                model.addAttribute("error", "密码不能为空");
            }
        }else {
            model.addAttribute("error", "用户名不能为空");
        }
        return "login";
    }

    //加密
    public String encodePassword(String password){
        /**加盐*/
        //password = "dhauihaoeoaiue"+password+"duiehiauhejxnbn";

        //1、MD5 算法
        String algorithm = "MD5";
        char[] encodeHex = null;
        try {
            //MD5加密
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            //加密后的数组
            byte[] digest = instance.digest(password.getBytes());
            //2、十六进制
            encodeHex = org.apache.commons.codec.binary.Hex.encodeHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String (encodeHex);
    }

    public static void main(String[] args) {//测试
        LoginController l = new LoginController();
        String w = l.encodePassword("123456");
        System.out.println(w);
    }
	
}
