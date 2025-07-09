package dev.danilscheglov.quasarmedbot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class CrpApiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${crp.api.url}")
    private String crpApiUrl;

    @Value("${quasar.api.url}")
    private String quasarApiUrl;

    public CrpApiService(ObjectMapper objectMapper, RestClient restClient) {
        this.objectMapper = objectMapper;
        this.restClient = restClient;
    }

    public String search(String lastName, String firstName, String middleName, String birthdate) {
        try {
            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(crpApiUrl)
                            .queryParam("lastName", lastName)
                            .queryParam("firstName", firstName)
                            .queryParam("middleName", middleName)
                            .queryParam("birthdate", birthdate)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                return null;
            }

            List<String> crpIds = objectMapper.readValue(response, new TypeReference<>() {});
            if (crpIds.isEmpty()) {
                return null;
            } else if (crpIds.size() > 1) {
                throw new IllegalStateException("Найдено более одного идентификатора: " + crpIds);
            }

            return crpIds.getFirst();
        } catch (Exception e) {
            return null;
        }
    }


    public void sendToQuasar(String crpId, String lastName, String firstName, String middleName, String pressure) {
        try {
            Map<String, String> body = Map.of(
                    "crp_id", crpId != null ? crpId : "",
                    "last_name", lastName,
                    "first_name", firstName,
                    "middle_name", middleName,
                    "pressure", pressure
            );

            restClient.post()
                    .uri(quasarApiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
