package com.mindhub.homebanking.configurations;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity

@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/index.html","/web/about-us.html", "/web/segurity.html" ,"/web/price.html","/web/index.js","/web/style.css","/web/singin1.html", "/web/singin1.css", "/web/app.js", "/web/create-cards.html","/web/style1.css").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts","/api/transactions/filter", "/api/clients/current/transactions/payments ").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers(HttpMethod.GET,"/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers("/web/assets/**", "/web/login.html","/web/login.js","/web/,login.css").permitAll()
                .antMatchers(HttpMethod.PATCH,"/api/clients/current/cards","/api/clients/current/accounts").hasAnyAuthority("CLIENT", "ADMIN")

                .antMatchers("/manager.html","/style.css","/manager.js").hasAuthority("ADMIN")
                .antMatchers("/web/**").hasAnyAuthority("CLIENT","ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");


        http.csrf().disable();



        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

