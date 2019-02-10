package pl.lapme.adoption.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.lapme.adoption.components.UserDaoAuthenticationProvider;
import pl.lapme.adoption.service.LoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/admin/**").hasRole("ADMIN")
                .antMatchers(
                        "/",
                        "/login",
                        "/i18n/**",
                        "/register",
                        "/webjars/**",
                        "/locale",
                        "/img/**",
                        "/css/**").permitAll()
                .anyRequest()
                .authenticated()
                .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and().logout()
                .clearAuthentication(true)
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true).permitAll();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        UserDaoAuthenticationProvider daoAuthenticationProvider = new UserDaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(loginService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
}