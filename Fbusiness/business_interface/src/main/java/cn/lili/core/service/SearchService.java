package cn.lili.core.service;

import cn.itcast.common.page.Pagination;
import cn.lili.core.bean.product.Product;
import org.apache.solr.client.solrj.SolrServerException;

import java.util.List;

/**
 * Created by ABEL on 2018/6/2.
 */
public interface SearchService {

    //搜索
    public Pagination selectPaginationByQuery(Integer pageNo, Long brandId, String price, String keyword) throws SolrServerException;
    //保存信息到Solr中
    public void insertProductToSolr(Long id);

}
