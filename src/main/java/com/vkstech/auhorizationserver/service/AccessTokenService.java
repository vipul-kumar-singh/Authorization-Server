package com.vkstech.auhorizationserver.service;

import com.vkstech.auhorizationserver.constants.ApplicationConstants;
import com.vkstech.auhorizationserver.constants.ResponseMessages;
import com.vkstech.auhorizationserver.model.OauthClientDetails;
import com.vkstech.auhorizationserver.model.User;
import com.vkstech.auhorizationserver.repository.OauthClientDetailsRepository;
import com.vkstech.auhorizationserver.utils.ResponseObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AccessTokenService {

    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;

    @Autowired
    private OauthClientDetailsRepository clientDetailsRepository;

    @Autowired
    private TokenStore jdbcTokenStore;

    private final Logger LOGGER = LoggerFactory.getLogger(AccessTokenService.class);

    public OAuth2AccessToken getAccessToken(User user, HttpServletRequest request) {
        LOGGER.info("AccessTokenService getAccessToken..");

        //getting client id from basic token
        String basicToken = getTokenFromRequest(request);
        String[] client = basicToken.split(":");

        OauthClientDetails clientDetails = clientDetailsRepository.findByClientId(client[0]);

        Map<String, String> requestParameters = new HashMap<>();
        Map<String, Serializable> extensionProperties = new HashMap<>();

        //approval
        boolean approved = true;

        //response types
        Set<String> responseTypes = new HashSet<>();
        responseTypes.add(ApplicationConstants.OAUTH_RESPONSE_TYPE);

        // Authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        });


        //Scopes
        Set<String> scope = new HashSet<>(Arrays.asList(clientDetails.getScope().split(",")));

        //Resource Ids
        Set<String> resourceIds = new HashSet<>(Arrays.asList(clientDetails.getResourceIds().split(",")));

        OAuth2Request oauth2Request = new OAuth2Request(requestParameters, clientDetails.getClientId(), authorities, approved, scope, resourceIds, clientDetails.getWebServerRedirectUri(), responseTypes, extensionProperties);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), "N/A", authorities);

        OAuth2Authentication auth = new OAuth2Authentication(oauth2Request, authenticationToken);

        AuthorizationServerTokenServices tokenService = configuration.getEndpointsConfigurer().getTokenServices();

        //returns existing token if not expired, else creates a new token
        OAuth2AccessToken token = tokenService.createAccessToken(auth);

        return token;
    }

    public boolean removeAccessToken(HttpServletRequest request) {
        LOGGER.info("AccessTokenService removeAccessToken..");

        //extracting bearer token from header
        String tokenValue = getTokenFromRequest(request);
        if (tokenValue == null) {
            return false;
        }

        //fetching token from database
        OAuth2AccessToken accessToken = jdbcTokenStore.readAccessToken(tokenValue);
        if (accessToken == null) {
            LOGGER.error(ResponseMessages.INVALID_TOKEN);
            return false;
        }

        //Logout - removing access token from database
        try {
            jdbcTokenStore.removeAccessToken(accessToken);
            LOGGER.info(ResponseMessages.LOGOUT_SUCCESS);
            return true;
        } catch (Exception e) {
            LOGGER.error(ResponseMessages.LOGOUT_FAILURE + " = {}", e.getMessage());
            return false;
        }

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        LOGGER.info("AccessTokenService getTokenFromRequest..");

        String header = request.getHeader(ApplicationConstants.HEADER_AUTHORIZATION);

        if (header == null)
            return null;

        try {

            //Split header into key and value
            String[] strArr = header.split(" ");

            //if token is basic token, return decoded token value
            if (strArr[0].equalsIgnoreCase(ApplicationConstants.BASIC_AUTHENTICATION))
                return new String(Base64.decodeBase64(strArr[1].getBytes(StandardCharsets.UTF_8)));

            //if token is bearer, return token value
            if (strArr[0].equalsIgnoreCase(ApplicationConstants.BEARER_AUTHENTICATION))
                return strArr[1];

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        return null;
    }
}
