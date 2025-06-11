package com.aoverflow.mmb.question_service.question.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aoverflow.mmb.question_service.config.TestConfig;
import com.aoverflow.mmb.question_service.member.dto.MemberDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import com.aoverflow.mmb.question_service.question.entity.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
  private final static Long QUESTION_AUTHOR_ID = 1L;
  private final static String QUESTION_AUTHOR_NICKNAME = "더미 질문 작성자";
  private final static String QUESTION_AUTHOR_PICTURE = "images/profiles/01JWTWGSHTEM6QRN8VRMJN77NH";

  private void saveList() {
    questionRepository.deleteAll();

    MemberDto member1 = MemberDto.builder()
        .id(QUESTION_AUTHOR_ID)
        .nickname("영희")
        .build();
    MemberDto member2 = MemberDto.builder()
        .id(2L)
        .nickname("미애")
        .build();

    // member1: 5개 질문
    for (int i = 1; i <= 5; i++) {
      questionRepository.save(Question.of(
              QUESTION_SUBJECT + i,
              QUESTION_CONTENT + i,
              member1.getId(),
              member1.getNickname(),
              member1.getPicture()
          )
      );
    }
    // member2: 3개 질문
    for (int i = 6; i <= 8; i++) {
      questionRepository.save(Question.of(
              QUESTION_SUBJECT + i,
              QUESTION_CONTENT + i,
              member2.getId(),
              member2.getNickname(),
              member2.getPicture()
          )
      );
    }
  }

  @Test
  public void 질문_목록_조회__첫_페이지는_가장_최신_목록을_조회한다() {
    // given
    saveList();

    // when
    List<Question> questions = questionRepository.findByPageAndFiltersOrderByIdDesc(null, 5, null);

    // then
    assertEquals(5, questions.size());
    assertEquals(questions.getFirst().getId(), 8); // 가장 최신 id
  }

  @Test
  public void 질문_목록_조회__lastId가_주어지면_그보다_이전의_목록을_조회한다() {
    // given
    saveList();

    // when
    List<Question> questions = questionRepository.findByPageAndFiltersOrderByIdDesc(6L, 5, null);

    // then
    assertTrue(questions.stream().allMatch(question -> question.getId() < 6));
  }

  @Test
  public void 질문_목록_조회__authorId가_주어지면_그_작성자의_질문만_조회한다() {
    // given
    saveList();

    // when
    List<Question> questions = questionRepository.findByPageAndFiltersOrderByIdDesc(null, 10, QUESTION_AUTHOR_ID);

    // then
    assertEquals(5, questions.size());
    assertTrue(questions.stream().allMatch(question -> question.getAuthorId().equals(QUESTION_AUTHOR_ID)));
  }

  @Test
  public void 질문_생성() {
    // given
    Question question = Question.of("질문 있습니다!", "이게 질문입니다.", 123L, "images/profiles/01JWTWGSHTEM6QRN8VRMJN77NH", "작성자");

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
    Question question = Question.of(QUESTION_SUBJECT, QUESTION_CONTENT, QUESTION_AUTHOR_ID, QUESTION_AUTHOR_NICKNAME, QUESTION_AUTHOR_PICTURE);
    Question savedQuestion = questionRepository.save(question);
    String subject = savedQuestion.getSubject();

    // when
    question.update(QuestionUpdateDto.from("수정한 제목", QUESTION_CONTENT));
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
    Question question = Question.of(QUESTION_SUBJECT, QUESTION_CONTENT, QUESTION_AUTHOR_ID, QUESTION_AUTHOR_NICKNAME, QUESTION_AUTHOR_PICTURE);
    Question savedQuestion = questionRepository.save(question);
    String content = savedQuestion.getContent();

    // when
    question.update(QuestionUpdateDto.from(QUESTION_SUBJECT, "수정한 내용"));
    questionRepository.save(question);

    // then
    em.flush();
    em.clear();

    Question foundQuestion = questionRepository.findById(question.getId())
        .orElseThrow(EntityNotFoundException::new);

    assertNotEquals(content, foundQuestion.getContent());
    assertTrue(foundQuestion.getEditedAt().isAfter(foundQuestion.getCreatedAt()));
  }

  @Test
  public void 질문_개수_조회() {
    // given
    saveList();

    // when
    Long countMember1 = questionRepository.countByAuthorId(QUESTION_AUTHOR_ID);
    Long countMember2 = questionRepository.countByAuthorId(2L);
    Long countAll = questionRepository.countByAuthorId(null);

    // then
    assertEquals(5, countMember1);
    assertEquals(3, countMember2);
    assertEquals(8, countAll);
  }
}
