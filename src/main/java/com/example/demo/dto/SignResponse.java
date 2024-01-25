package com.example.demo.dto;

import com.example.demo.entity.Authority;
import com.example.demo.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;

    private String nickname;

    private String email;

    private List<Authority> roles = new ArrayList<>();

    private String token;

    private boolean isActivated;

    public SignResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.roles = member.getRoles();
    }
}