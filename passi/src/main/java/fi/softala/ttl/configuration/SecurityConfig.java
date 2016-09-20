/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username=?")
				.authoritiesByUsernameQuery("SELECT user.username, role.role_name FROM user JOIN role ON user.role_id = role.role_id WHERE user.username = ?");
	}

	/*
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder
	 * authenticationMgr) throws Exception {
	 * authenticationMgr.inMemoryAuthentication().withUser("Donald").password(
	 * "Trump").authorities("ROLE_USER"); }
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// character encoding filter
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
		
		http.csrf().csrfTokenRepository(csrfTokenRepository());
		
		http.authorizeRequests().anyRequest().authenticated()
				.antMatchers("/login", "/expired").permitAll()
				.antMatchers("/index", "/nav**").hasRole("ADMIN").anyRequest().permitAll()
				.and()
				.formLogin().loginPage("/login").permitAll()
					.defaultSuccessUrl("/init")
					.failureUrl("/login?error")
					.usernameParameter("username")
					.passwordParameter("password")
					.and()
					.logout().logoutSuccessUrl("/login?logout")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
					.and()
					.sessionManagement()
					.maximumSessions(1)
					.expiredUrl("/error");
					// .sessionAuthenticationErrorUrl("/error")	
	}
	
	private CsrfTokenRepository csrfTokenRepository() { 
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
	    repository.setSessionAttributeName("_csrf");
	    return repository; 
	}
}