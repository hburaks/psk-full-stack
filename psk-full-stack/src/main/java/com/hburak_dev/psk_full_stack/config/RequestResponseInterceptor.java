package com.hburak_dev.psk_full_stack.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RequestResponseInterceptor implements Filter {
    Logger logger = LoggerFactory.getLogger(RequestResponseInterceptor.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        logger.info("Incoming request - {} {} ", httpServletRequest.getMethod(), httpServletRequest.getRequestURL());
        chain.doFilter(request, response);
        logger.info("Response sent with status code = {} {} ", httpServletResponse.getStatus(), httpServletRequest.getRequestURL());
    }
}