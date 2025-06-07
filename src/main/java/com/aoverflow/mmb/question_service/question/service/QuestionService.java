package com.aoverflow.mmb.question_service.question.service;

import com.aoverflow.mmb.question_service.common.feignClients.MemberServiceClient;
import com.aoverflow.mmb.question_service.member.dto.MemberDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionCreateDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionPageDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import com.aoverflow.mmb.question_service.question.entity.Question;
import com.aoverflow.mmb.question_service.question.repository.QuestionRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;
  private final MemberServiceClient memberServiceClient;

  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int MAX_PAGE_SIZE = 1000;

  /**
   * 질문 단건 조회
   */
  public QuestionDto get(Long id) {
    Question question = questionRepository.findById(id)
        .orElseThrow(EntityNotFoundException::new);

    return QuestionDto.from(question);
  }

  /**
   * 질문 목록 조회
   */
  public QuestionPageDto<QuestionDto> getPaginatedList(Long id, int size) {
    int validSize = (size <= 0) ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);

    List<Question> questions = questionRepository.findAfterIdOrderByIdDesc(id, validSize + 1);  // hasNext 판단을 위해 +1 하기.

    boolean hasNext = questions.size() > validSize;
    if (hasNext) {
      questions.remove(validSize);
    }

    List<QuestionDto> questionDtoList = questions.stream()
        .map(QuestionDto::from)
        .toList();

    Long lastId = null;
    if (!questions.isEmpty()) {
      lastId = questions.getLast().getId();
    }

    Long totalElements = questionRepository.count();

    return QuestionPageDto.<QuestionDto>builder()
        .questions(questionDtoList)
        .hasNext(hasNext)
        .lastId(lastId)
        .totalElements(totalElements)
        .pageSize(questions.size())
        .build();
  }

  /**
   * 질문 생성
   */
  @Transactional
  public QuestionDto create(Long authorId, QuestionCreateDto questionCreateDto) {
    MemberDto author;
    try {
      author = memberServiceClient.getMember(authorId);
    } catch (FeignException.NotFound e) {
      throw new EntityNotFoundException("작성자를 찾을 수 없음. Id: " + authorId);
    }

    Question savedQuestion = questionRepository.save(Question.of(authorId, author.getNickname(), questionCreateDto));

    return QuestionDto.from(savedQuestion);
  }

  /**
   * 질문 수정
   */
  @Transactional
  public QuestionDto update(QuestionUpdateDto questionUpdateDto) {
    Question question = questionRepository.findById(questionUpdateDto.getId())
        .orElseThrow(EntityNotFoundException::new);

    question.update(questionUpdateDto);

    return QuestionDto.from(question);
  }

  /**
   * 질문 삭제
   */
  @Transactional
  public void delete(Long id) {
    questionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

    questionRepository.deleteById(id);
  }
}
