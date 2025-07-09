package com.aoverflow.mmb.question_service.question.dto;

import com.aoverflow.mmb.question_service.question.entity.Question;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionDto {

  private Long id;
  private String subject;
  private String content;
  private AuthorDto author;
  private LocalDateTime createdAt;
  private LocalDateTime editedAt;

  public static QuestionDto from(Question question) {

    AuthorDto authorDto = AuthorDto.builder()
        .id(question.getAuthorId())
        .nickname(question.getAuthorNickname())
        .picture(question.getAuthorPicture())
        .build();

    return QuestionDto.builder()
        .id(question.getId())
        .subject(question.getSubject())
        .content(question.getContent())
        .author(authorDto)
        .createdAt(question.getCreatedAt())
        .editedAt(question.getEditedAt())
        .build();
  }

  @Getter
  @Setter
  @Builder
  public static class AuthorDto {

    private Long id;
    private String nickname;
    private String picture;
  }
}
