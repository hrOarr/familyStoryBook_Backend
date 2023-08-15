package com.astrodust.familyStoryBook_backend.service;

import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserDetailsServiceImp.class);
    private FamilyService familyService;

    @Autowired
    public UserDetailsServiceImp(FamilyService familyService){
        this.familyService = familyService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        FamilyAccount account = familyService.getByEmail(email);
        if(account==null){
            logger.info("User not found with email = " + email);
            throw new UsernameNotFoundException("User not found with this email = " + email);
        }
        logger.info("End of loadUserByUsername------------");
        return new User(account.getEmail(), account.getPassword(), new ArrayList<>());
    }
}
