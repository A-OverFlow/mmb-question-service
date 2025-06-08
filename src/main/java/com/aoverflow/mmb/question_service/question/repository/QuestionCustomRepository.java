package com.aoverflow.mmb.question_service.question.repository;

import com.aoverflow.mmb.question_service.question.entity.Question;
import java.util.List;

public interface QuestionCustomRepository {

  List<Question> findByPageAndFiltersOrderByIdDesc(Long id, int size, Long authorId);

  List<Question> findByAnswerAuthor(String author);

  List<Question> findByAnswerContent(String content);

  List<Question> findByHashtagName(String name);

  Long countByAuthorId(Long authorId);
}
