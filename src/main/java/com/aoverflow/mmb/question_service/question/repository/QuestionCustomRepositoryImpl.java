package com.aoverflow.mmb.question_service.question.repository;

import static com.aoverflow.mmb.question_service.answer.entity.QAnswer.answer;
import static com.aoverflow.mmb.question_service.question.entity.QHashtag.hashtag;
import static com.aoverflow.mmb.question_service.question.entity.QQuestion.question;
import static com.aoverflow.mmb.question_service.question.entity.QQuestionHashtag.questionHashtag;

import com.aoverflow.mmb.question_service.question.entity.Question;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Question> findByPageAndFiltersOrderByIdDesc(Long id, int size, Long authorId) {
    BooleanBuilder builder = new BooleanBuilder();

    if (id != null) {
      builder.and(question.id.lt(id));
    }

    if (authorId != null) {
      builder.and(question.authorId.eq(authorId));
    }

    return queryFactory
        .selectFrom(question)
        .where(builder)
        .orderBy(question.id.desc())
        .limit(size)
        .fetch();
  }

  @Override
  public List<Question> findByAnswerAuthor(String author) {
    return queryFactory
        .selectFrom(question)
        .leftJoin(answer)
        .fetchJoin()
        .on(question.eq(answer.question))
        .where(answer.author.eq(author))
        .fetch();
  }

  @Override
  public List<Question> findByAnswerContent(String content) {
    return queryFactory
        .selectFrom(question)
        .leftJoin(answer)
        .fetchJoin()
        .on(question.eq(answer.question))
        .where(answer.content.eq(content))
        .fetch();
  }

  @Override
  public List<Question> findByHashtagName(String name) {
    return queryFactory
        .selectFrom(question)
        .leftJoin(questionHashtag)
        .fetchJoin()
        .on(question.eq(questionHashtag.question))
        .leftJoin(hashtag)
        .fetchJoin()
        .on(questionHashtag.hashtag.eq(hashtag))
        .where(hashtag.name.eq(name))
        .fetch();
  }

  @Override
  public Long countByAuthorId(Long authorId) {
    BooleanBuilder builder = new BooleanBuilder();

    if (authorId != null) {
      builder.and(question.authorId.eq(authorId));
    }

    return queryFactory
        .select(question.count())
        .from(question)
        .where(builder)
        .fetchOne();
  }
}
