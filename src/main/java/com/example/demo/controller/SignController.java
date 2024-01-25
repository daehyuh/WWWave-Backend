package com.example.demo.controller;

import com.example.demo.dto.SignRequest;
import com.example.demo.dto.SignResponse;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final MemberRepository memberRepository;
    private final SignService memberService;

    @Operation(operationId = "login", summary = "로그인", description = "요청을 검토한뒤 로그인", tags = "SignController")
    @PostMapping(value = "/login")
    public ResponseEntity<SignResponse> signin(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @Operation(operationId = "register", summary = "회원가입", description = "요청을 검토한뒤 회원가입", tags = "SignController")
    @PostMapping(value = "/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @Operation(operationId = "myinfo", summary = "내정보 보기", description = "요청을 검토한뒤 회원수정", tags = "SignController")
    @GetMapping("/myinfo")
    public ResponseEntity<SignResponse> myinfo(Principal principal) throws Exception {
        System.out.println(principal.getName());
        return new ResponseEntity<>( memberService.getMember(principal.getName()), HttpStatus.OK);
    }

    @Operation(operationId = "update", summary = "닉네임 수정", description = "요청을 검토한뒤 회원수정", tags = "SignController")
    @PostMapping("/updateNick")
    public ResponseEntity<Boolean> updateNick(@RequestBody SignRequest request, Principal principal) throws Exception {
        System.out.println(principal.getName());
        return new ResponseEntity<Boolean>(memberService.updateNickname(request.getNickname(), principal.getName()), HttpStatus.OK);
    }

    // 자기소개, 태그 수정
    @Operation(operationId = "update", summary = "자기소개, 태그 수정", description = "요청을 검토한뒤 회원수정", tags = "SignController")
    @PostMapping("/updateMyself")
    public ResponseEntity<Boolean> update(@RequestBody SignRequest request, Principal principal) throws Exception {
        System.out.println(principal.getName());
        return new ResponseEntity<Boolean>(memberService.updateMyselfAndHashtag(request.getHashtag(), request.getHashtag(), principal.getName()), HttpStatus.OK);
    }

}