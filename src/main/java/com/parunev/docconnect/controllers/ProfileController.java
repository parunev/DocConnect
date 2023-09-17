package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.user.profile.*;
import com.parunev.docconnect.services.UserProfileService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.profile.ApiChangeEmail;
import com.parunev.docconnect.utils.annotations.swagger.profile.ApiChangePassword;
import com.parunev.docconnect.utils.annotations.swagger.profile.ApiUpdateProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-profile")
@Schema(name = "Profile Controller", description = "CRUD operations for user profile.")
public class ProfileController {

    private final UserProfileService userProfileService;
    private final DCLogger dcLogger = new DCLogger(ProfileController.class);

    @ApiUpdateProfile
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProfileResponse> updateUserProfile(@RequestBody ProfileRequest request){
        dcLogger.info("Request to update user profile.");
        return ResponseEntity.ok(userProfileService.updateUserProfile(request));
    }

    @ApiChangePassword
    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PasswordChangeResponse> changeUserPassword(@RequestBody PasswordChangeRequest request){
        dcLogger.info("Request to change user password.");
        return ResponseEntity.ok(userProfileService.changeUserPassword(request));
    }


    @ApiChangeEmail
    @PutMapping("/change-email")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<EmailChangeResponse> changeUserEmail(@RequestBody EmailChangeRequest request){
        dcLogger.info("Request to change user email.");
        return ResponseEntity.ok(userProfileService.changeUserEmail(request));
    }
}
