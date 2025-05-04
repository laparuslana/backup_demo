package com.example.mainapp.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.mainapp.Model.UserManagement.MyAppUserService;
import lombok.AllArgsConstructor;
import java.util.Set;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private final MyAppUserService appUserService;
    
    
    @Bean
    public UserDetailsService userDetailsService(){
        return appUserService;
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(httpForm -> httpForm
                            .loginPage("/req/login").permitAll()
        .successHandler((request, response, authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/user");
            }
        })
                        )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/req/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

            .authorizeHttpRequests(registry -> registry
                    .requestMatchers("/req/signup","/css/**","/js/**").permitAll()
                    .requestMatchers("/admin/**", "/js/**").hasRole("ADMIN")
                    .requestMatchers("/api/**", "/js/**").permitAll()
                    .requestMatchers("/req/history/**","/js/**").permitAll()
                    .requestMatchers("/history","/css/**","/js/**").permitAll()
                    .anyRequest().authenticated()
            )
            .build();
    }
    
}
