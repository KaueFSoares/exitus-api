package br.exitus.api.infra.security;

import br.exitus.api.constant.message.AuthMessages;
import br.exitus.api.constant.variable.RouteVAR;
import br.exitus.api.infra.exception.AuthException;
import br.exitus.api.infra.filter.ExceptionHandlerFilter;
import br.exitus.api.infra.filter.PerformanceFilter;
import br.exitus.api.infra.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final PerformanceFilter performanceFilter;

    @Autowired
    public SecurityConfig(SecurityFilter securityFilter, ExceptionHandlerFilter exceptionHandlerFilter, PerformanceFilter performanceFilter) {
        this.securityFilter = securityFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
        this.performanceFilter = performanceFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(RouteVAR.FULL_LOGIN).permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, SecurityFilter.class)
                .addFilterBefore(performanceFilter, ExceptionHandlerFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        try {
            return authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new AuthException(AuthMessages.AUTHENTICATION_MANAGER_ERROR);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
