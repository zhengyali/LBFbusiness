package cn.lili.core.dao.product;

import cn.lili.core.bean.product.Brand;
import cn.lili.core.bean.product.BrandQuery;

import java.util.List;

/**
 * Created by ABEL on 2018/5/19.
 */
public interface BrandDao {

    /**查询结果集  条件包括：名称、是否可用、当前页、页数 可以将条件做封装*/
    public List<Brand> selectBrandListByQuery(BrandQuery brandQuery);
    /**查询总条数 符合条件的*/
    public Integer selectCount(BrandQuery brandQuery);
    /** 通过id查询品牌 */
    public Brand selectBrandById(Long id);
    /** 修改 */
    public void updateBrandById(Brand brand);
    /**添加*/
    public void addBrand(Brand brand);
    /**批量删除*/
    public void deletes(Long[] ids);
}
