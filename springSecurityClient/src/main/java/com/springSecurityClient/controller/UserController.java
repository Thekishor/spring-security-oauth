package com.springSecurityClient.controller;

import com.springSecurityClient.entities.User;
import com.springSecurityClient.entities.VerificationToken;
import com.springSecurityClient.event.RegistrationCompleteEvent;
import com.springSecurityClient.model.PasswordModel;
import com.springSecurityClient.model.UserModel;
import com.springSecurityClient.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel userModel,
                                          final HttpServletRequest request){
        User user = userService.registerUser(userModel);
        RegistrationCompleteEvent registrationCompleteEvent =
                new RegistrationCompleteEvent(user, applicationUrl(request));
        publisher.publishEvent(registrationCompleteEvent);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")){
            return new ResponseEntity<>("User Verifies Successfully", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Invalid or Expired Token",HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("token") String oldToken ,
                                                     HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return new ResponseEntity<>("Verification Link Send", HttpStatus.CREATED);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (user != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")){
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()){
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return new ResponseEntity<>("Password Reset Successfully", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordModel passwordModel){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
            return new ResponseEntity<>("Invalid Old Password", HttpStatus.NOT_FOUND);
        }
        else {
            userService.changePassword(user, passwordModel.getNewPassword());
            return new ResponseEntity<>("Password Change Successfully", HttpStatus.OK);
        }
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl
                + "/savePassword?token=" + token;

        //sendPasswordResetEmail()
        log.info("Click the link to Reset your Password: {}", url);
        return url;
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl
                + "/verifyRegistration?token=" + verificationToken.getToken();

        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}", url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" + request.getServerPort() +
                request.getContextPath();
    }
}
