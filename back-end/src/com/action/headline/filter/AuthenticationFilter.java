package com.action.headline.filter;


import com.action.headline.common.Result;
import com.action.headline.common.ResultCodeEnum;
import com.action.headline.util.JwtUtil;
import com.action.headline.util.WebUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/headline/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        HttpServletResponse response =(HttpServletResponse) servletResponse;

        String token = request.getHeader("token");
        boolean flag = token != null && JwtUtil.validateToken(token);

        if(flag){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            WebUtil.writeJson(response, Result.build(null, ResultCodeEnum.NOT_LOGIN));
        }
    }
}
