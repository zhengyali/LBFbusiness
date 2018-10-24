package cn.lili.core.interceptor;

import cn.lili.common.utils.RequestUtils;
import cn.lili.core.service.cob.user.SessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截的是Controller层之前的  Controller即springmvc中handler处理器
 * Created by ABEL on 2018/6/11.
 */
public class CustomInterceptor implements HandlerInterceptor{

    @Autowired
    private SessionProvider sessionProvider;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //必须登录
        String username = sessionProvider.getAtrributeForUsername(RequestUtils.getCSESSIONID(request, response));
        if(null == username){
            //未登录
            //重定向到登录页面    returnUrl ---此处决定登录后跳回哪个页面
            response.sendRedirect("http://localhost:8090/login.aspx?returnUrl=http://localhost:8097/");
            return false;
        }
        //放行 true   不放行false
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
