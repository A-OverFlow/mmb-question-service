package com.aoverflow.mmb.question_service.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotNull(message = "Author id cannot be null")
  private Long authorId;

  @NotBlank(message = "Author name cannot be blank")
  private String authorName;

  public static QuestionCreateDto from(String subject, String content, Long authorId, String authorName) {
    return QuestionCreateDto.builder()
        .subject(subject)
        .content(content)
        .authorId(authorId)
        .authorName(authorName)
        .build();
  }
}
