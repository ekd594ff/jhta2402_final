package com.user.IntArea.dto.portfolio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.IntArea.dto.solution.SolutionRequestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class PortfolioRequestDto {

    private UUID id;
    private String title;
    private String description;
    private List<MultipartFile> images;
    private List<SolutionRequestDto> solutions;

    public PortfolioRequestDto(String title, String description, List<MultipartFile> images, String solutionStrings) throws JsonProcessingException {
        this.title = title;
        this.description = description;
        this.images = Objects.requireNonNullElseGet(images, ArrayList::new);

        if (solutionStrings == null) {
            this.solutions = new ArrayList<>();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();

            this.solutions = objectMapper.readValue(
                    solutionStrings,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SolutionRequestDto.class));
        }
    }
}
