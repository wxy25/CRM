package com.yjxxt.crm.config;

import com.yjxxt.crm.interceptors.NoLoginIntercerpor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    //实例化对象
    @Bean
    public NoLoginIntercerpor noLoginIntercerpor(){
        return new NoLoginIntercerpor();
    }

    /**
     *
     * @param registry   先添加拦截  在设置放行
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //拦截路径
        registry.addInterceptor(noLoginIntercerpor())
                //设置拦截器的过滤规则   拦截路径
                .addPathPatterns("/**")
                //设置不需要拦截的过滤规则  放行路径
                .excludePathPatterns("/index","/user/login","/js/**","/css/**","/images/**","/lib/**");
    }
}
