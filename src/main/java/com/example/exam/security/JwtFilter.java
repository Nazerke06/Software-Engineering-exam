//package com.example.exam.security;
//
//import com.example.exam.entities.User;
//import com.example.exam.repositories.UserRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserRepository userRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//
//            if (jwtUtil.isTokenValid(token) &&
//                    SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                String username = jwtUtil.extractUsername(token);
//                User user = userRepository.findByUsername(username).orElse(null);
//
//                if (user != null) {
//                    var authorities = user.getRole().stream()
//                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
//                            .collect(Collectors.toList());
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(user, null, authorities);
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                    // Optional: helpful debug log
//                    // logger.info("Authenticated user: {} with roles: {}", username, authorities);
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}