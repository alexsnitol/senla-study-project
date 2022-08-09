package ru.senla.autoservice.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import ru.senla.autoservice.config.security.filter.JwtTokenFilter;

@EnableWebSecurity(debug = true)
@ComponentScan(basePackages = "ru.senla.autoservice")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;


    @Autowired
    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                             JwtTokenFilter jwtTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.cors().and().csrf().disable();

        http
//                .csrf().disable()


                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()


                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()


                .authorizeRequests()


                .antMatchers("/api/auth/**").permitAll()

                .antMatchers(HttpMethod.GET, "/api/garages/nearest-date")
                    .permitAll()
                .antMatchers(HttpMethod.GET, "/api/garages/number-of-free-places-by-date")
                    .permitAll()
                .antMatchers(HttpMethod.POST, "/api/garages/{id}/places")
                    .hasAnyAuthority("ROLE_ADMIN", "ADD_AND_DELETE_FREE_PLACES")
                .antMatchers(HttpMethod.DELETE, "/api/garages/{id}")
                    .hasAnyAuthority("ROLE_ADMIN", "ADD_AND_DELETE_FREE_PLACES")

                .antMatchers(HttpMethod.GET, "/api/masters")
                    .hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers(HttpMethod.GET, "/api/masters/{id}")
                    .hasAnyRole("USER", "ADMIN", "MANAGER")

                .antMatchers(HttpMethod.GET, "/api/orders")
                    .hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers(HttpMethod.GET, "/api/orders/{id}")
                    .hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers("/api/orders/{id}/shift-time-of-completion")
                    .hasAnyAuthority("ROLE_ADMIN", "SHIFT_TIME_OF_COMPLETION")
                .antMatchers(HttpMethod.DELETE, "/api/orders/{id}")
                    .hasAnyAuthority("ROLE_ADMIN", "DELETE_ORDER")
                
                
                .anyRequest().hasAnyRole("ADMIN", "MANAGER");

        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

}
