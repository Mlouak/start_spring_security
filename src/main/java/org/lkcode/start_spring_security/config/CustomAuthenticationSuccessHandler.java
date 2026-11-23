package org.lkcode.start_spring_security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = request.getContextPath();

        if (authentication.getAuthorities().stream().anyMatch(a ->
        {
            String authority = a.getAuthority();
            assert authority != null;
            return authority.equals("ROLE_ADMIN");
        })) {
            redirectUrl = "/admin/home";
        } else if (authentication.getAuthorities().stream().anyMatch(a ->
        {
            String authority = a.getAuthority();
            assert authority != null;
            return authority.equals("ROLE_USER");
        })) {
            redirectUrl = "/user/home";
        }
        response.sendRedirect(redirectUrl);
    }
}
