package pl.materus.nbp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${nbp-url}")
    @NonNull
    private String webclientUrl = "https://api.nbp.pl";

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient nbpWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(this.getWebclientUrl()).build();
    }

    @NonNull
    public String getWebclientUrl() {
        return webclientUrl;
    }

    public void setWebclientUrl(@NonNull String webclientUrl) {
        this.webclientUrl = webclientUrl;
    }
}
