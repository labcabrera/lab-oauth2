package org.lab.oauth.ui.zuul;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OauthLoginPreZuulFilter extends ZuulFilter {

	@Override
	public Object run() {
		final RequestContext ctx = RequestContext.getCurrentContext();
		log.info("in zuul filter " + ctx.getRequest().getRequestURI());
		byte[] encoded;
		try {
			encoded = Base64.encode("fooClientIdPassword:secret".getBytes("UTF-8"));
			ctx.addZuulRequestHeader("Authorization", "Basic " + new String(encoded));
			log.info(ctx.getRequest().getHeader("Authorization"));
			final HttpServletRequest req = ctx.getRequest();
			final String refreshToken = extractRefreshToken(req);
			if (refreshToken != null) {
				final Map<String, String[]> param = new HashMap<String, String[]>();
				param.put("refresh_token", new String[] { refreshToken });
				param.put("grant_type", new String[] { "refresh_token" });

				ctx.setRequest(new CustomHttpServletRequest(req, param));
			}
		} catch (final UnsupportedEncodingException e) {
			log.error("Error occured in pre filter", e);
		}
		return null;
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

	@Override
	public boolean shouldFilter() {
		// TODO
		final RequestContext ctx = RequestContext.getCurrentContext();
		String requestURI = ctx.getRequest().getRequestURI();
		if ("/oauth/token".equals(requestURI)) {
			return true;
		}
		return false;
	}

	@Override
	public int filterOrder() {
		return -2;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}
