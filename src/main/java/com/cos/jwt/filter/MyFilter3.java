package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");
        //토큰 : 코스
        if(request.getMethod().equals("POST")){
            System.out.println("POST 요청됨");
            String headerAuth = request.getHeader("Authorization");
            System.out.println(headerAuth);

            if(headerAuth.equals("cos")){
                filterChain.doFilter(request,response);
            }else {
                PrintWriter out = response.getWriter();
                out.print("인증안됨");
            }
        }

    }

}
