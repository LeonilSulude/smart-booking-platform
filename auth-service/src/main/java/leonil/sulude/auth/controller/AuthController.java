package leonil.sulude.auth.controller;

import leonil.sulude.auth.dto.AuthRequest;
import leonil.sulude.auth.dto.AuthResponse;
import leonil.sulude.auth.dto.RegisterRequest;
import leonil.sulude.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint to register a new user.
     * Accepts a RegisterRequest (email, password, role).
     * Returns a JWT token upon successful registration.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to authenticate a user.
     * Accepts an AuthRequest (email and password).
     * Returns a JWT token if the credentials are valid.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
