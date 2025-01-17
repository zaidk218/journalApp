package com.zaid.journalApp.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmailDto {
    private String to;
    private String subject;
    private String body;
    private String from;
    private boolean isHtml;
}