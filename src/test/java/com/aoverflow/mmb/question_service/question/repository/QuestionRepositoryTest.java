package com.aoverflow.mmb.question_service.question.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aoverflow.mmb.question_service.config.TestConfig;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import com.aoverflow.mmb.question_service.question.entity.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private EntityManager em;

  private final static String QUESTION_SUBJECT = "더미 질문 제목";
  private final static String QUESTION_CONTENT = "더미 질문 내용";
  private final static Long QUESTION_AUTHOR_ID = 123L;
  private final static String QUESTION_AUTHOR_NAME = "더미 질문 작성자";

  @Test
  public void 질문_생성() {
    // given
    Question question = Question.of("질문 있습니다!", "이게 질문입니다.", 123L, "작성자");

    // when
    Question savedQuestion = questionRepository.save(question);

    // then
    em.flush();
    em.clear();

    assertEquals(question.getSubject(), savedQuestion.getSubject());
    assertEquals(question.getContent(), savedQuestion.getContent());
    assertNotNull(savedQuestion.getCreatedAt());
    assertNotNull(savedQuestion.getEditedAt());
  }

  @Test
  public void 질문_제목_수정() {
    // given
    Question question = Question.of(QUESTION_SUBJECT, QUESTION_CONTENT, QUESTION_AUTHOR_ID, QUESTION_AUTHOR_NAME);
    Question savedQuestion = questionRepository.save(question);
    String subject = savedQuestion.getSubject();

    // when
    question.update(QuestionUpdateDto.from(savedQuestion.getId(), "수정한 제목", QUESTION_CONTENT));
    questionRepository.save(question);

    // then
    em.flush();
    em.clear();

    Question foundQuestion = questionRepository.findById(question.getId())
        .orElseThrow(EntityNotFoundException::new);

    assertNotEquals(subject, foundQuestion.getSubject());
    assertTrue(foundQuestion.getEditedAt().isAfter(foundQuestion.getCreatedAt()));
  }

  @Test
  public void 질문_내용_수정() {
    // given
    Question question = Question.of(QUESTION_SUBJECT, QUESTION_CONTENT, QUESTION_AUTHOR_ID, QUESTION_AUTHOR_NAME);
    Question savedQuestion = questionRepository.save(question);
    String content = savedQuestion.getContent();

    // when
    question.update(QuestionUpdateDto.from(savedQuestion.getId(), QUESTION_SUBJECT, "수정한 내용"));
    questionRepository.save(question);

    // then
    em.flush();
    em.clear();

    Question foundQuestion = questionRepository.findById(question.getId())
        .orElseThrow(EntityNotFoundException::new);

    assertNotEquals(content, foundQuestion.getContent());
    assertTrue(foundQuestion.getEditedAt().isAfter(foundQuestion.getCreatedAt()));
  }
}
