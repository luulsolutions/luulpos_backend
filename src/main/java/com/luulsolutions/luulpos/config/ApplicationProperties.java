package com.luulsolutions.luulpos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.luulsolutions.luulpos.utils.Constants;

/**
 * Properties specific to Luulpos.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
	private final AuthServer authServer = new AuthServer();
	public static class AuthServer {

        private String registerUrl = Constants.AUTH_SERVER_DEFAULT_REGISTER_URL;


        public String getRegisterUrl() {
            return registerUrl;
        }

        public void setRegisterUrl(String registerUrl) {
            this.registerUrl = registerUrl;
        }
    }
	public AuthServer getAuthServer() {
		return authServer;
	}

}
