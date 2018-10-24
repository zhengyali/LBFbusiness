package cn.lili.core.service.cms;

import cn.lili.core.bean.product.Product;
import cn.lili.core.bean.product.Sku;
import cn.lili.core.bean.product.SkuQuery;
import cn.lili.core.dao.product.ColorDao;
import cn.lili.core.dao.product.ProductDao;
import cn.lili.core.dao.product.SkuDao;
import cn.lili.core.service.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 评论
 * 晒单
 * 广告
 * 静态化
 * @author lx
 *
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	//查询商品
	public Product selectProductById(Long productId){
		
		return productDao.selectByPrimaryKey(productId);
	}
	//查询Sku结果集 (包括颜色）  有货  
	public List<Sku> selectSkuListByProductId(Long productId){
		SkuQuery skuQuery = new SkuQuery(); //大于0
		skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
}
