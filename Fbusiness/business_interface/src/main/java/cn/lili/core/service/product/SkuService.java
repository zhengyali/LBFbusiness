package cn.lili.core.service.product;

import cn.lili.core.bean.BuyerCart;
import cn.lili.core.bean.product.Sku;

import java.util.List;

/**
 * Created by ABEL on 2018/5/31.
 */
public interface SkuService {

    public List<Sku> selectSkuListByProductId(Long id);

    public void updateSkuById(Sku sku);

    public Sku selectSkuById(Long id);

    public void insertBuyerCartToRedis(BuyerCart buyerCart, String username);

    public BuyerCart selectBuyerCartFromRedis(String username);
}
