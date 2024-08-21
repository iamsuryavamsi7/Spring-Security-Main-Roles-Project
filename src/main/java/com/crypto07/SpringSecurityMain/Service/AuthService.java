package com.crypto07.SpringSecurityMain.Service;

import com.crypto07.SpringSecurityMain.Entity.Role;
import com.crypto07.SpringSecurityMain.Entity.UserEntity;
import com.crypto07.SpringSecurityMain.Error.UserIsNotAuthenticated;
import com.crypto07.SpringSecurityMain.Model.AuthenticateRequestModel;
import com.crypto07.SpringSecurityMain.Model.RegistrationRequestModel;
import com.crypto07.SpringSecurityMain.Model.AuthenticationResponse;
import com.crypto07.SpringSecurityMain.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse registerMember(RegistrationRequestModel request) {

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity user = new UserEntity();

        BeanUtils.copyProperties(request, user);

        user.setRole(Role.MEMBER);

        userRepo.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticateRequestModel request) throws UserIsNotAuthenticated {

        if ( request.getPassword().equals(request.getConformPassword()) ) {

            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));

            if ( authenticate.isAuthenticated() ){

                UserDetails user = userRepo.findByEmail(request.getEmail());

                String jwtToken = jwtService.generateToken(user);

                return AuthenticationResponse.builder()
                        .jwtToken(jwtToken)
                        .build();

            }else {

                System.out.println("Invalid Credentials");

                throw new UserIsNotAuthenticated("Invalid Credentials");

            }

        }

        throw new UsernameNotFoundException("User Not Found (or) Invalid Credentials");

    }

    public AuthenticationResponse registerAdmin(RegistrationRequestModel request) {

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity user = new UserEntity();

        BeanUtils.copyProperties(request, user);

        user.setRole(Role.ADMIN);

        userRepo.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();

    }
}
