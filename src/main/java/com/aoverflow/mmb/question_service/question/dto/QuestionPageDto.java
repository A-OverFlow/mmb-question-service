package com.aoverflow.mmb.question_service.question.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionPageDto<T> {

  private List<T> questions;
  private boolean hasNext;
  private Long lastId;
  private Long totalElements;
  private int pageSize;

  public static <T> QuestionPageDto from(List<T> questions, boolean hasNext, Long lastId, Long totalElements, int pageSize) {

    return QuestionPageDto.<T>builder()
        .questions(questions)
        .hasNext(hasNext)
        .lastId(lastId)
        .totalElements(totalElements)
        .pageSize(pageSize)
        .build();
  }
}
