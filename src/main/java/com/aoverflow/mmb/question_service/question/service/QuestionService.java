package com.aoverflow.mmb.question_service.question.service;

import com.aoverflow.mmb.question_service.common.feignClients.MemberServiceClient;
import com.aoverflow.mmb.question_service.member.dto.MemberDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionCreateDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import com.aoverflow.mmb.question_service.question.entity.Question;
import com.aoverflow.mmb.question_service.question.repository.QuestionRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;
  private final MemberServiceClient memberServiceClient;

  /**
   * 질문 단건 조회
   */
  public QuestionDto get(Long id) {
    Question question = questionRepository.findById(id)
        .orElseThrow(EntityNotFoundException::new);

    return QuestionDto.from(question);
  }

  /**
   * 질문 전체 조회
   */
  public Page<QuestionDto> getAll(Pageable pageable) {
    return questionRepository.findAll(pageable)
        .map(QuestionDto::from);
  }

  /**
   * 질문 생성
   */
  @Transactional
  public QuestionDto create(Long authorId, QuestionCreateDto questionCreateDto) {
    MemberDto author;
    try {
      author = memberServiceClient.getName(authorId);
    } catch (FeignException.NotFound e) {
      throw new EntityNotFoundException("작성자를 찾을 수 없음. Id: " + authorId);
    }

    Question savedQuestion = questionRepository.save(Question.of(authorId, author.getName(), questionCreateDto));

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
