package org.lab.oauth2;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleFlowClient {

	public static void main(String[] args) throws Exception {

		String url = "http://localhost:9999/uaa/oauth/token";
		// byte[] credentials = "user:password".getBytes();
		byte[] credentials = "acme:acmesecret".getBytes();

		String base64Creds = new String(Base64.encodeBase64String(credentials));

		MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
		bodyMap.add("grant_type", "client_credentials");

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Accept", "application/json");

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters()
				.add(new MappingJackson2HttpMessageConverter());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyMap,
				headers);

		HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
				String.class);

		String responseBody = response.getBody();
		HttpHeaders responseHeaders = response.getHeaders();

		System.out.println(response);
		System.out.println(responseBody);
		System.out.println(responseHeaders);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(responseBody);
		String token = jsonNode.get("access_token").asText();
		String tokenType = jsonNode.get("token_type").asText();
		Long expires = jsonNode.get("expires_in").asLong();
		String scope = jsonNode.get("scope").asText();

		Assert.notNull(token, "Missing token");
		Assert.notNull(tokenType, "Missing token type");
		Assert.notNull(expires, "Missing expiration");
		Assert.notNull(scope, "Missing scope");


	}
}
