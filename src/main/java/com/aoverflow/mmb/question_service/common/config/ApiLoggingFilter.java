package com.aoverflow.mmb.question_service.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;

@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {
    @Value("${spring.application.name}")
    private String applicationName;
    private static final String APPLICATION_NAME = "APPLICATION_NAME";
    private static final Set<String> allowedHeaders = Set.of("x-user-id", "content-type", "content-length");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        MDC.put(APPLICATION_NAME, applicationName);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logRequestDetails(wrappedRequest);
            logResponseDetails(wrappedResponse);
            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) {
        StringBuilder requestLog = new StringBuilder("➡️ [ Request Detail ]\n");

        // URL
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }
        requestLog.append("\tURL:\n")
            .append("\t\t[").append(request.getMethod()).append("] ").append(url).append("\n");

        // Headers
        requestLog.append("\tHeaders:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (allowedHeaders.contains(name.toLowerCase())) {
                String value = request.getHeader(name);
                requestLog.append("\t\t").append(name).append(": ").append(value).append("\n");
            }
        }

        // Body
        String contentType = request.getContentType();
        if (contentType != null &&
            (contentType.contains("application/json") || contentType.contains("application/x-www-form-urlencoded"))) {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                String body = new String(content, StandardCharsets.UTF_8);
                requestLog.append("\tRequest Body:\n")
                    .append("\t\t").append(body).append("\n");
            }
        }

        // Multipart
        if (contentType != null && contentType.contains("multipart/form-data")) {
            requestLog.append("\tMultipart Parts:\n");
            try {
                Collection<Part> parts = request.getParts();
                for (jakarta.servlet.http.Part part : parts) {
                    requestLog.append("\t\t").append(part.getName())
                        .append(" [size: ").append(part.getSize()).append(" bytes]\n");
                }
            } catch (Exception e) {
                requestLog.append("❌ Multipart parsing error: ").append(e.getMessage()).append("\n");
            }
        }

        log.info("\n{}", requestLog);
    }

    private void logResponseDetails(ContentCachingResponseWrapper response) {
        boolean isSuccess = HttpStatusCode.valueOf(response.getStatus()).is2xxSuccessful();
        StringBuilder responseLog = new StringBuilder(isSuccess ? "✅ [ Response Detail ]\n" : "❌ [ Response Detail ]\n");

        responseLog.append("\tResponse Status:\n")
            .append("\t\t").append(HttpStatus.valueOf(response.getStatus()).getReasonPhrase()).append("\n");

        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            responseLog.append("\tResponse Body:\n")
                .append("\t\t").append(body).append("\n");
        }

        if (isSuccess) {
            log.info("\n{}", responseLog);
        } else {
            log.error("\n{}", responseLog);
        }
    }
}
