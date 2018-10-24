package cn.lili.core.service.cob;

import cn.lili.core.bean.BuyerCart;
import cn.lili.core.bean.BuyerItem;
import cn.lili.core.bean.order.Detail;
import cn.lili.core.bean.order.Order;
import cn.lili.core.bean.product.Sku;
import cn.lili.core.bean.user.Buyer;
import cn.lili.core.bean.user.BuyerQuery;
import cn.lili.core.dao.order.DetailDao;
import cn.lili.core.dao.order.OrderDao;
import cn.lili.core.dao.product.ColorDao;
import cn.lili.core.dao.product.ProductDao;
import cn.lili.core.dao.product.SkuDao;
import cn.lili.core.dao.user.BuyerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ABEL on 2018/6/9.
 */
@Service("buyerService")
public class BuyerServiceImpl  implements BuyerService{

    @Autowired
    private BuyerDao buyerDao;
    //通过用户名查询用户对象
    public Buyer selectBuyerByUsername(String username){
        BuyerQuery buyerQuery= new BuyerQuery();
        buyerQuery.createCriteria().andUsernameEqualTo(username);
        List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
        if(null != buyers && buyers.size() > 0){
            return buyers.get(0);
        }
        return null;
    }

    @Autowired
    private Jedis jedis;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private DetailDao detailDao;
    //保存订单
    public void insertOrder(Order order,String username){
        /** 保存订单 */
        //id 订单编号  全国唯一   由redis 提供
        Long oid = jedis.incr("oid");
        order.setId(oid);
        //加载购物车   从redis中获取   已经登陆了，redis中已经存了，就不用页面提交传过来的值了
        BuyerCart buyerCart = selectBuyerCartFromRedis(username);
        /** redis中只存了skuid 和 数量   因此需要装满购物车*/
        List<BuyerItem> items = buyerCart.getItems();
        for (BuyerItem buyerItem : items){
            buyerItem.setSku(selectSkuById(buyerItem.getSku().getId()));
        }
        //运费   购物车提供
        order.setDeliverFee(buyerCart.getFree());
        //总价
        order.setTotalPrice(buyerCart.getTotalPrice());
        //订单金额
        order.setOrderPrice(buyerCart.getProductPrice());
        //支付状态：0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
        if(order.getPaymentWay() == 1){
            order.setIsPaiy(0);
        }else {
            order.setIsPaiy(1);
        }
        //订单状态：0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
        order.setOrderState(0);
        //时间  ：  后台程序自己写的
        order.setCreateDate(new Date());
        //其余由页面提供
        //用户id  前台用户注册 （用户名、密码） 用户id--redis生成====企业中  K(用户名)：V(用户id) 将注册的保存在redis中
        String uid = jedis.get(username);
        order.setBuyerId(Long.parseLong(uid));
        //保存订单
        orderDao.insertSelective(order);
        /** 保存订单详情 */
        for (BuyerItem buyerItem : items) {
            Detail detail = new Detail();
            //ID   一个id对应多个订单详情，每一个购物项为一个订单详情
            Long did = jedis.incr("did");
            detail.setId(did);
            //订单ID
            detail.setOrderId(oid);
            //商品编号
            detail.setProductId(buyerItem.getSku().getProductId());
            // 商品名称
            detail.setProductName(buyerItem.getSku().getProduct().getName());
            //颜色
            detail.setColor(buyerItem.getSku().getColor().getName());
            //尺码
            detail.setSize(buyerItem.getSku().getSize());
            //价格
            detail.setPrice(buyerItem.getSku().getPrice());
            //数量
            detail.setAmount(buyerItem.getAmount());
            /** 上面的购物车提供*/
            //保存订单详情   遍历一次保存一次
            detailDao.insertSelective(detail);
        }
        //数据库 也保存完了，所以redis中就没有必要有了
        /**清空redis 中的购物车*/
        jedis.del("buyerCart:"+username);
        //正常应该删除的是  redis购物车中的指定项 不是全部清除  复选框没弄

    }

    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;
    /** 购物车：根据skuid获取Sku对象 （商品对象，颜色对象） */
    public Sku selectSkuById(Long id){
        //Sku 对象
        //商品对象
        //颜色对象
        Sku sku = skuDao.selectByPrimaryKey(id);
        sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        return sku;
    }

    /**从redis中取出所有购物车 */
    public BuyerCart selectBuyerCartFromRedis(String username){
        BuyerCart buyerCart = new BuyerCart();
        Map<String, String> map = jedis.hgetAll("buyerCart:" + username);
        if (null != map){
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String ,String> entry : entrySet){
                /**
                 * K : Skuid   V : amount
                 * */
                Sku sku = new Sku();
                Long  id = Long.valueOf(entry.getKey());
                sku.setId(id);
                BuyerItem buyerItem = new BuyerItem();
                buyerItem.setSku(sku);
                buyerItem.setAmount(Integer.parseInt(entry.getValue()));
                //追加商品到购物车
                buyerCart.addItem(buyerItem);
            }
        }
        return buyerCart;
    }

}
