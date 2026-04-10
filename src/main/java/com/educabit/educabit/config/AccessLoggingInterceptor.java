package com.educabit.educabit.config;

import com.educabit.educabit.model.AccessLog;
import com.educabit.educabit.repositories.AccessLogRepository;
import com.educabit.educabit.services.GeoLocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class AccessLoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private GeoLocationService geoLocationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        AccessLog log = new AccessLog();
        log.setIpAddress(ip);
        log.setRequestUrl(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setReferer(request.getHeader("Referer"));
        log.setTimestamp(LocalDateTime.now());
        
        try {
            log.setLocation(geoLocationService.getUserLocation(ip));
        } catch (Exception e) {
            log.setLocation("Unknown");
        }
        
        accessLogRepository.save(log);
        return true;
    }
}
