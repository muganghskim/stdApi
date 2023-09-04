package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.EmailAuth;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.repository.EmailAuthRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import com.stdApi.pacificOcean.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    private final JavaMailSender javaMailSender;

    @Autowired
    public UserService(UserRepository userRepository, EmailAuthRepository emailAuthRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager, JavaMailSender javaMailSender)
    {
        this.userRepository = userRepository;
        this.emailAuthRepository = emailAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.javaMailSender = javaMailSender;
    }

    // 회원가입 : 아이디와 패스워드 닉네임만 저장
    // todo : 추가정보 처리 ex: role
    public Member signUp(String userEmail, String password, String username) {
        // 중복 회원 확인
        Optional<Member> existingMember = userRepository.findByUserEmail(userEmail);
        if (existingMember.isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 회원입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Member member = Member.builder()
                .userEmail(userEmail)
                .password(encodedPassword)
                .userName(username)
                .build();
        return userRepository.save(member);
    }

    // 로그인
    public Map<String, Object> login(String userId, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
        log.info("인증: " + authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        log.info("토큰 생성: " + token);

        // 리프레시 토큰 생성
        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        log.info("리프레시 토큰 생성: " + refreshToken);

        // 사용자의 역할 확인
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        // 사용자 이름 가져오기
        String username = userDetails.getUsername();

        if (isAdmin) {
            log.info("관리자 권한 사용자");
        } else {
            log.info("일반 사용자 권한");
        }

        // 토큰, 사용자 이름 및 이메일 리턴
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        resultMap.put("refreshToken", refreshToken); //리프레시 토큰 추가
        resultMap.put("username", username);
        resultMap.put("userId", userId);

        return resultMap;
    }

    // 프로필 관리
    public Member getProfile(String userEmail) {
        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        return memberOpt.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
    }

    // 프로필 업데이트
    public Member updateProfile(String userEmail, String newUsername, String newPassword) {
        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        Member member = memberOpt.get();
        if (newUsername != null) {
            member.setUserName(newUsername);
        }
        if (newPassword != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            member.setPassword(encodedPassword);
        }

        return userRepository.save(member);
    }

    // 프로필 삭제
    public Member deleteProfile(String userEmail) {
        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        userRepository.delete(memberOpt.get());
        return null;
    }

    // 리프레쉬 토큰으로 액세스 토큰 업데이트
    public String updateAccessToken(String refreshToken) throws Exception {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh Token is missing");
        }

        // 액세스 토큰 업데이트
        return jwtProvider.updateAccessToken(refreshToken);
    }

    public String generateRandomCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;  // six digit positive number
        return String.valueOf(code);
    }

    public void sendVerificationEmail(String userEmail) {
        String verificationCode = generateRandomCode();

        // Invalidate the existing code and update with the new code
        Optional<EmailAuth> existingCode = emailAuthRepository.findByEmail(userEmail);

        if (existingCode.isPresent()) {
            EmailAuth emailAuth = existingCode.get();
            emailAuth.setExpiresAt(new Date(0));
            emailAuth.setCode(verificationCode);
            emailAuthRepository.save(emailAuth);
        } else {
            EmailAuth newEmailAuth = EmailAuth.builder()
                    .email(userEmail)
                    .code(verificationCode)
                    .verify("N")
                    .build();
            emailAuthRepository.save(newEmailAuth);
        }
        log.info("email={}",userEmail);

        // Send the verification code via email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom("rhgustmfrh@gmail.com");
        message.setSubject("Your verification code");
        message.setText("Your verification code is: " + verificationCode);

        log.info("mail message={}", message);

        javaMailSender.send(message);
    }


    public void verifyEmail(String userEmail, String code) {
        // DB에서 해당 사용자의 이메일 주소로 저장된 인증 코드 가져오기
        Optional<EmailAuth> emailCode = emailAuthRepository.findByEmail(userEmail);
        // 제출된 code와 DB에 저장된 인증 코드 비교
        if (!emailCode.isPresent()) {
            throw new RuntimeException("이메일을 찾을 수 없습니다.");
        }
        EmailAuth emailAuth = emailCode.get();
        // 일치하면 해당 사용자의 '이메일 확인' 상태를 true로 업데이트
        if(new Date().before(emailAuth.getExpiresAt())){
            if(emailAuth.getCode().equals(code)){
                emailAuth.setVerify("Y");
                emailAuthRepository.save(emailAuth);
            }
            else throw new RuntimeException("일치하지 않는 코드입니다.");
        }
        else throw new RuntimeException("시간 초과하였습니다.");
    }

    public boolean isVerified(String userEmail) {
        // DB에서 해당 사용자의 '이메일 확인' 상태 가져오기
        Optional<EmailAuth> emailCode = emailAuthRepository.findByEmail(userEmail);
        log.info("emailCode = {}", emailCode);
        if (!emailCode.isPresent()) {
            return false;
        }
        EmailAuth emailAuth = emailCode.get();
        if(emailAuth.getVerify().equals("Y")){

            return true;
        }
        else return false;
    }

    public String simpleLogin(String userEmail) {
        // Find the user in your database
        Optional<Member> optionalMember  = userRepository.findByUserEmail(userEmail);
        if (optionalMember .isPresent()) {
            Member member = optionalMember .get();
            Authentication authentication = new UsernamePasswordAuthenticationToken(member.getUserEmail(), null, Collections.singleton(new SimpleGrantedAuthority(member.getUserRole())));
            return jwtProvider.generateToken(authentication);
        }
        // Create an Authentication object for the user
        else return null;
        // Generate a new JWT for the authenticated user
    }
}
