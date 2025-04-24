package com.springSecurityClient.controller;

import com.springSecurityClient.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;


@RestController
public class ProductController {

    @Autowired
    private WebClient webClient;

    @GetMapping("/api/users")
    public String[] users(
            @RegisteredOAuth2AuthorizedClient("user-service-authorization-code")OAuth2AuthorizedClient auth2AuthorizedClient
    ){
        System.out.println(auth2AuthorizedClient.getAccessToken().getTokenValue());
        System.out.println(auth2AuthorizedClient.getRefreshToken());

        return this.webClient
                .get()
                .uri("http://127.0.0.1:8090/api/users")
                .attributes(oauth2AuthorizedClient(auth2AuthorizedClient))
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    @GetMapping("/api/products")
    public List<ProductResponse> getAll(
            @RegisteredOAuth2AuthorizedClient("user-service-authorization-code")OAuth2AuthorizedClient auth2AuthorizedClient
    ){
        System.out.println(auth2AuthorizedClient.getAccessToken().getTokenValue());
        System.out.println(auth2AuthorizedClient.getRefreshToken());

        return  this.webClient
                .get()
                .uri("http://127.0.0.1:8090/api/products")
                .attributes(oauth2AuthorizedClient(auth2AuthorizedClient))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {})
                .block();
    }

    @GetMapping("/api/product/{id}")
    private ProductResponse getSingleProduct(
            @RegisteredOAuth2AuthorizedClient("user-service-authorization-code")OAuth2AuthorizedClient authorizedClient,
            @PathVariable("id") String id )
    {
        return this.webClient
                .get()
                .uri("http://127.0.0.1:8090/api/product/{id}", id)
                .attributes(oauth2AuthorizedClient((authorizedClient)))
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();
    }
}
