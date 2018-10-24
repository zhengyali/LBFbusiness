package cn.lili.core.service.cob.user;

/**
 * Created by ABEL on 2018/6/9.
 */
public interface SessionProvider {

    //先行提供接口。
    //保存用户名到Redis  此处name指的是键值
    public void setAtrribuerForUsername(String name,String value);

    //取用户名
    public String getAtrributeForUsername(String name);

    //验证

    //退出登录  消除session

}
