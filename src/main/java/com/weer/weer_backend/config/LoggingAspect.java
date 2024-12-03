package com.weer.weer_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();

    logger.info("Request URL: {}", httpServletRequest.getRequestURL());
    logger.info("HTTP Method: {}", httpServletRequest.getMethod());
    logger.info("Request Params: {}", httpServletRequest.getQueryString());
    logger.info("Executing {}.{}() with arguments: {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long endTime = System.currentTimeMillis();

    logger.info("Completed {}.{}() in {} ms with result: {}", className, methodName, endTime - startTime, result);

    return result;
  }
}
