package com.aoverflow.mmb.question_service.question.repository;

import com.aoverflow.mmb.question_service.question.entity.Question;
import java.util.List;

public interface QuestionCustomRepository {

  List<Question> findByAnswerAuthor(String author);

  List<Question> findByAnswerContent(String content);

  List<Question> findByHashtagName(String name);
}
