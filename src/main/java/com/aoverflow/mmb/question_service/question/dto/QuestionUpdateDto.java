package com.aoverflow.mmb.question_service.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionUpdateDto {

  @NotBlank(message = "Subject cannot be blank")
  private String subject;

  @NotBlank(message = "Content cannot be blank")
  private String content;

  public static QuestionUpdateDto from(String subject, String content) {
    return QuestionUpdateDto.builder()
        .subject(subject)
        .content(content)
        .build();
  }
}
