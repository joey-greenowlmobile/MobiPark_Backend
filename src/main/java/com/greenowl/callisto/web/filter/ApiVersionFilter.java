package com.greenowl.callisto.web.filter;

import com.greenowl.callisto.config.ApiVersion;
import com.greenowl.callisto.util.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters incoming requests and determines which API version is requested by the client.
 * /api/v#/...
 */
public class ApiVersionFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(ApiVersionFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (servletRequest instanceof HttpServletRequest) {
                String url = ((HttpServletRequest) servletRequest).getRequestURI();
                String uri = ((HttpServletRequest) servletRequest).getRequestURI();
                ApiVersion version = extractVersion(uri);
                LOG.debug("HTTP request to url = {}. Setting version # to {}", url, version.getValue());
                ApiUtil.setVersion(version);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private ApiVersion extractVersion(String uri) {
        for (ApiVersion version : ApiVersion.values()) {
            if (uri.contains(version.name())) {
                return version;
            }
        }
        LOG.warn("Unable to find api version from request uri = {}. Using default.", uri);
        return ApiVersion.latest();
    }

}
