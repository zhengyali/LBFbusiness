package cn.lili.core.controller;

import cn.lili.common.utils.RequestUtils;
import cn.lili.common.web.Constants;
import cn.lili.core.bean.BuyerCart;
import cn.lili.core.bean.BuyerItem;
import cn.lili.core.bean.order.Order;
import cn.lili.core.bean.product.Sku;
import cn.lili.core.bean.user.Buyer;
import cn.lili.core.service.cob.BuyerService;
import cn.lili.core.service.cob.user.SessionProvider;
import cn.lili.core.service.product.SkuService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.List;

/**
 * 购物车
 * 去购物车页面
 * 添加商品到购物车
 * 删除
 *   + - 数量增减
 * Created by ABEL on 2018/6/10.
 */
@Controller
public class CartController {

    @Autowired
    private SessionProvider sessionProvider;

    /** 加入购物车 */
    @RequestMapping(value = "/addCart")
    public String addCart(Long skuId, Integer amount, Model model,
                         HttpServletRequest request, HttpServletResponse response) throws Exception{
        ObjectMapper om = new ObjectMapper();
        //null 就不转了
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);//null 不转
        //声明购物车
        BuyerCart buyerCart = null ;

        //1、从request中取Cookies,遍历Cookie 取出之前的购物车
        Cookie[] cookies = request.getCookies();
        if(null != cookies && cookies.length >0){
            //遍历Cookie 取出之前的购物车
            for(Cookie cookie:cookies){
                //2、判断Cookie中有没有购物车
                if(Constants.BUYER_CART.equals(cookie.getName())){
                    String value = cookie.getValue();
                    //转回对象
                    buyerCart = om.readValue(value, BuyerCart.class);
                    break;
                }
            }
        }
        //判断购物车是否为空
        if (null == buyerCart) {
            buyerCart = new BuyerCart();
        }
        /** 为了简化代码，先把当前的加到购物车，再判断登陆与否，持久化在哪里*/
        Sku sku = new Sku();
        sku.setId(skuId);
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setSku(sku);
        buyerItem.setAmount(amount);
        //追加商品到购物车
        buyerCart.addItem(buyerItem);
        /** 用户是否登陆  去redis中查 */
        String username = sessionProvider.getAtrributeForUsername(RequestUtils.getCSESSIONID(request, response));
        if (null != username){//已经登陆了
                // 3：有  把购物车中商品添加到Redis的购物车中，
                skuService.insertBuyerCartToRedis(buyerCart,username);
                //清理之前Cookie
                Cookie cookie = new Cookie(Constants.BUYER_CART,null);//覆盖
                cookie.setMaxAge(0);
                cookie.setPath("/");  //这个路径下的cookie
                response.addCookie(cookie);

            // 4：没有
            // 5：直接添加当前商品到Redis中的购物车里

        }else {
            /**未登录*/
            // 3：有 （cookie保存七天）
            // 4：没有 创建购物车
            //判断购物车是否为空
            //5：有 追加当前商品到购物车
            //追加商品到购物车   上面的这些都抽取出来了
            //6：创建Cookie 把新购物车放进去
            StringWriter w = new StringWriter();
            om.writeValue(w, buyerCart);
            Cookie cookie = new Cookie(Constants.BUYER_CART, w.toString());
            //设置时间  cookie存活时间
            cookie.setMaxAge(60 * 60 * 24);
            //设置路径
            cookie.setPath("/");  //   带/的可以将数据带过去
            // 设置域（跨域）
            //7：保存Cookie写回浏览器
            response.addCookie(cookie);

        }
        return "redirect:/toCart";
    }

    @Autowired
    private SkuService skuService;
    /**去购物车页面*/
    @RequestMapping(value = "/toCart")
    public String toCart(Model model,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper om = new ObjectMapper();
        //null 就不转了
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);//null 不转
        //声明购物车
        BuyerCart buyerCart = null ;

        //1、从request中取Cookies,遍历Cookie 取出之前的购物车
        Cookie[] cookies = request.getCookies();
        if(null != cookies && cookies.length >0){
            //遍历Cookie 取出之前的购物车
            for(Cookie cookie:cookies){
                //2、判断Cookie中有没有购物车
                if(Constants.BUYER_CART.equals(cookie.getName())){
                    //转回对象
                    buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
                    break;
                }
            }
        }
        /** 用户是否登陆  去redis中查 */
        String username = sessionProvider.getAtrributeForUsername(RequestUtils.getCSESSIONID(request, response));
        if (null != username) {//已经登陆了
            if(null != buyerCart){
           // 3：再把购物车保存到Redis中，清理Cookie
            skuService.insertBuyerCartToRedis(buyerCart,username);
            Cookie cookie = new Cookie(Constants.BUYER_CART,null);//覆盖
            cookie.setMaxAge(0);
            cookie.setPath("/");  //这个路径下的cookie
            response.addCookie(cookie);
            }
            // 4：从Redis中取出所有的购物车   service
            buyerCart = skuService.selectBuyerCartFromRedis(username);
        }
        if (null != buyerCart){
            //2: 有 给购物车装具体数据
            List<BuyerItem> items = buyerCart.getItems();//此时buyerCart中只有 skuid和amount数量
            for (BuyerItem buyerItem :items){
                buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
             //   System.out.println(buyerItem.getHave());
            }
        }
        //3、没有
        //4、回显购物车内容
        model.addAttribute("buyerCart",buyerCart);
        //跳转到页面中去
        return "cart";
    }

    /** 去结算 */
    @RequestMapping(value = "/buyer/trueBuy")
    public String  trueBuy(Long []skuid,Model model,
                           HttpServletRequest request, HttpServletResponse response){
        //购物车中必须有商品（用户点返回页面，页面显示的是假数据，所以必须判断有商品）
        String username = sessionProvider.getAtrributeForUsername(RequestUtils.getCSESSIONID(request, response));
        BuyerCart buyerCart = skuService.selectBuyerCartFromRedis(username);
        List<BuyerItem> items = buyerCart.getItems();//有可能只是一个空的购物车对象
        Boolean flag = false;
        if (items.size()>0){
            //此时buyerCart中只有 skuid和amount数量
            for (BuyerItem buyerItem :items){
                buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
                //商品必须有库存
                if (buyerItem.getSku().getStock()< buyerItem.getAmount()){
                       //无货
                    buyerItem.setHave(false);
                    flag = true;
                }
            }
            //至少一款无货
            if(flag){
                // 如果都没货  不进入下个页面
                model.addAttribute("buyerCart",buyerCart);
                return "cart";
            }
        }else{
            return "redirect:/toCart";
        }
        // 如果都有货  进入下个页面
        return "order";
    }
    @Autowired
    private BuyerService buyerService;
    @RequestMapping(value = "/buyer/submitOrder",method = RequestMethod.GET)
    public String submitOrder(Model model,Order order,
                              HttpServletRequest request, HttpServletResponse response){
        String username = sessionProvider.getAtrributeForUsername(RequestUtils.getCSESSIONID(request, response));
        System.out.println(order.getNote());
        buyerService.insertOrder(order,username);
        //回显页面  不做了
        return "success";
    }

}
