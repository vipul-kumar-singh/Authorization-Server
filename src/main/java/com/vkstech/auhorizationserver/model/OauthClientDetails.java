package com.vkstech.auhorizationserver.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OauthClientDetails {

    @Id
    private String clientId;

    private String clientSecret;

    private String webServerRedirectUri;

    private String scope;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String resourceIds;

    private String authorizedGrantTypes;

    private String authorities;

    private String additionalInformation;

    private String autoapprove;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public String getScope() {
        return scope;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public String getAuthorities() {
        return authorities;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public String getAutoapprove() {
        return autoapprove;
    }
}
