package com.example.interceptors;

import com.example.MBean.BlacklistMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dongyudeng
 */
@Order(2)
@Component
public class BlacklistInterceptor implements HandlerInterceptor {
    final Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    BlacklistMBean blacklistMBean;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip=request.getRemoteAddr();
        logger.info("check ip address {}...", ip);
        if(blacklistMBean.checkIn(ip)){
            logger.warn("will block ip {} for it is in blacklist.", ip);
            response.sendError(403);
            return false;
        }
        return true;
    }
}
