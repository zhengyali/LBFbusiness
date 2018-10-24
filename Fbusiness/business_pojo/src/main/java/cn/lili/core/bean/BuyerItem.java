package cn.lili.core.bean;

import cn.lili.core.bean.product.Sku;

import java.io.Serializable;

/**
 * 购物项
 * Created by ABEL on 2018/6/10.
 */
public class BuyerItem implements Serializable{
    //1、skuid  只是用skuid可以表示，但是要存储商品名、尺码、颜色等，需要使用Sku对象
    private Sku sku;
    //2、Boolean 是否有货（由库存和用户购买量决定）
    private Boolean have = true;
    //3、数量（用户购买）
    private Integer amount = 1;

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Boolean getHave() {
        return have;
    }
    public void setHave(Boolean have) {
        this.have = have;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)   //比较地址
            return true;
        if (object == null || getClass() != object.getClass()) return false;

        BuyerItem buyerItem = (BuyerItem) object;

        return sku != null ? sku.getId().equals(buyerItem.sku.getId()) : buyerItem.sku == null;

    }

    @Override
    public int hashCode() {
        return sku != null ? sku.hashCode() : 0;
    }
}

