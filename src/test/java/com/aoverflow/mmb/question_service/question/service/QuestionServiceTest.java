package com.aoverflow.mmb.question_service.question.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aoverflow.mmb.question_service.question.dto.QuestionCreateDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import com.aoverflow.mmb.question_service.question.entity.QuestionStatus;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class QuestionServiceTest {

  @Autowired
  QuestionService questionService;

  private final static String QUESTION_SUBJECT = "더미 질문 제목";
  private final static String QUESTION_CONTENT = "더미 질문 내용";
  private final static String QUESTION_AUTHOR = "더미 질문 작성자";

  @Test
  @DisplayName("질문 단건 조회")
  void get() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT,
        QUESTION_AUTHOR
    );
    QuestionDto createdQuestionDto = questionService.create(questionCreateDto);

    // when
    QuestionDto foundQuestion = questionService.get(createdQuestionDto.getId());

    // then
    assertEquals(createdQuestionDto.getId(), foundQuestion.getId());
  }

  @Test
  @DisplayName("질문 전체 조회")
  void getAll() {
    // given
    List<Long> createdQuestionIds = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
          QUESTION_SUBJECT + i,
          QUESTION_CONTENT + i,
          QUESTION_AUTHOR + i
      );
      QuestionDto createdQuestionDto = questionService.create(questionCreateDto);
      createdQuestionIds.add(createdQuestionDto.getId());
    }

    // when
    List<QuestionDto> foundQuestionDtoList = questionService.getAll();

    // then
    assertEquals(createdQuestionIds, foundQuestionDtoList.stream()
        .map(QuestionDto::getId)
        .toList()
    );
  }

  @Test
  @DisplayName("질문 생성")
  void create() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT,
        QUESTION_AUTHOR
    );

    // when
    QuestionDto createdQuestionDto = questionService.create(questionCreateDto);

    // then
    QuestionDto foundQuestion = questionService.get(createdQuestionDto.getId());
    assertEquals(createdQuestionDto.getId(), foundQuestion.getId());
  }

  @Test
  @DisplayName("질문 수정")
  void update() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT,
        QUESTION_AUTHOR
    );
    QuestionDto createdQuestionDto = questionService.create(questionCreateDto);

    QuestionUpdateDto questionUpdateDto = QuestionUpdateDto.from(
        createdQuestionDto.getId(),
        "수정한 제목",
        "수정한 내용",
        QuestionStatus.ING
    );

    // when
    QuestionDto updatedQuestionDto = questionService.update(questionUpdateDto);

    // then
    assertEquals(createdQuestionDto.getId(), updatedQuestionDto.getId());
    assertNotEquals(createdQuestionDto.getSubject(), updatedQuestionDto.getSubject());
    assertNotEquals(createdQuestionDto.getContent(), updatedQuestionDto.getContent());
    assertNotEquals(createdQuestionDto.getStatus(), updatedQuestionDto.getStatus());
    assertEquals(updatedQuestionDto.getCreatedAt(), createdQuestionDto.getCreatedAt());
    assertTrue(updatedQuestionDto.getEditedAt().isAfter(createdQuestionDto.getEditedAt()));
  }

  @Test
  @DisplayName("질문 삭제")
  void delete() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT,
        QUESTION_AUTHOR
    );
    QuestionDto createdQuestionDto = questionService.create(questionCreateDto);

    // when
    questionService.delete(createdQuestionDto.getId());

    // then
    assertThrows(EntityNotFoundException.class,
        () -> questionService.get(createdQuestionDto.getId()));
  }
}
