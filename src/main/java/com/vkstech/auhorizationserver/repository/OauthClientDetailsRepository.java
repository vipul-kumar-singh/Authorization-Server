package com.vkstech.auhorizationserver.repository;

import com.vkstech.auhorizationserver.model.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails, String> {

    OauthClientDetails findByClientId(String clientId);
}
