package cn.lili.core.service;

import cn.lili.core.bean.product.Product;
import cn.lili.core.bean.product.Sku;

import java.util.List;



public interface CmsService {

	//查询商品
	public Product selectProductById(Long productId);
	
	//查询Sku结果集 (包括颜色）  有货  
	public List<Sku> selectSkuListByProductId(Long productId);

}
