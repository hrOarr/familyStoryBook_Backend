package com.astrodust.familyStoryBook_backend.utils;

import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    private final FamilyService familyService;

    public AuthenticatedUser(FamilyService familyService){
        this.familyService = familyService;
    }

    public FamilyAccount get(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return familyService.getByEmail(userDetails.getUsername());
    }
}
