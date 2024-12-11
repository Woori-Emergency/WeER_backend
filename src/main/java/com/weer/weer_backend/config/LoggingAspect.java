package com.weer.weer_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  private final HttpServletRequest httpServletRequest;

  public LoggingAspect(HttpServletRequest httpServletRequest) {
    this.httpServletRequest = httpServletRequest;
  }

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void controllerPointcut() {
    // Pointcut for all RestControllers
  }

  @Around("controllerPointcut()")
  public Object logControllerRequests(ProceedingJoinPoint joinPoint) throws Throwable {
    // Gather request details
    String requestUrl = httpServletRequest.getRequestURL().toString();
    String httpMethod = httpServletRequest.getMethod();
    String queryParams = httpServletRequest.getQueryString();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    String arguments = Arrays.toString(joinPoint.getArgs());

    long startTime = System.currentTimeMillis();
    Object result;
    try {
      result = joinPoint.proceed(); // Execute the method
    } catch (Throwable throwable) {
      logger.error("Exception during AOP logging", throwable);
      throw throwable;
    }
    long executionTimeMs = System.currentTimeMillis() - startTime;

    // Log the details
    Map<String, Object> logDetails = new HashMap<>();
    logDetails.put("requestUrl", requestUrl);
    logDetails.put("httpMethod", httpMethod);
    logDetails.put("queryParams", queryParams);
    logDetails.put("className", className);
    logDetails.put("methodName", methodName);
    logDetails.put("arguments", arguments);
    logDetails.put("executionTimeMs", executionTimeMs);
    logDetails.put("response", result);

    logger.info("Controller Request Log: {}", logDetails);

    return result;
  }
}
