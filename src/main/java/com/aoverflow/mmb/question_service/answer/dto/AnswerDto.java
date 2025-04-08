package com.aoverflow.mmb.question_service.answer.dto;

import com.aoverflow.mmb.question_service.answer.entity.Answer;
import com.aoverflow.mmb.question_service.answer.entity.AnswerStatus;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerDto {

  private Long id;
  private String content;
  private String author;
  private AnswerStatus status;
  private List<CommentDto> comments;
  private List<EmotionDto> emotions;

  public static AnswerDto from(Answer answer) {
    List<CommentDto> commentDtoList = answer.getComments().stream()
        .map(CommentDto::from)
        .toList();

    List<EmotionDto> emotionDtoList = answer.getEmotions().stream()
        .map(EmotionDto::from)
        .toList();

    return AnswerDto.builder()
        .id(answer.getId())
        .content(answer.getContent())
        .author(answer.getAuthor())
        .status(answer.getStatus())
        .comments(commentDtoList)
        .emotions(emotionDtoList)
        .build();
  }
}
