package cn.lili.core.service.product;

import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.Brand;
import cn.lili.core.bean.product.BrandQuery;

import java.util.List;

/**
 * Created by ABEL on 2018/5/19.
 */
public interface BrandService {
    public Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo);
    public Brand selectBrandById(Long id);
    public void updateBrandById(Brand brand);
    public void addBrand(Brand brand);
    public void deletes(Long[] ids);
    public List<Brand> selectBrandListByQuery(Integer isDisplay);
    public List<Brand> selectBrandListFromRedis();
}
