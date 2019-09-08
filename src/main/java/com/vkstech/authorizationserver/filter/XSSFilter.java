package com.vkstech.authorizationserver.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class XSSFilter implements Filter {
    private Log LOGGER = LogFactory.getLog(XSSFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {

            if ((request.getContentType() != null) && (request.getContentType().toLowerCase().indexOf("multipart/form-data") == 0)) {

                chain.doFilter(request, response);

            } else {

                MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest((HttpServletRequest) request);

                HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

                chain.doFilter(requestWrapper, responseCopier);

                responseCopier.flushBuffer();

                byte[] copy = responseCopier.getCopy();
                LOGGER.info("response_body: " + new String(copy, response.getCharacterEncoding()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) {

    }
}
