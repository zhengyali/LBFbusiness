package cob.al;

/**
 * Created by 鸭梨 on 2018/8/1.
 */

import java.util.List;

/**
 * 店铺
 */
public interface Store {

    /**
     * 把所指定的商品和相应的数量添加到购物车中。
     *
     * @param name String 商品名称，商品名称是商品的唯一标识(id)
     * @param quantity int 商品数量
     */
    void addItemToCart(String name, int quantity);

    /**
     * 添加所指定的商品和其价格添加到此店铺
     *
     * @param name String 商品名称，商品名称是商品的唯一标识(id)
     * @param price long 商品价格，以'分'为单位 100 = 1元， 10 = 1角
     */
    void addItemToStore(String name, long price);

    /**
     * 得到所有在购物车中的商品。
     *
     */
    List<LineItem> getCartItems();

    /**
     * 得到购物车中所有商品的总价值
     */
    long getCartTotal();

    /**  注意：
    对于同一个产品，会调用addItemToStore方法多次，每次价格不一定一样
    对于你自己的设计决定，请给出你所依据的相应假设。
            （可选）增加单元测试
    （可选）可运行代码

    加分题：
    如果希望你可以对同一个store实现多个购物车功能，请你用文字（或者加代码）来说明一下你的解决方案。*/
}

