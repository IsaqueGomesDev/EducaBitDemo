package com.educabit.educabit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        CustomAuthenticationSuccessHandler successHandler) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(auth -> auth
                                                // 1. Usuário Anônimo (Público) and Static Assets
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/educabit/usuario")
                                                .permitAll()
                                                .requestMatchers("/api/public/**", "/educabit/usuario")
                                                .permitAll()
                                                .requestMatchers("/css/**", "/js/**", "/img/**", "/uploads/**")
                                                .permitAll()
                                                .requestMatchers("/*.css", "/*.js", "/*.png", "/*.jpg", "/*.jpeg",
                                                                "/*.html")
                                                .permitAll()
                                                .requestMatchers("/cadastrar_user.html", "/login.html",
                                                                "/selecao_login.html", "/login_user.html",
                                                                "/login_adm.html")
                                                .permitAll()

                                                // Página inicial acessível a todos
                                                .requestMatchers("/", "/tela_inicial.html")
                                                .permitAll()

                                                // Conteúdo público acessível sem login
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/educabit/conteudo/publico")
                                                .permitAll()

                                                // 2. Usuário Simples (Logado)
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/educabit/conteudo/**")
                                                .hasAnyRole("USER", "CREATOR", "ADMIN")

                                                // 3. Usuário Criador (Escrever)
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/educabit/conteudo/**")
                                                .hasAnyRole("CREATOR", "ADMIN")

                                                // 4. Administrador (Aprovação e Gestão)
                                                // 5. Solicitação de Criador
                                                .requestMatchers("/educabit/criador/**").authenticated()

                                                .requestMatchers("/api/admin/**", "/adm/**").hasRole("ADMIN")

                                                // Qualquer outra coisa exige autenticação
                                                .anyRequest().authenticated())
                                .formLogin((form) -> form
                                                .loginPage("/selecao_login.html")
                                                .loginProcessingUrl("/perform_login")
                                                .successHandler(successHandler)
                                                .permitAll())
                                .logout((logout) -> logout.permitAll())
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        // Fix #15: CORS configurado no SecurityConfig (onde deve estar) em vez de WebMvcConfigurer separado
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        // WARNING: Use BCrypt in production. Using NoOp for compatibility with
        // potential plaintext legacy passwords.
        @SuppressWarnings("deprecation")
        @Bean
        public PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
        }
}
