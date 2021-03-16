/**
 * create this file at 上午9:53:22 by renhd.
 */
package com.allinpay.sample.config;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author 任海东 2020年4月7日
 *
 */
@Component
public class TraceFilter extends OncePerRequestFilter {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {
		try {
			String traceId = request.getHeader("x-traceId-header");
			if (StringUtils.isEmpty(traceId)) {
				traceId = UUID.randomUUID().toString();
			}
			MDC.put("traceId", traceId);
			filterChain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}

}
