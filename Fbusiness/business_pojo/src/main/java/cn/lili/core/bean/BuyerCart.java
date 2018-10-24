package cn.lili.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 * Created by ABEL on 2018/6/10.
 */
public class BuyerCart implements Serializable{

    /** 两部分 商品结果集  和  小计 */
    //1、商品结果集  List<BuyerItem>  每个购物项组成的集合
    private List<BuyerItem> items = new ArrayList<>();

    //添加购物项到购物车
    public void addItem(BuyerItem item){
        /**同款合并 -- 判断list 是否重复  */
        if(items.contains(item)){
           for (BuyerItem it : items){
              if(it.equals(item)){  // 两个对象，比较的是地址，因此需要在BuyerItem类中重写equals() 和 hashcode()
                  it.setAmount(item.getAmount() + it.getAmount());
              }
           }
        }else {
           items.add(item);
        }
    }

    public List<BuyerItem> getItems() {
        return items;
    }

    public void setItems(List<BuyerItem> items) {
        this.items = items;
    }

    //2、小计  （商品数量 、商品金额  、运费  、 总计）
    /**商品数量  非标准的Javabean转json会报错，因此将javabean转json时，将此属性忽略掉*/
    @JsonIgnore
    public Integer getProductAmount(){
        Integer result = 0;
        //计算过程
        for (BuyerItem buyerItem : items){
            result+= buyerItem.getAmount();
        }
        return result;
    }
    //商品金额
    @JsonIgnore
    public Float getProductPrice(){
        Float result = 0f;
        //计算过程
        for (BuyerItem buyerItem : items){
            result+= buyerItem.getAmount()*buyerItem.getSku().getPrice();
        }
        return result;
    }
    //运费
    @JsonIgnore
    public Float getFree(){
        Float result = 0f;
        if(getProductPrice()<79){
            result = 5f;
        }
        return result;
    }
    //总金额
    @JsonIgnore
    public Float getTotalPrice(){
        Float result = 0f;
        result = getProductPrice()+getFree();
        return  result;
    }

}
