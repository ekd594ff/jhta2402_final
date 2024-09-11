package com.user.IntArea.dto.report;

import com.user.IntArea.entity.enums.ReportSort;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private UUID id;
    private UUID refId;
    private UUID memberId;
    private ReportSort sort;
    private String title;
    private String description;
}
