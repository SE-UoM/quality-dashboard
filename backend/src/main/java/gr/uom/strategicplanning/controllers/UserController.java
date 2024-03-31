package gr.uom.strategicplanning.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uom.strategicplanning.controllers.requests.UserRegistrationRequest;
import gr.uom.strategicplanning.controllers.responses.implementations.UserResponse;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.UserRepository;
import gr.uom.strategicplanning.services.UserService;
import gr.uom.strategicplanning.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRegistrationRequest registrationRequest){
        User user = userService.createUser(registrationRequest);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<UserResponse> verifyUser(@RequestParam String token, @RequestParam Long uid){
        User user = userService.verifyUser(token, uid);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/verify/resend")
    public ResponseEntity<UserResponse> resendVerification(@RequestParam Long uid){
        User user = userService.resendVerification(uid);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/all")
    List<UserResponse> getAllUser(){
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = new ArrayList<>();

        for(User user : users){
            UserResponse userResponse = new UserResponse(user);
            userResponses.add(userResponse);
        }

        return userResponses;
    }



    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        UserResponse userResponse = new UserResponse(user);
        return userResponse;
    }

    @GetMapping("/token/refresh")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        boolean authHeaderIsNotPresent = authorizationHeader == null || !authorizationHeader.startsWith("Bearer ");

        if(authHeaderIsNotPresent) throw new RuntimeException("Refresh token is missing");

        try {
            DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(authorizationHeader);
            String email = decodedJWT.getSubject();

            //get user in order to get roles
            User user = userService.getUserByEmail(email);

            List<String> roles = stream(user.getRoles().split(",")).collect(Collectors.toList());
            String name = user.getName();
            String id = user.getId().toString();

            // Generate tokens
            String accessToken = TokenUtil.generateAccessToken(email, request.getRequestURL(), id, name, roles);
            String refreshToken = TokenUtil.generateRefreshToken(email, request.getRequestURL());

            Map<String,String> tokens= new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }
        catch (Exception e){
            System.err.println("Error with token: " + e.getMessage());
            response.setHeader("error",e.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String,String> error= new HashMap<>();
            error.put("error_message", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);

            e.printStackTrace();
        }
    }
}
