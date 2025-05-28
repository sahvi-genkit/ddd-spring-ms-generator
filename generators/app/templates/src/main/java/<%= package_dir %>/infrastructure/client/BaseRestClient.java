package <%= base_package_name %>.infrastructure.client;

import <%= base_package_name %>.infrastructure.config.RestClientProperties;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public abstract class BaseRestClient {

    protected final RestTemplate restTemplate;
    protected final RestClientProperties properties;
    protected final String baseUrl;

    protected BaseRestClient(RestTemplate restTemplate, RestClientProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.baseUrl = properties.getBaseUrl();
    }

    protected <T> ResponseEntity<T> get(String endpoint, Class<T> responseType) {
        return get(endpoint, responseType, Collections.emptyMap());
    }

    @Retryable(
        value = {HttpServerErrorException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    protected <T> ResponseEntity<T> get(String endpoint, Class<T> responseType, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        
        String url = buildUrl(endpoint);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
    }

    protected <T, R> ResponseEntity<R> post(String endpoint, T body, Class<R> responseType) {
        return post(endpoint, body, responseType, Collections.emptyMap());
    }

    @Retryable(
        value = {HttpServerErrorException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    protected <T, R> ResponseEntity<R> post(String endpoint, T body, Class<R> responseType, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        headers.forEach(httpHeaders::add);
        
        HttpEntity<T> requestEntity = new HttpEntity<>(body, httpHeaders);
        String url = buildUrl(endpoint);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    protected <T, R> ResponseEntity<R> put(String endpoint, T body, Class<R> responseType) {
        return put(endpoint, body, responseType, Collections.emptyMap());
    }

    @Retryable(
        value = {HttpServerErrorException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    protected <T, R> ResponseEntity<R> put(String endpoint, T body, Class<R> responseType, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        headers.forEach(httpHeaders::add);
        
        HttpEntity<T> requestEntity = new HttpEntity<>(body, httpHeaders);
        String url = buildUrl(endpoint);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
    }

    protected void delete(String endpoint) {
        delete(endpoint, Collections.emptyMap());
    }

    @Retryable(
        value = {HttpServerErrorException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    protected void delete(String endpoint, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);
        
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        String url = buildUrl(endpoint);
        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
    }

    protected String buildUrl(String endpoint) {
        return baseUrl + (endpoint.startsWith("/") ? endpoint : "/" + endpoint);
    }

    protected <T> Optional<T> handleResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    protected void handleError(HttpClientErrorException e) {
        // Log error and potentially throw a custom exception
        throw new RuntimeException("Client error: " + e.getMessage(), e);
    }

    protected void handleError(HttpServerErrorException e) {
        // Log error and potentially throw a custom exception
        throw new RuntimeException("Server error: " + e.getMessage(), e);
    }
} 