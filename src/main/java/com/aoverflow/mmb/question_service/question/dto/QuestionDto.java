package com.aoverflow.mmb.question_service.question.dto;

import com.aoverflow.mmb.question_service.answer.dto.AnswerDto;
import com.aoverflow.mmb.question_service.question.entity.Question;
import com.aoverflow.mmb.question_service.question.entity.QuestionStatus;
import java.time.LocalDateTime;
import java.util.List;
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
  private String author;
  private QuestionStatus status;
  private List<AnswerDto> answers;
  private LocalDateTime createdAt;
  private LocalDateTime editedAt;

  public static QuestionDto from(Question question) {

    List<AnswerDto> answerDtoList = question.getAnswers().stream()
        .map(AnswerDto::from)
        .toList();

    return QuestionDto.builder()
        .id(question.getId())
        .subject(question.getSubject())
        .content(question.getContent())
        .author(question.getAuthor())
        .status(question.getStatus())
        .answers(answerDtoList)
        .createdAt(question.getCreatedAt())
        .editedAt(question.getEditedAt())
        .build();
  }
}
