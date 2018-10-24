package cn.lili.core.service.product;


import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.Brand;
import cn.lili.core.bean.product.BrandQuery;
import cn.lili.core.dao.product.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ABEL on 2018/5/19.
 */
@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;
    //查询分页对象
    public Pagination selectPaginationByQuery(String name,Integer isDisplay,Integer pageNo){
        BrandQuery brandQuery = new BrandQuery();
        //当前页
        brandQuery.setPageNo(Pagination.cpn(pageNo));//pageNo为空设置为最小值1
        //每页数
        brandQuery.setPageSize(3);
        //用StringBuilder拼接分页链接的参数  前面加& 没关系
        StringBuilder params = new StringBuilder();

        //条件
        if(null != name){
            brandQuery.setName(name);
            params.append("name=").append(name);
        }
        if(null != isDisplay){
            brandQuery.setIsDisplay(isDisplay);
            params.append("&isDisplay=").append(isDisplay);
        }else{
            brandQuery.setIsDisplay(1);
            params.append("&isDisplay=").append(1);
        }

        Pagination pagination = new Pagination(
                brandQuery.getPageNo(),
                brandQuery.getPageSize(),
                brandDao.selectCount(brandQuery)
        );
        //设置结果集
        pagination.setList(brandDao.selectBrandListByQuery(brandQuery));
        //分页展示  Pagniation 封装好的
        String url = "/brand/list.do";

        pagination.pageView(url, params.toString());

        return pagination;
    }

    /** 通过id查询品牌 */
    public Brand selectBrandById(Long id) {
        return brandDao.selectBrandById(id);
    }

    @Autowired
    private Jedis jedis;
    /**修改*/
    public void updateBrandById(Brand brand){
        //修改redis
        jedis.hset("brand", String.valueOf(brand.getId()),brand.getName());
        //修改数据到数据库
        brandDao.updateBrandById(brand);
    }

    /** 从redis中查询结果集*/
    public List<Brand> selectBrandListFromRedis(){
        List<Brand> brands= new ArrayList<>();
        Map<String, String> hgetAll = jedis.hgetAll("brand");
        Set<Map.Entry<String, String>> entrySet = hgetAll.entrySet();
        for (Map.Entry<String,String> entry:entrySet ){
            Brand brand = new Brand();
            brand.setId(Long.parseLong(entry.getKey()));
            brand.setName(entry.getValue());
            brands.add(brand);
        }
        return  brands;
    }

    /**添加品牌*/
    public void addBrand(Brand brand){
        //保存到redis
        jedis.hset("brand", String.valueOf(brand.getId()),brand.getName());
        //保存到数据库
        brandDao.addBrand(brand);
    }
    /**删除*/
    public void deletes(Long[] ids) {
        brandDao.deletes(ids);
    }

    /** 商品管理 需要品牌结果集  */
    public List<Brand> selectBrandListByQuery(Integer isDisplay){
        BrandQuery brandQuery= new BrandQuery();
        brandQuery.setIsDisplay(isDisplay);
        List<Brand> brands = brandDao.selectBrandListByQuery(brandQuery);
        return brands;
    }

}
