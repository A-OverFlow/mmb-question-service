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

  @NotBlank(message = "Author cannot be blank")
  private String author;

  public static QuestionCreateDto from(String subject, String content, String author) {
    return QuestionCreateDto.builder()
        .subject(subject)
        .content(content)
        .author(author)
        .build();
  }
}
