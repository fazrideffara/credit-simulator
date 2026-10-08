package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.LoanRequest;
import com.bcadigital.creditsimulator.util.JsonUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MockyApiClient {

    private static final String DEFAULT_ENDPOINT = "https://run.mocky.io/v3/9108b1da-beec-409e-ae14-e8091955666c";
    private static final String SAMPLE_JSON = """
            {
              "vehicleType": "Mobil",
              "vehicleCondition": "Baru",
              "vehicleYear": 2025,
              "totalLoanAmount": 1000000000,
              "loanTenure": 6,
              "downPayment": 500000000
            }
            """;

    private final HttpClient httpClient;
    private final String endpointUrl;

    public MockyApiClient() {
        this(DEFAULT_ENDPOINT);
    }

    public MockyApiClient(String endpointUrl) {
        this.endpointUrl = endpointUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public LoanRequest fetchExistingCalculation() {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .header("Accept", "application/json")
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new IllegalStateException("gagal menghubungi web service");
        }

        if (response.statusCode() != 200 || response.body() == null || response.body().isBlank()) {
            throw new IllegalStateException("HTTP " + response.statusCode());
        }
        return JsonUtils.parseLoanRequest(response.body());
    }

    public LoanRequest sampleRequest() {
        return JsonUtils.parseLoanRequest(SAMPLE_JSON);
    }
}
