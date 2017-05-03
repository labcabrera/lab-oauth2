package org.lab.oauth.ui.zuul;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenBearerPreZuulFilter extends ZuulFilter {

	@Override
	public Object run() {
		final RequestContext ctx = RequestContext.getCurrentContext();
		log.info("in zuul filter " + ctx.getRequest().getRequestURI());
		ctx.addZuulRequestHeader("Authorization", ctx.getRequest().getHeader("Authorization"));
		//TODO other request headers here
		return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO
		final RequestContext ctx = RequestContext.getCurrentContext();
		String header = ctx.getRequest().getHeader("Authorization");
		if (header != null && header.startsWith("Bearer")) {
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