package cn.lili.core.service.cob;

import cn.lili.core.bean.order.Order;
import cn.lili.core.bean.user.Buyer;

/**
 * Created by ABEL on 2018/6/9.
 */
public interface BuyerService {

    public Buyer selectBuyerByUsername(String username);
    public void insertOrder(Order order, String username);
}
