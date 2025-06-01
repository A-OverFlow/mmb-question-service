package com.aoverflow.mmb.question_service.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionCreateDto {

  @NotBlank(message = "Subject cannot be blank")
  private String subject;

  @NotBlank(message = "Content cannot be blank")
  private String content;

  public static QuestionCreateDto from(String subject, String content) {
    return QuestionCreateDto.builder()
        .subject(subject)
        .content(content)
        .build();
  }
}
