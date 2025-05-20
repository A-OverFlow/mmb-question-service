package com.aoverflow.mmb.question_service.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionUpdateDto {

  @NotNull(message = "Id cannot be null")
  private Long id;

  @NotBlank(message = "Subject cannot be blank")
  private String subject;

  @NotBlank(message = "Content cannot be blank")
  private String content;

  public static QuestionUpdateDto from(Long id, String subject, String content) {
    return QuestionUpdateDto.builder()
        .id(id)
        .subject(subject)
        .content(content)
        .build();
  }
}
