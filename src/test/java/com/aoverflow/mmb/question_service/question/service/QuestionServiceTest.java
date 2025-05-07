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
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class QuestionServiceTest {

  @Autowired
  QuestionService questionService;

  private final static String QUESTION_SUBJECT = "더미 질문 제목";
  private final static String QUESTION_CONTENT = "더미 질문 내용";
  private final static Long QUESTION_AUTHOR_ID = 123L;
  private final static String QUESTION_AUTHOR_NAME = "더미 질문 작성자";
  private final static int PAGE_NUMBER = 0;
  private final static int PAGE_SIZE = 3;

  @Test
  @DisplayName("질문 단건 조회")
  void get() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT,
        QUESTION_AUTHOR_ID,
        QUESTION_AUTHOR_NAME
    );
    QuestionDto createdQuestionDto = questionService.create(questionCreateDto);

    // when
    QuestionDto foundQuestion = questionService.get(createdQuestionDto.getId());

    // then
    assertEquals(createdQuestionDto.getId(), foundQuestion.getId());
  }

  @Test
  @DisplayName("질문 목록 조회")
  void getAll() {
    // given
    List<Long> createdQuestionIds = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
          QUESTION_SUBJECT + i,
          QUESTION_CONTENT + i,
          QUESTION_AUTHOR_ID + i,
          QUESTION_AUTHOR_NAME + i
      );
      QuestionDto createdQuestionDto = questionService.create(questionCreateDto);
      createdQuestionIds.add(createdQuestionDto.getId());
    }

    // when
    PageRequest pageRequest = PageRequest.of(
        PAGE_NUMBER,
        PAGE_SIZE,
        Sort.by(Sort.Direction.DESC, "createdAt")
    );
    Page<QuestionDto> page = questionService.getAll(pageRequest);

    // then
    List<QuestionDto> content = page.getContent();    // 조회된 데이터
    assertEquals(3, content.size());          // 조회된 데이터 수
    assertEquals(10, page.getTotalElements()); // 전체 데이터 수
    assertEquals(0, page.getNumber());      // 페이지 번호
    assertEquals(4, page.getTotalPages());  // 전체 페이지 번호
    assertTrue(page.isFirst());   // 첫번째 항목인가?
    assertTrue(page.hasNext());   // 다음 페이지가 있는가?

    // 정렬이 의도대로 잘 됐는가?
    createdQuestionIds.sort(Comparator.reverseOrder());
    assertEquals(createdQuestionIds.stream().skip(PAGE_NUMBER).limit(PAGE_SIZE).toList(), content.stream().map(QuestionDto::getId).toList());
  }

  @Test
  @DisplayName("질문 생성")
  void create() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT,
        QUESTION_AUTHOR_ID,
        QUESTION_AUTHOR_NAME
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
        QUESTION_AUTHOR_ID,
        QUESTION_AUTHOR_NAME
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
        QUESTION_AUTHOR_ID,
        QUESTION_AUTHOR_NAME
    );
    QuestionDto createdQuestionDto = questionService.create(questionCreateDto);

    // when
    questionService.delete(createdQuestionDto.getId());

    // then
    assertThrows(EntityNotFoundException.class,
        () -> questionService.get(createdQuestionDto.getId()));
  }
}
