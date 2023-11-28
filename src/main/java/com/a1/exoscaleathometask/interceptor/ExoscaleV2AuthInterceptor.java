package com.a1.exoscaleathometask.interceptor;

import com.a1.exoscaleathometask.config.ExoscaleApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.util.ForwardedHeaderUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExoscaleV2AuthInterceptor implements ClientHttpRequestInterceptor {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private final ExoscaleApiProperties apiProperties;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long expirationTs = Instant.now().getEpochSecond() + 10 * 60; // Expiration time set to 10 minutes
        String signature = generateSignature(request, body, expirationTs);
        String signedQueryArgs = getSignedQueryArgs(request);
        String authHeader = String.format("EXO2-HMAC-SHA256 credential=%s%s,expires=%d,signature=%s",
                apiProperties.key(),
                signedQueryArgs.isEmpty() ? "" : ",signed-query-args=" + signedQueryArgs,
                expirationTs,
                signature
        );
        request.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
        return execution.execute(request, body);
    }

    private String generateSignature(HttpRequest request, byte[] body, long expirationTs) throws IOException {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(apiProperties.secret().getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);

            String method = request.getMethod().toString();
            URI uri = request.getURI();
            String path = uri.getPath();
            String queryParams = getSortedQueryParams(uri);

            String message = String.join("\n",
                    method + " " + path,
                    body != null ? new String(body, StandardCharsets.UTF_8) : "",
                    queryParams,
                    "",    //The official website (https://openapi-v2.exoscale.com/) says: "Request header values, concatenated without separator (none at the moment, leave empty)
                    String.valueOf(expirationTs));
            byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IOException("Failed to generate signature", e);
        }
    }

    private String getSortedQueryParams(URI uri) {
        return UriComponentsBuilder.fromUri(uri).build().getQueryParams()
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getKey().compareToIgnoreCase(e2.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.joining());
    }

    private String getSignedQueryArgs(HttpRequest request) {
        return ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders())
                .build()
                .getQueryParams()
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getKey().compareToIgnoreCase(e2.getKey()))
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(";"));
    }
}
