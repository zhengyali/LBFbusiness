package cob.al;

/**
 * Created by 鸭梨 on 2018/8/1.
 */
/**
 * 商品
 */
public interface LineItem {

    /**
     * 商品名称，是商品的唯一标识，可理解为id
     */
    String getName();

    /**
     * 商品的数量
     */
    int getQuantity();


}


//注意：
//        对于同一个产品，会调用addItemToStore方法多次，每次价格不一定一样
//        对于你自己的设计决定，请给出你所依据的相应假设。
//        （可选）增加单元测试
//        （可选）可运行代码
//
//        加分题：
//        如果希望你可以对同一个store实现多个购物车功能，请你用文字（或者加代码）来说明一下你的解决方案。
//
//        */
