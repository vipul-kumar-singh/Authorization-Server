package com.vkstech.authorizationserver;

import com.vkstech.authorizationserver.repository.OauthClientDetailsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizationServerApplicationTests {

    @Autowired
    OauthClientDetailsRepository clientDetailsRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void clientRepoTest(){
        System.out.println(clientDetailsRepository.findByClientId("mobile"));
    }

}
