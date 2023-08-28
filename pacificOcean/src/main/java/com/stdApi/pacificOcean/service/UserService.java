package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.repository.UserRepository;
import com.stdApi.pacificOcean.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
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
}
