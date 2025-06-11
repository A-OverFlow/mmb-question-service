package com.aoverflow.mmb.question_service.question.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import com.aoverflow.mmb.question_service.common.feignClients.MemberServiceClient;
import com.aoverflow.mmb.question_service.member.dto.MemberDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionCreateDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionPageDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class QuestionServiceTest {

  private final static String QUESTION_SUBJECT = "더미 질문 제목";
  private final static String QUESTION_CONTENT = "더미 질문 내용";
  private final static Long QUESTION_AUTHOR_ID = 123L;
  private final static String QUESTION_AUTHOR_NAME = "더미 질문 작성자";
  private final static int PAGE_SIZE = 3;
  private final static int DEFAULT_PAGE_SIZE = 10;
  private final static int NEGATIVE_PAGE_SIZE = -1;
  private final static int ABUSIVE_PAGE_SIZE = 9999;

  @Autowired
  QuestionService questionService;

  @Autowired
  private EntityManager em;

  @MockitoBean
  MemberServiceClient memberServiceClient;

  private void mockAuthorFound() {
    // mocking FeignClient connection
    given(memberServiceClient.getMember(QUESTION_AUTHOR_ID))
        .willReturn(MemberDto.builder()
            .name(QUESTION_AUTHOR_NAME)
            .build()
        );
  }

  private void mockAuthorFound(int i) {
    // mocking FeignClient connection
    given(memberServiceClient.getMember(QUESTION_AUTHOR_ID + i))
        .willReturn(MemberDto.builder()
            .name(QUESTION_AUTHOR_NAME + i)
            .build()
        );
  }

  @Test
  @DisplayName("질문 단건 조회")
  void get() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT
    );

    mockAuthorFound();

    QuestionDto createdQuestionDto = questionService.create(QUESTION_AUTHOR_ID, questionCreateDto);

    // when
    QuestionDto foundQuestion = questionService.get(createdQuestionDto.getId());

    // then
    assertEquals(createdQuestionDto.getId(), foundQuestion.getId());
  }

  @Test
  @DisplayName("질문 목록 조회")
  void getPaginatedList() {
    // given
    List<Long> createdQuestionIds = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
          QUESTION_SUBJECT + i,
          QUESTION_CONTENT + i
      );
      mockAuthorFound(i);
      QuestionDto createdQuestionDto = questionService.create(QUESTION_AUTHOR_ID + i, questionCreateDto);
      createdQuestionIds.add(createdQuestionDto.getId());
    }

    // when
    QuestionPageDto<QuestionDto> page = questionService.getPaginatedList(null, PAGE_SIZE, null);
    QuestionPageDto<QuestionDto> wrongRequestPage1 = questionService.getPaginatedList(null, NEGATIVE_PAGE_SIZE, null);
    QuestionPageDto<QuestionDto> wrongRequestPage2 = questionService.getPaginatedList(null, ABUSIVE_PAGE_SIZE, null);

    // then
    List<QuestionDto> content = page.getQuestions();    // 조회된 데이터
    assertEquals(3, page.getPageSize());          // 조회된 데이터 수
    assertEquals(10, page.getTotalElements()); // 전체 데이터 수
    assertEquals(content.getLast().getId(), page.getLastId());        // 마지막 id(커서) 값
    assertTrue(page.isHasNext());   // 다음 페이지가 있는가?

    // then - 예외1(size가 음수일 때)
    List<QuestionDto> content1 = wrongRequestPage1.getQuestions();    // 조회된 데이터
    assertEquals(DEFAULT_PAGE_SIZE, wrongRequestPage1.getPageSize()); // 조회된 데이터 수
    assertEquals(10, wrongRequestPage1.getTotalElements()); // 전체 데이터 수
    assertEquals(content1.getLast().getId(), wrongRequestPage1.getLastId());        // 마지막 id(커서) 값
    assertFalse(wrongRequestPage1.isHasNext());   // 다음 페이지가 있는가?

    // then - 예외2(size가 터무니없이 클 때)
    List<QuestionDto> content2 = wrongRequestPage2.getQuestions();    // 조회된 데이터
    assertEquals(10, wrongRequestPage2.getPageSize());          // 조회된 데이터 수
    assertEquals(10, wrongRequestPage2.getTotalElements()); // 전체 데이터 수
    assertEquals(content2.getLast().getId(), wrongRequestPage1.getLastId());        // 마지막 id(커서) 값
    assertFalse(wrongRequestPage2.isHasNext());   // 다음 페이지가 있는가?

    // 정렬이 의도대로 잘 됐는가?
    createdQuestionIds.sort(Comparator.reverseOrder());
    assertEquals(createdQuestionIds.stream().limit(PAGE_SIZE).toList(), content.stream().map(QuestionDto::getId).toList());
  }

  @Test
  @DisplayName("질문 생성")
  void create() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT
    );

    mockAuthorFound();

    // when
    QuestionDto createdQuestionDto = questionService.create(QUESTION_AUTHOR_ID, questionCreateDto);

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
        QUESTION_CONTENT
    );
    mockAuthorFound();
    QuestionDto createdQuestionDto = questionService.create(QUESTION_AUTHOR_ID, questionCreateDto);

    QuestionUpdateDto questionUpdateDto = QuestionUpdateDto.from(
        "수정한 제목",
        "수정한 내용"
    );

    // when
    questionService.update(createdQuestionDto.getId(), questionUpdateDto);

    // then
    em.flush();
    em.clear();
    QuestionDto updatedQuestionDto = questionService.get(createdQuestionDto.getId());

    assertEquals(createdQuestionDto.getId(), updatedQuestionDto.getId());
    assertNotEquals(createdQuestionDto.getSubject(), updatedQuestionDto.getSubject());
    assertNotEquals(createdQuestionDto.getContent(), updatedQuestionDto.getContent());
    assertNotEquals(updatedQuestionDto.getEditedAt(), updatedQuestionDto.getCreatedAt());
  }

  @Test
  @DisplayName("질문 삭제")
  void delete() {
    // given
    QuestionCreateDto questionCreateDto = QuestionCreateDto.from(
        QUESTION_SUBJECT,
        QUESTION_CONTENT
    );
    mockAuthorFound();
    QuestionDto createdQuestionDto = questionService.create(QUESTION_AUTHOR_ID, questionCreateDto);

    // when
    questionService.delete(createdQuestionDto.getId());

    // then
    assertThrows(EntityNotFoundException.class,
        () -> questionService.get(createdQuestionDto.getId()));
    assertThrows(EntityNotFoundException.class,
        () -> questionService.delete(createdQuestionDto.getId()));
  }
}
