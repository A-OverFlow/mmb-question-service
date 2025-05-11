package com.aoverflow.mmb.question_service.question.service;

import com.aoverflow.mmb.question_service.question.dto.QuestionCreateDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import com.aoverflow.mmb.question_service.question.entity.Question;
import com.aoverflow.mmb.question_service.question.repository.QuestionRepository;
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
  public List<QuestionDto> getAll() {
    List<Question> questions = questionRepository.findAll();

    return questions.stream()
        .map(QuestionDto::from)
        .toList();
  }

  /**
   * 질문 생성
   */
  @Transactional
  public QuestionDto create(QuestionCreateDto questionCreateDto) {
    Question savedQuestion = questionRepository.save(Question.of(questionCreateDto));

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
    questionRepository.deleteById(id);
  }
}
