package org.lab.oauth.resource.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import javax.annotation.Resource;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.*;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.Cookie;

@Controller
public class TokenController {

	@Resource(name = "tokenServices")
	ConsumerTokenServices tokenServices;

	@Resource(name = "tokenStore")
	TokenStore tokenStore;

	@RequestMapping(method = RequestMethod.POST, value = "/tokens/revoke/{tokenId:.*}")
	@ResponseBody
	public String revokeToken(@PathVariable String tokenId) {
		tokenServices.revokeToken(tokenId);
		return "\"{token:" + tokenId + "}\"";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tokens")
	@ResponseBody
	public List<String> getTokens() {
		List<String> tokenValues = new ArrayList<String>();
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId("sampleClientId");
		if (tokens != null) {
			for (OAuth2AccessToken token : tokens) {
				tokenValues.add(token.getValue());
			}
		}
		return tokenValues;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokens/revokeRefreshToken/{tokenId:.*}")
	@ResponseBody
	public String revokeRefreshToken(@PathVariable String tokenId) {
		if (tokenStore instanceof JdbcTokenStore) {
			((JdbcTokenStore) tokenStore).removeRefreshToken(tokenId);
		}
		return tokenId;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/oauth/token/revokeRefreshTokenFromCookie")
	@ResponseBody
	public String revokeRefreshToken(HttpServletRequest request, HttpServletResponse response) {
		String tokenId = extractRefreshToken(request);
		if (tokenStore instanceof JdbcTokenStore) {
			((JdbcTokenStore) tokenStore).removeRefreshToken(tokenId);
		}

		Cookie cookie = new Cookie("refreshToken", "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return "\"{token:" + tokenId + "}\"";
	}

	private String extractRefreshToken(HttpServletRequest req) {
		final Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("refreshToken")) {
					return cookies[i].getValue();
				}
			}
		}
		return null;
	}
}