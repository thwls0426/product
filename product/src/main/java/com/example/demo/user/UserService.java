package com.example.demo.user;

import com.example.demo.core.error.Exception.Exception400;
import com.example.demo.core.error.Exception.Exception401;
import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.security.JwtTokenProvider;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void join(UserRequest.JoinDTO joinDTO) {

        Optional<User> byEmail = userRepository.findByEmail(joinDTO.getEmail());

        // 이미 존재하는 이메일인 경우 예외 처리
        if (byEmail.isPresent()) {
            throw new Exception400("이미 존재하는 이메일입니다: " + joinDTO.getEmail());
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(joinDTO.getPassword());
        joinDTO.setPassword(encodedPassword);

        userRepository.save(joinDTO.toEntity(passwordEncoder));
    }

    public String login(UserRequest.JoinDTO requestDTO) {
        // ** 인증 작업.
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken
            );

            // ** 인증 완료 값을 받아온다.
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // ** 토큰 발급.
            return JwtTokenProvider.create(customUserDetails.getUser());

        } catch (Exception e) {
            // 401 반환.
            throw new Exception401("인증되지 않음.");
        }
    }

}
