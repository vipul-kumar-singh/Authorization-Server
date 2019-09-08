package com.vkstech.authorizationserver.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    Log LOGGER = LogFactory.getLog(XSSFilter.class);

    private String request_body;

    public MultiReadHttpServletRequest(HttpServletRequest request) {
        super(request);
        request_body = "";

        try {
            BufferedReader bufferedReader = request.getReader();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                request_body += line;
            }

            StringBuffer requestURL = request.getRequestURL();

            String queryString = request.getQueryString();

            if (queryString == null) {
                LOGGER.info("request_url: " + requestURL.toString());
            } else {
                LOGGER.info("request_url: " + requestURL.append('?').append(queryString).toString());
            }

            LOGGER.info("request_body: " + request_body);

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request_body.getBytes())) {
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }

                public int read() {
                    return byteArrayInputStream.read();
                }
            };
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}