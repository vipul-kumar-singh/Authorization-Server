package com.vkstech.auhorizationserver.exceptionHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vkstech.auhorizationserver.constants.ResponseMessages;
import com.vkstech.auhorizationserver.utils.ResponseObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ResponseObject response = new ResponseObject(ResponseMessages.INVALID_TOKEN);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType(MappingJackson2JsonView.DEFAULT_CONTENT_TYPE);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}