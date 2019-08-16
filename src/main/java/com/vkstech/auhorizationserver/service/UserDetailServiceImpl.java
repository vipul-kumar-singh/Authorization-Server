package com.vkstech.auhorizationserver.service;

import com.vkstech.auhorizationserver.model.AuthUserDetail;
import com.vkstech.auhorizationserver.model.User;
import com.vkstech.auhorizationserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Invalid Username");

        UserDetails userDetails = new AuthUserDetail(user);

        //check if account isAccountNonExpired/isAccountNonLocked/isCredentialsNonExpired/isEnabled
        new AccountStatusUserDetailsChecker().check(userDetails);

        return userDetails;
    }
}
