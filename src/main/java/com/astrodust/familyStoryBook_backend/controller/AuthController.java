package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.FamilyLoginRequestDTO;
import com.astrodust.familyStoryBook_backend.dto.FamilyLoginResponseDTO;
import com.astrodust.familyStoryBook_backend.dto.FamilyRegisterDTO;
import com.astrodust.familyStoryBook_backend.exception.ValidationException;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.security.JwtToken;
import com.astrodust.familyStoryBook_backend.service.FamilyService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private FamilyService familyService;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier(value = "userDetailsServiceImp")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    public AuthController(FamilyService familyService, PasswordEncoder passwordEncoder,
                          ModelMapper modelMapper, AuthenticationManager authenticationManager){
        this.familyService = familyService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @ApiOperation(value = "Family Login")
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody FamilyLoginRequestDTO familyLoginRequestDTO) throws Exception {
//        if(result.hasErrors()){
//            List<String> errors = result.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
//        }
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
            logger.info("SoA:: Exception from login method------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
        FamilyLoginResponseDTO familyLoginResponseDTO = new FamilyLoginResponseDTO(account.getUsername(), account.getEmail(), token);
        return ResponseEntity.status(HttpStatus.OK).body(familyLoginResponseDTO);
    }

    private void authenticate(String email, String password) throws Exception {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("email = " + userDetails.getUsername() + ", password = " + userDetails.getPassword());
            logger.info("authorities = " + userDetails.getAuthorities());
        }
        catch (DisabledException e){
            logger.error("From authenticate method------User Disabled" + e.getMessage());
            throw new ValidationException("User Disabled");
        }

        catch (BadCredentialsException e){
            logger.error("From authenticate method------Bad credentials" + e.getMessage());
            throw new ValidationException("Email or password is not correct");
        }
        catch (Exception e){
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Family Sign-up")
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody FamilyRegisterDTO familyRegisterDTO) throws Exception {
        FamilyAccount account = null;
        try {
            account = modelMapper.map(familyRegisterDTO, FamilyAccount.class);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            account.setCreatedDate(LocalDateTime.now());
            familyService.save(account);
        } catch (Exception e) {
            throw new Exception("Something went wrong. Please try again");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping
    public ResponseEntity<?> check()
    {
        return ResponseEntity.ok("Its Ok!");
    }
}
