package com.user.IntArea.controller;

import com.user.IntArea.dto.company.CompanyRequestDto;
import com.user.IntArea.dto.member.MemberRequestDto;
import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.repository.CompanyRepository;
import com.user.IntArea.repository.PortfolioRepository;
import com.user.IntArea.service.CompanyService;
import com.user.IntArea.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 스프링 통합 테스트
 * -> 전체 테스트 가능 / 실행 시간 느림
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 테스트용 h2 DB 연결정보
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // h2 DB 연결
class CompanyControllerIntegrationTest {

    @Autowired
    private MemberController memberController;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @Transactional
    @DisplayName("회사 생성 테스트")
    void 회사_등록_성공() {

        // given : 로그인 할 멤버 이메일과, 등록 할 회사 정보가 주어진다.
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("test@test.com");
        memberRequestDto.setPassword("asdf1234");
        memberRequestDto.setUsername("test");
        memberRequestDto.setPlatform(Platform.SERVER);

        CompanyRequestDto companyRequestDto = new CompanyRequestDto(
                "회사 이름1",
                "회사 설명1",
                "010-1234-5678",
                "부산 해운대구 APEC로 17 (센텀리더스마크)"
        );
        companyRequestDto.setImage(new MockMultipartFile("temp.png", "".getBytes()));

        // when : 회원가입 & 로그인 후, 계정으로 회사 생성을 요청한다.
        memberService.signup(memberRequestDto);

        memberController.login(memberRequestDto);

        companyService.create(companyRequestDto);

        // then : 등록한 회사의 이름, 생성멤버 이메일이 같다.
        List<Company> companyList = companyRepository.findAll();
        Company company = companyList.get(companyList.size() - 1);
        assertThat(company).isNotNull();
        assertThat(company.getCompanyName()).isEqualTo("회사 이름1");

        assertThat(company.getMember().getEmail()).isEqualTo("test@test.com");
    }
}