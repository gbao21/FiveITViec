package com.DATN.FiveITViec.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class FiveItViecSecurity {

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;



    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/provinces").permitAll()
                .requestMatchers("/api/jobs").permitAll()
                .requestMatchers("/api/company/**").permitAll()
                .requestMatchers("/api/reviewAndRatings/**").permitAll()
                .requestMatchers("/api/auth/profile").authenticated()
                .requestMatchers("/api/auth/loadJobApply").authenticated()
                .requestMatchers("/api/auth/applyJob").authenticated()
                .requestMatchers("/api/auth/actionFavoriteJob").authenticated()
                .requestMatchers("/api/auth/employer/**").authenticated()
                .requestMatchers("/api/auth/admin/**").authenticated()
                .requestMatchers("/api/auth/profile/**").authenticated()
                .requestMatchers("/api/contacts/**").authenticated()
                .requestMatchers("/contact").permitAll()
//                .requestMatchers("/api/profileEmployees/**").authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
    }
}
