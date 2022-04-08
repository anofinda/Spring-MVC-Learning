package com.example.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author dongyudeng
 */
@Component
public class ApiFilter implements Filter {
    final Logger logger=LoggerFactory.getLogger(getClass());
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        logger.info("Using Api {}", request.getRequestURI());
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
