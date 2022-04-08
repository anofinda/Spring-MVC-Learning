package com.example;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author dongyudeng
 */
@Configuration
@ComponentScan
@MapperScan("com.example.Mappers")
@PropertySource("classpath:/jdbc.properties")
@EnableWebMvc
@EnableTransactionManagement
public class AppConfig {
    @Bean
    DataSource createDataSource(@Value("${jdbc.url}") String jdbcUrl,
                                @Value("${jdbc.username}") String jdbcUsername,
                                @Value("${jdbc.password}") String jdbcPassword) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
        var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean
    PlatformTransactionManager createTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Order(2)
    @Bean
    HandlerInterceptor createLocaleInterceptor(){
        var localeInterceptor=new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    @Bean
    WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                for (var interceptor : interceptors) {
                    String interceptorName=interceptor.getClass().getSimpleName();
                    if(interceptorName.startsWith("Api")){
                        registry.addInterceptor(interceptor).addPathPatterns("/Api/*");
                    }else if(interceptorName.startsWith("User")){
                        registry.addInterceptor(interceptor).addPathPatterns("/User/*");
                    }else {
                        registry.addInterceptor(interceptor);
                    }
                }
            }
        };
    }

    @Bean("localeResolver")
    LocaleResolver createLocaleResolver() {
        var localResolver = new CookieLocaleResolver();
        localResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        localResolver.setDefaultTimeZone(TimeZone.getDefault());
        return localResolver;
    }

    @Bean("i18n")
    MessageSource createMessageSource() {
        var messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("messages");
        return messageSource;
    }
    @Bean
    SpringExtension createSpringExtension(@Autowired @Qualifier("i18n") MessageSource messageSource){
        return new SpringExtension(messageSource);
    }
    @Bean
    ViewResolver createViewResolver(@Autowired ServletContext servletContext,@Autowired SpringExtension springExtension) {
        PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(true)
                .cacheActive(true)
                .loader(new ServletLoader(servletContext))
                .extension(springExtension)
                .build();
        PebbleViewResolver viewResolver = new PebbleViewResolver(engine);
        viewResolver.setPrefix("/WEB-INF/templates/");
        viewResolver.setSuffix("");
        return viewResolver;
    }

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        Context context = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
        context.setResources(resources);
        tomcat.start();
        Server server = tomcat.getServer();
        server.await();
    }
}
