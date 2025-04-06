package com.aoverflow.mmb.question_service.answer.dto;

import com.aoverflow.mmb.question_service.answer.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentDto {

  private Long id;
  private String content;
  private String author;

  public static CommentDto from(Comment comment) {
    return CommentDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .author(comment.getAuthor())
        .build();
  }
}
