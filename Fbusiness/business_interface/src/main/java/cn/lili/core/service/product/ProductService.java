package cn.lili.core.service.product;

import cn.lili.core.bean.product.Color;
import cn.lili.core.bean.product.Product;

import java.util.List;

/**
 * Created by ABEL on 2018/5/28.
 */
public interface ProductService {
    public cn.itcast.common.page.Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow);
    /**加载颜色*/
    public List<Color> selectColorList();
    /** 保存商品 */
    public void insertProduct(Product product);
    /**上架 */
    public void  isShow(Long[] ids);
}
