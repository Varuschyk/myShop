package com.alexvar.springboot_rest.configuration.security;

import com.alexvar.springboot_rest.model.Permission;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.Principal;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()

                .antMatchers("/auth/login", "/auth/register").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginPage("/auth/login").permitAll()
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/home", true)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/auth/login");


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
