package com.example.filters;

import com.example.entity.User;
import com.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author dongyudeng
 */
@Component
public class AuthorFilter implements Filter {
    final Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try{
            loginByBasic(request);
        }catch (RuntimeException runtimeException){
            logger.warn("Login by authorization header failed.",runtimeException);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
    private void loginByBasic(HttpServletRequest request){
        String authorHeader=request.getHeader("Authorization");
        if(authorHeader!=null&&authorHeader.startsWith("Basic")){
            logger.info("try authenticate by authorization header...");
            String userInformation=new String(Base64.getDecoder().decode(authorHeader.substring(6)), StandardCharsets.UTF_8);
            int position=userInformation.indexOf(":");
            String username= URLDecoder.decode(userInformation.substring(0,position),StandardCharsets.UTF_8);
            String password=URLDecoder.decode(userInformation.substring(position+1),StandardCharsets.UTF_8);
            User user=userService.login(username, password);
            request.getSession().setAttribute("NOW_USER",user);
            logger.info("User {} login by authorization header.",username);
        }
    }
}
