package cn.lili.core.service.cob.user;

import cn.lili.common.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/** 保存用户名或验证码到Redis
 * Sesssion 共享
 * Created by ABEL on 2018/6/9.
 */

public class SessionProviderImpl implements  SessionProvider{
    @Autowired
    private Jedis jedis;
    private Integer exp = 30;
    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public void setAtrribuerForUsername(String name, String value) {
        //保存用户名到Redis
        jedis.set(name+":"+Constants.USER_NAME,value);
        //时间
        jedis.expire(name+":"+Constants.USER_NAME,60*exp);
    }

    public String getAtrributeForUsername(String name) {
        String value = jedis.get(name + ":" + Constants.USER_NAME);
        if (null != value){//Session过期时间：最后一次访问开始计时---30分钟
            jedis.expire(name+":"+Constants.USER_NAME,60*exp);
        }
        return value;
    }
}
