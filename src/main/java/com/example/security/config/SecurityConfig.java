
package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.security.service.CustomUserDetailsServiceImpl;

@Configuration

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService;

	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * 
	 * http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/login").
	 * hasAnyRole("ADMIN", "ANALYST") .antMatchers("/user/me").hasAnyRole("ADMIN",
	 * "ANALYST").antMatchers("/hello") .hasAnyRole("ADMIN",
	 * "ANALYST").anyRequest().authenticated().and().formLogin().loginPage("/login")
	 * .permitAll().and().logout().permitAll();
	 * 
	 * http.csrf().disable(); }
	 */
	
	@Override
    protected void configure(HttpSecurity httpSecurity)
      throws Exception {
        httpSecurity.authorizeRequests()
          .antMatchers("/h2-console/**")
          .permitAll()
          .anyRequest()
          .authenticated()
          .and()
          .formLogin();
         
        httpSecurity.csrf()
          .ignoringAntMatchers("/h2-console/**");
        httpSecurity.headers()
          .frameOptions()
          .sameOrigin();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

}
