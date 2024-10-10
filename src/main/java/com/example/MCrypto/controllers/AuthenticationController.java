package com.example.MCrypto.controllers;

import com.example.MCrypto.dto.LoginDTO;
import com.example.MCrypto.dto.RegisterDTO;
import com.example.MCrypto.dto.Response;
import com.example.MCrypto.exception.ForbiddenException;
import com.example.MCrypto.exception.NotFoundException;
import com.example.MCrypto.models.Roles;
import com.example.MCrypto.models.UserEntity;
import com.example.MCrypto.repository.RoleRepository;
import com.example.MCrypto.repository.SettingsRepository;
import com.example.MCrypto.repository.UserRepository;
import com.example.MCrypto.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.of;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    @Autowired
    private SettingsRepository settingsRepository;


    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("login")
    public ResponseEntity<Response> login(@RequestBody LoginDTO loginDto){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            UserEntity user = userRepository.findByPhoneNumber(loginDto.getUsername()).get();



            Map<String, Object> tokenAndUserMap = new HashMap<>();
            tokenAndUserMap.put("token",token);
            tokenAndUserMap.put("userInfo",user);

            return buildResponse("user",tokenAndUserMap, "Login successful", HttpStatus.OK);


        }catch (AuthenticationException e){
            return buildResponse(null,null, "Invalid email or password", HttpStatus.UNAUTHORIZED);
        }


    }

    @PostMapping("setPassword")
    public ResponseEntity<Response> setPassword(@RequestBody LoginDTO setPassDto) {
        UserEntity user = userRepository.findByPhoneNumber(setPassDto.getUsername()).orElseThrow(()-> new NotFoundException("user not found"));

        if(!user.getVerified()){
            throw new ForbiddenException("phone number not verified");
        }

        user.setPassword(passwordEncoder.encode((setPassDto.getPassword())));


        return buildResponse("user",userRepository.save(user), "Password created successfully", HttpStatus.CREATED);


    }


    @PostMapping("register")
    public ResponseEntity<Response> register(@RequestBody RegisterDTO registerDto) {
        if ( userRepository.existsByPhoneNumber(registerDto.getPhoneNumber()) ) {
            return buildResponse(null,null, "Email or Phone Number already taken", HttpStatus.CONFLICT);

        }

        UserEntity user = new UserEntity();

        user.setPhoneNumber(registerDto.getPhoneNumber());
        //user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        Roles roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));


        return buildResponse("user",userRepository.save(user), "Registration successful", HttpStatus.CREATED);

    }

    @PostMapping("register/admin")
    public ResponseEntity<Response> registerAdmin(@RequestBody  RegisterDTO registerDto) {
        if (userRepository.existsByPhoneNumber(registerDto.getPhoneNumber()) ) {
            return buildResponse(null,null, "Email or Phone Number already taken", HttpStatus.CONFLICT);

        }

        UserEntity user = new UserEntity();
        user.setPhoneNumber(registerDto.getPhoneNumber());
        //user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        Roles roles = roleRepository.findByName("ADMIN").get();
        user.setRoles(Collections.singletonList(roles));


        return buildResponse("user",userRepository.save(user), "Registration successful", HttpStatus.CREATED);

    }

    //send otp



    private ResponseEntity<Response> buildResponse(String desc,Object data, String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(data == null ? null : of(desc, data))
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .build());
    }
}
