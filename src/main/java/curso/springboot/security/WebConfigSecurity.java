package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override // Configurar as solicitações de acesso por http
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // Desativa as configurações padrão de memória
		.authorizeRequests() //Permitir restringer acessos
		.antMatchers(HttpMethod.GET, "/").permitAll()
		.antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN")//Qualquer usuário acessa a página inicial
		.anyRequest().authenticated().and().formLogin().permitAll()// permite qualquer usuario
		.loginPage("/login")
		.defaultSuccessUrl("/cadastropessoa")
		.failureUrl("/login?error=true")
		.and().logout().logoutSuccessUrl("/login") // mapeia url de sair do sistema e invalida o usuario autenticado
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Override //Cria autenticação do usuário com Banco de dados ou em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		
		auth.userDetailsService(implementacaoUserDetailsService).
		passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	@Override // ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
}
