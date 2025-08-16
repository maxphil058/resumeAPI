package com.resumeAI.resumeAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class LatexWebClientConfig {

	@Bean("latexWebClient")
	public WebClient latexWebClient() {
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("https://latexonline.cc");
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

		return WebClient.builder().uriBuilderFactory(factory).baseUrl("https://latexonline.cc").build();
	}

}
