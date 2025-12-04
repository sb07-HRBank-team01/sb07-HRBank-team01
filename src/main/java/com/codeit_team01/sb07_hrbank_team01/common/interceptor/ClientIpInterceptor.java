package com.codeit_team01.sb07_hrbank_team01.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class ClientIpInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception {
    String clientIp = extractClientIp(request);
    log.info("IP CATCH -> Client IP: {}", clientIp);

    // 컨트롤러에서 사용할 수 있도록 request attribute에 저장
    request.setAttribute("clientIp", clientIp);

    return true; // true면 다음 인터셉터/컨트롤러로 진행
  }

  private String extractClientIp(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
        .filter(h -> !h.isBlank() && !"unknown".equalsIgnoreCase(h))
        .map(h -> h.split(",")[0].trim())
        .orElse(request.getRemoteAddr());
  }
}