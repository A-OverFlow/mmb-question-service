package com.aoverflow.mmb.question_service.question.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionCountDto {

  private Long count;

  public static QuestionCountDto from(Long count) {
    return QuestionCountDto.builder()
        .count(count)
        .build();
  }
}
