package com.example.demo.service;

import com.example.demo.entity.Authority;
import com.example.demo.entity.Member;
import com.example.demo.dto.SignRequest;
import com.example.demo.dto.SignResponse;
import com.example.demo.repository.MemberRepository;
import com.example.demo.unit.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public SignResponse login(SignRequest request) throws Exception {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return SignResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .roles(member.getRoles())
                .token(jwtProvider.createToken(member.getEmail(), member.getRoles()))
                .build();

    }

    public boolean register(SignRequest request) throws Exception {
        try {
            Member member = Member.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .hashtag("")
                    .myself("")
                    .isActivated(false)
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            memberRepository.save(member);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignResponse getMember(String email) throws Exception {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new SignResponse(member);
    }


    public boolean updateNickname(String nickname, String email) throws Exception {
        try {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
            member.setNickname(nickname);
            memberRepository.save(member);
        } catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public boolean updateMyselfAndHashtag(String myself, String hashtag, String name) throws Exception {
        try {
            Member member = memberRepository.findByEmail(name)
                    .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
            member.setHashtag(myself);
            member.setMyself(hashtag);
            memberRepository.save(member);
        } catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }

        return true;
    }
}