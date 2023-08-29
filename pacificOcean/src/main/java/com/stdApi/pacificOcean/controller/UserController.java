package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @Data
    public static class SignUpRequest {
        private String userEmail;
        private String password;
        private String username;
    }

    @Data
    public static class LoginRequest {
        private String userEmail;
        private String password;
    }

    @Data
    public static class UpdateProfileRequest {
        private String newUsername;
        private String newPassword;
    }

    // 회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Member signUp(@RequestBody SignUpRequest signUpRequest) throws Exception{
        if (!userService.isVerified(signUpRequest.getUserEmail())) {
            throw new Exception("Unverified email address");
        }

        return userService.signUp(signUpRequest.getUserEmail(), signUpRequest.getPassword(), signUpRequest.getUsername());
    }


    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> userInfo = userService.login(loginRequest.getUserEmail(), loginRequest.getPassword());
            log.info("토큰: " + userInfo.get("token"));

            // Return token and user info
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            log.error("Login error: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        SecurityContextHolder.clearContext();
        request.logout();
        response.setStatus(HttpServletResponse.SC_OK);
        log.info("로그아웃 성공: " + response.getStatus());
    }

    // 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<Member> getProfile(@RequestParam String userId) {
        try {
            Member member = userService.getProfile(userId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // todo : 프로필 업데이트 시 사용자 정보 추가 업데이트
    // 프로필 업데이트
    @PutMapping("/profile")
    public ResponseEntity<Member> updateProfile(@RequestParam String userEmail, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            Member updatedMember = userService.updateProfile(userEmail, updateProfileRequest.getNewUsername(), updateProfileRequest.getNewPassword());
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 회원탈퇴
    @PostMapping("/deleteProfile")
    public ResponseEntity<Member> deleteProfile(@RequestParam String userEmail) {
        try {
            Member deletedMember = userService.deleteProfile(userEmail);
            return ResponseEntity.ok(deletedMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update-token")
    public ResponseEntity<?> updateAccessToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");

        try {
            // 액세스 토큰 업데이트
            String newAccessToken = userService.updateAccessToken(refreshToken);
            return ResponseEntity.ok(Collections.singletonMap("accessToken", newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }
    }

    // 이메일로 인증 코드 보내기
    @PostMapping("/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody String userEmail) {
        try {
            userService.sendVerificationEmail(userEmail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 이메일 인증 코드 확인
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> verificationRequest) {
        String userEmail = verificationRequest.get("userEmail");
        String code = verificationRequest.get("code");

        try {
            userService.verifyEmail(userEmail, code);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
