package microservice.grade_service.Config;

import jakarta.servlet.http.HttpServletResponse;
import microservice.common_classes.JWT.JWTSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JWTSecurity jwtSecurity;

    @Autowired
    public SecurityConfig(JWTSecurity jwtSecurity) {
        this.jwtSecurity = jwtSecurity;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("v1/api/grades/teachers/groups/**").hasAnyRole("ADMIN", "TEACHER")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtSecurity, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic ->
                        httpBasic
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.getWriter().write("Unauthorized");
                                })
                );

        return http.build();
    }
}