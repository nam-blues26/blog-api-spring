package com.blog.configs;

import com.blog.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebMvc
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(r ->{
                    r.requestMatchers(
                                    String.format("%s/user/register", apiPrefix),
                                    String.format("%s/user/login", apiPrefix),
//                            String.format("%s/post", apiPrefix),
                                    "/api-docs",
                                    "/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/webjars/swagger-ui/**",
                                    "/swagger-ui/index.html",
                                    "/swagger-ui/index.html#/**",
                                    "/kafka/publish"

                            ).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/category/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/post/**", apiPrefix)).permitAll()
//                            .requestMatchers(POST,
//                                    String.format("%s/post/**", apiPrefix)).permitAll()
                            .requestMatchers(GET,
                                    String.format("/swagger-ui.html")).permitAll()
                            .anyRequest()
                            .authenticated();
                })
        ;

        return http.build();
    }
}
