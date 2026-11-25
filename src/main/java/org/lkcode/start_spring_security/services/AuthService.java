package org.lkcode.start_spring_security.services;




import org.lkcode.start_spring_security.dto.AuthenticationRequest;
import org.lkcode.start_spring_security.dto.RegisterRequest;
import org.lkcode.start_spring_security.models.AuthenticationResponse;
import org.lkcode.start_spring_security.models.SecurityUser;
import org.lkcode.start_spring_security.models.UserProfile;
import org.lkcode.start_spring_security.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        validateCredentials(request == null ? null : request.getUsername(),
                request == null ? null : request.getPassword());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        UserProfile user = new UserProfile(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                List.of("USER")
        );

        UserProfile savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(new SecurityUser(savedUser));

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        validateCredentials(request == null ? null : request.getUsername(),
                request == null ? null : request.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserProfile userProfile = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        String jwtToken = jwtService.generateToken(new SecurityUser(userProfile));

        return new AuthenticationResponse(jwtToken);
    }

    private void validateCredentials(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or blank");
        }
    }
}
