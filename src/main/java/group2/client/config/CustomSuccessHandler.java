/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.config;

import group2.client.service.JwtService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author hokim
 */
@Configuration
public class CustomSuccessHandler implements AuthenticationSuccessHandler{
    
     @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ADMIN") || roles.contains("DOCTOR") || roles.contains("CASHER")) {
            String user_roles = roles.toString().substring(1, roles.toString().length() - 1);
            String token = jwtService.generateToken(authentication.getName(), user_roles);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setMaxAge(3600); // Thời gian sống của cookie (tính bằng giây)
            cookie.setPath("/"); // Đường dẫn áp dụng cookie (có thể là "/" để áp dụng cho tất cả các đường dẫn)
            response.addCookie(cookie);
            response.sendRedirect("/admin");
        } else if(roles.contains("PATIENT")){
            String user_roles = roles.toString().substring(1, roles.toString().length() - 1);
            String token = jwtService.generateToken(authentication.getName(), user_roles);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setMaxAge(3600); // Thời gian sống của cookie (tính bằng giây)
            cookie.setPath("/"); // Đường dẫn áp dụng cookie (có thể là "/" để áp dụng cho tất cả các đường dẫn)
            response.addCookie(cookie);
            response.sendRedirect("/");

        }

    }

}

