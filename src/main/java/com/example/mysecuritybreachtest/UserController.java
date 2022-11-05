package com.example.mysecuritybreachtest;

import com.example.mysecuritybreachtest.security.SecurityBreachTestUserDetails;
import com.example.mysecuritybreachtest.security.UserSession;
import com.example.mysecuritybreachtest.security.jwt.JwtResponse;
import com.example.mysecuritybreachtest.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceJpa userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserSession userSession;

    @GetMapping("")
    public List<User> listUsers() {
        return userService.listAll();
    }

    @GetMapping("/auth")
    public List<User> listUsersNoAuth() {
        return userService.listAll();
    }

    @PostMapping("/auth/register/{secure}")
    public User registerUser(@PathVariable("secure") boolean isSecure, @RequestBody User user) throws IOException {
        if (userService.checkUsernameUnique(user)) {
            return userService.registerUser(isSecure, user);
        } else {
            throw new IllegalArgumentException("Username already exists.");
        }
    }

    private ResponseEntity<Object> authenticateUser(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        SecurityBreachTestUserDetails userDetails = (SecurityBreachTestUserDetails ) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.user.getId(),
                userDetails.getUsername(),
                userDetails.user.getEmail(),
                roles));
    }

    @PostMapping("/auth/login/{secure}")
    public ResponseEntity<Object> loginUser(@PathVariable("secure") boolean isSecure, @RequestBody User user) {
        if (userService.checkUsernameExists(user) && userService.checkPassword(isSecure, user)) {
            if(isSecure) return authenticateUser(user);
            else return new ResponseEntity<Object>("User logged in!", HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("Invalid username/password.");
        }
    }
}
