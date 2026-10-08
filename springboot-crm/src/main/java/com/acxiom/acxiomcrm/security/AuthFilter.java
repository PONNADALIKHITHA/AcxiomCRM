package com.acxiom.acxiomcrm.security;

import com.acxiom.acxiomcrm.model.User;
import com.acxiom.acxiomcrm.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {
    public static final String USER_ATTR = "CRM_USER";
    private final TokenStore tokens;
    private final UserRepository users;

    public AuthFilter(TokenStore tokens, UserRepository users) {
        this.tokens = tokens; this.users = users;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        if (path.equals("/api/auth/login") || path.equals("/api/health")) {
            chain.doFilter(request,response); return;
        }

        String h = req.getHeader("Authorization");
        if (h == null || !h.startsWith("Bearer ")) {
            ((HttpServletResponse)response).sendError(401, "Authentication required");
            return;
        }

        String username = tokens.username(h.substring(7));
        if (username == null) {
            ((HttpServletResponse)response).sendError(401, "Invalid token");
            return;
        }
        User user = users.findByUsername(username).orElse(null);
        if (user == null || !user.active) {
            ((HttpServletResponse)response).sendError(403, "Inactive user");
            return;
        }
        req.setAttribute(USER_ATTR, user);
        chain.doFilter(request,response);
    }
}
