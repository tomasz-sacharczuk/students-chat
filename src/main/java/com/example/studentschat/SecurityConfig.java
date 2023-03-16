package com.example.studentschat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.studentschat.component.DaoAuthenticationProvider;
import com.example.studentschat.service.impl.JpaUserDetailsService;

@Configuration
@EnableWebSecurity(debug=false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	JpaUserDetailsService userDetailsService;
	
	@Autowired
	DaoAuthenticationProvider daoAuthenticationProvider;
	
	@Autowired
	public SecurityConfig(JpaUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(daoAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/h2_console/**").permitAll()
				.antMatchers("/ws").permitAll()
				.antMatchers("/app").permitAll()
				.antMatchers("/topic/**").permitAll()
				.antMatchers("/chat.sendMessage").permitAll()
				.antMatchers("/chat").permitAll()
				.antMatchers("/chat.addUser").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/js/**").permitAll()
			.antMatchers("/admin_panel").hasAuthority("ADMIN")
				.antMatchers("/user_group").hasAnyAuthority("ADMIN","USER")
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.usernameParameter("username")
			.passwordParameter("password")
			.defaultSuccessUrl("/user_panel", true)
			.and()
		.logout()
			.logoutUrl("/user_logout")
			.logoutSuccessUrl("/login?logout")
			.deleteCookies("cookies")
				.and()
				.csrf().disable();
		http.headers().frameOptions().disable();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
