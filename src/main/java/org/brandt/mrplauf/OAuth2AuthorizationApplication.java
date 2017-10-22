package org.brandt.mrplauf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableResourceServer
@EnableAuthorizationServer
public class OAuth2AuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2AuthorizationApplication.class, args);
	}
	
	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private UserDetailsService userDetailsService;
 
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.userDetailsService(userDetailsService);			
			//JWT Configuration
		    endpoints.tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter()).authenticationManager(authenticationManager);
		    
			endpoints.authenticationManager(authenticationManager);
		}
 
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory().withClient("foo").secret("foosecret")
					.authorizedGrantTypes("authorization_code", "refresh_token", "password").scopes("openid");
		}
		
	
		   @Bean
		    public TokenStore tokenStore() {
		        return new JwtTokenStore(accessTokenConverter());
		    }
		 
		    @Bean
		    public JwtAccessTokenConverter accessTokenConverter() {
		        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		        converter.setSigningKey("123");
		        return converter;
		    }
		 
		    @Bean
		    @Primary
		    public DefaultTokenServices tokenServices() {
		        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		        defaultTokenServices.setTokenStore(tokenStore());
		        defaultTokenServices.setSupportRefreshToken(true);
		        return defaultTokenServices;
		    }
		  
	}
 
	@RequestMapping("/user")
	public String user(HttpServletRequest res) {
		return res.getHeader("Authorization");
	}	
	
}
