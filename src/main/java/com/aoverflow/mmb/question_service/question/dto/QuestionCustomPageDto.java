package com.aoverflow.mmb.question_service.question.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Builder
public class QuestionCustomPageDto<T> {

  private List<T> questions; // 필드명 content 대신 questions으로 변경.

  private Pageable pageable;

  private int totalPages;
  private long totalElements;
  private boolean last;
  private int size;
  private int number;

  private Sort sort;

  private int numberOfElements;
  private boolean first;
  private boolean empty;

  public static <T> QuestionCustomPageDto from(Page<T> page) {

    return QuestionCustomPageDto.<T>builder()
        .questions(page.getContent())
        .pageable(page.getPageable())
        .totalPages(page.getTotalPages())
        .totalElements(page.getTotalElements())
        .last(page.isLast())
        .size(page.getSize())
        .number(page.getNumber())
        .sort(page.getSort())
        .numberOfElements(page.getNumberOfElements())
        .first(page.isFirst())
        .empty(page.isEmpty())
        .build();
  }
}
