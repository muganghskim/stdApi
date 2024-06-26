package com.stdApi.pacificOcean.config;

import com.stdApi.pacificOcean.service.CustomOAuth2UserService;
import com.stdApi.pacificOcean.util.JwtAuthenticationEntryPoint;
import com.stdApi.pacificOcean.util.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private JwtConfiguration jwtConfig;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(UserDetailsService userDetailsService, JwtConfiguration jwtConfig,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, CustomOAuth2UserService customOAuth2UserService) {
        this.userDetailsService = userDetailsService;
        this.jwtConfig = jwtConfig;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customOAuth2UserService = customOAuth2UserService;
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), jwtConfig, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilter(corsFilter())
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN") // 관리자 권한이 있는 도메인만 접근을 허용합니다.
                .antMatchers("/admin/**").hasRole("ADMIN") // 관리자 권한이 있는 도메인만 접근을 허용합니다.
                .antMatchers("/","/loginSuccess","/api/loginSuccess","/api/products/**","/api/login","/api/signup","/api/logout","/api/notice/all","/api/send-verification-email", "/api/verify-email", "/api/cart/**", "/api/delivery/**", "/api/profile/**", "/api/update-token", "/api/orderItem/**", "/api/transaction/**", "/api/support/**").permitAll()
                .antMatchers("/error", "/error/**").permitAll()  // /error 및 하위 경로에 대한 접근 허용
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .oauth2Login()
                    .defaultSuccessUrl("http://3.34.188.252/back/api/loginSuccess")  // 로그인 성공 후 리다이렉트할 URL
                    .failureUrl("/loginFailure")         // 로그인 실패 후 리다이렉트할 URL
                    .userInfoEndpoint()                  // 사용자 정보를 가져올 때의 설정들을 담당합니다.
                    .userService(customOAuth2UserService);  // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록
    }
}
