package com.techgirl.inventory_management_system.config;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("X-CorrelationId", correlationId);
        chain.doFilter(request, response);
        MDC.clear();
    }
}

