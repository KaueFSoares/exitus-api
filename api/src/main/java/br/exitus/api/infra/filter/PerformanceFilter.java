package br.exitus.api.infra.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class PerformanceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        Long end = System.currentTimeMillis();
        System.out.println("Request: " + request.getRequestURI());
        System.out.println("Time: " + (end - start) + "ms");
    }
}
