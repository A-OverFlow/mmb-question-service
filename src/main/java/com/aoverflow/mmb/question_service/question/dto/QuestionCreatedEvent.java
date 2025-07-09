package com.aoverflow.mmb.question_service.question.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionCreatedEvent {

  private Long questionId;
  private Long authorId;
  private String subject;
}
