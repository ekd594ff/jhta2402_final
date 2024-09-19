package com.user.IntArea.dto.member;

import com.user.IntArea.entity.Member;
import lombok.Data;

import java.util.UUID;

@Data
public class QuotationRequestMemberDto {

    private UUID memberId;
    private String email;
    private String username;
    private String memberUrl;

    public QuotationRequestMemberDto(Member member, String memberUrl) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.memberUrl = memberUrl;
    }
}
