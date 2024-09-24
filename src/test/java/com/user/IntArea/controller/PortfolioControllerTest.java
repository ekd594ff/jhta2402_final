package com.user.IntArea.controller;

import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 테스트용 h2 DB 연결정보
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // h2 DB 연결
public class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 목 객체 생성
    @MockBean
    private PortfolioService portfolioService;

    @Test
    public void testGetOpenPortfolioInfoDtos() throws Exception {

        Portfolio portfolioExample = new Portfolio();
        // Given: PortfolioService가 반환할 데이터를 설정
        Page<PortfolioInfoDto> page = new PageImpl<>(Collections.singletonList(new PortfolioInfoDto(portfolioExample)));

        // 메서드 동작 정의
        when(portfolioService.getOpenPortfolioInfoDtos(any(Pageable.class))).thenReturn(page);

        // When & Then: GET 요청을 보내고, 결과 검증
        mockMvc.perform(get("/api/portfolio/list")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Portfolio"))
                .andExpect(jsonPath("$.content.length()").value(1));

        // 메서드 호출 검증
        verify(portfolioService, times(1)).getOpenPortfolioInfoDtos(any(Pageable.class));
    }
}
