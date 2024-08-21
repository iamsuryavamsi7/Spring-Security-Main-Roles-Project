package com.crypto07.SpringSecurityMain.Controller;

import com.crypto07.SpringSecurityMain.Error.UserIsNotAuthenticated;
import com.crypto07.SpringSecurityMain.Model.AuthenticateRequestModel;
import com.crypto07.SpringSecurityMain.Model.RegistrationRequestModel;
import com.crypto07.SpringSecurityMain.Model.AuthenticationResponse;
import com.crypto07.SpringSecurityMain.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registerMember")
    public ResponseEntity<AuthenticationResponse> registerMember(
            @RequestBody RegistrationRequestModel request
    ){

        AuthenticationResponse response = authService.registerMember(request);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequestModel request
    ) throws UserIsNotAuthenticated {

        AuthenticationResponse response = authService.authenticate(request);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @RequestBody RegistrationRequestModel request
    ){

        AuthenticationResponse response = authService.registerAdmin(request);

        return ResponseEntity.ok(response);

    }

}
