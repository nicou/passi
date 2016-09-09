package fi.softala.ttl.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
		authenticationMgr.inMemoryAuthentication().withUser("Donald").password("Trump").authorities("ROLE_USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// character encoding filter
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
		
		http
		.authorizeRequests()
		.anyRequest().authenticated()
		.antMatchers("/index").hasRole("ADMIN").anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/login").permitAll()
		.defaultSuccessUrl("/index")
		.failureUrl("/login?error")
		.usernameParameter("username")
		.passwordParameter("password")
		.and()
		.logout()
		.logoutSuccessUrl("/login?logout")
		.invalidateHttpSession(false)
		.deleteCookies("remove");
		
		http
		.sessionManagement()
		.sessionAuthenticationErrorUrl("/login")
		.invalidSessionUrl("/login");
		
		/* old config
		http
		.authorizeRequests().antMatchers("/index").access("hasRole('ROLE_USER')").and().formLogin()
		.loginPage("/login").defaultSuccessUrl("/index").failureUrl("/login?error")
		.usernameParameter("username").passwordParameter("password").and().logout()
		.logoutSuccessUrl("/login?logout");
		*/

	}
}