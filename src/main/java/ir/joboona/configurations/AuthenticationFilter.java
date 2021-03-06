package ir.joboona.configurations;

import io.jsonwebtoken.MalformedJwtException;
import ir.joboona.Models.User;
import ir.joboona.Services.AuthenticationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@WebFilter(filterName="AuthenticationFilter")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getRequestURI().equals("/jobonja/user") && req.getMethod().equals("POST") ||
                req.getRequestURI().equals("/jobonja/user/login"))
            chain.doFilter(request, response);
        else {

            String token = req.getHeader("Authorization");

            if (isEmpty(token)) {
                res.setStatus(401);
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write("{\"message\":\"توکن احراز هویت ارسال نشده است.\"}");
                return;
            }
            Optional<User> user = Optional.empty();
            try {
                user = AuthenticationService.getInstance().getPrincipal(token);
            }catch (MalformedJwtException e1){
                invalidToken(res);
                return;
            }catch (Exception e) {
                e.printStackTrace();
            }

            if (!user.isPresent()){
                invalidToken(res);
            }else {
                req.setAttribute("principal", user.get());
                chain.doFilter(request, response);
            }

        }
    }

    private void invalidToken(HttpServletResponse res) throws IOException {
        res.setStatus(403);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"message\":\"توکن احراز هویت نامعتبر است\"}");
    }
}
