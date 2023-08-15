package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.FamilyLoginRequestDTO;
import com.astrodust.familyStoryBook_backend.dto.FamilyLoginResponseDTO;
import com.astrodust.familyStoryBook_backend.dto.FamilyRegisterDTO;
import com.astrodust.familyStoryBook_backend.exception.ValidationException;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.security.JwtToken;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final FamilyService familyService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier(value = "userDetailsServiceImp")
    private UserDetailsService userDetailsService;

    private final JwtToken jwtToken;

    @Autowired
    public AuthController(FamilyService familyService, PasswordEncoder passwordEncoder,
                          ModelMapper modelMapper, AuthenticationManager authenticationManager,
                          JwtToken jwtToken){
        this.familyService = familyService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtToken = jwtToken;
    }

    @ApiOperation(value = "Family Login")
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody FamilyLoginRequestDTO familyLoginRequestDTO) throws Exception {
        // authentication
        String email = familyLoginRequestDTO.getEmail();
        authenticate(email, familyLoginRequestDTO.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(familyLoginRequestDTO.getEmail());
        final String token = jwtToken.generateToken(userDetails);
        FamilyAccount account = null;
        try{
            account = familyService.getByEmail(email);
        }
        catch (Exception e){
            log.error("{}", e.getMessage(), e);
            throw new Exception("Something went wrong. Please try again");
        }
        FamilyLoginResponseDTO familyLoginResponseDTO = new FamilyLoginResponseDTO(account.getId(), account.getUsername(), account.getEmail(), token);
        return ResponseEntity.status(HttpStatus.OK).body(familyLoginResponseDTO);
    }

    private void authenticate(String email, String password) throws Exception {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            log.info("email = " + userDetails.getUsername() + ", password = " + userDetails.getPassword());
            log.info("authorities = " + userDetails.getAuthorities());
        }
        catch (DisabledException e){
            log.error("{}", e.getMessage(), e);
            throw new ValidationException("User Disabled");
        }
        catch (BadCredentialsException e){
            log.error("{}", e.getMessage(), e);
            throw new ValidationException("Email or password is not correct");
        }
        catch (Exception e){
            log.error("{}", e.getMessage(), e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Family Sign-up")
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody FamilyRegisterDTO familyRegisterDTO) throws Exception, ValidationException {
        FamilyAccount account = null;
        try {
            account = modelMapper.map(familyRegisterDTO, FamilyAccount.class);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            account.setCreatedDate(LocalDateTime.now());
            FamilyAccount account1 = familyService.getByEmail(account.getEmail());
            if(account1!=null){
                throw new ValidationException("Email is already exists");
            }
            FamilyAccount account2 = familyService.getByUsername(account.getUsername());
            if(account2!=null){
                throw new ValidationException("Username already exists");
            }
            familyService.save(account);

            // authentication
            authenticate(familyRegisterDTO.getEmail(), familyRegisterDTO.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(familyRegisterDTO.getEmail());
            final String token = jwtToken.generateToken(userDetails);
            try{
                account = familyService.getByEmail(familyRegisterDTO.getEmail());
            }
            catch (Exception e){
                log.error("{}", e.getMessage(), e);
                throw new Exception("Something went wrong. Please try again");
            }
            FamilyLoginResponseDTO familyLoginResponseDTO = new FamilyLoginResponseDTO(account.getId(), account.getUsername(), account.getEmail(), token);
            return ResponseEntity.status(HttpStatus.OK).body(familyLoginResponseDTO);
        }
        catch (ValidationException e){
            log.error("{}", e.getMessage(), e);
            throw new ValidationException(e.getLocalizedMessage());
        }
        catch (Exception e) {
            log.error("{}", e.getMessage(), e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @GetMapping
    public ResponseEntity<?> check()
    {
        return ResponseEntity.ok("Its Ok!");
    }
}
