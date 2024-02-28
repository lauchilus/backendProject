package com.gamelist.main.igbd;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.TwitchToken;
import com.gamelist.main.helpers.SecretsStore;

@Configuration
public class IgdbConfiguration {

	private String apiToken;

	private String apiClientId = SecretsStore.API_CLIENT_ID;
	
	private String clientSecret = SecretsStore.CLIENT_SECRET;

	@Bean
	HttpHeaders apiHeaders() {
		
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
		TwitchToken token = tAuth.requestTwitchToken(apiClientId, clientSecret);
		apiToken = token.getAccess_token();
		IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
		wrapper.setCredentials(apiClientId, apiToken);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + apiToken);
		headers.set("Client-ID", apiClientId);
		System.out.println(apiToken);
		return headers;
	}

	
}
