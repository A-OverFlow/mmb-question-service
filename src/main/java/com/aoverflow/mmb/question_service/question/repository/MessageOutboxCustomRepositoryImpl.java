package com.aoverflow.mmb.question_service.question.repository;

import static com.aoverflow.mmb.question_service.question.entity.QMessageOutbox.messageOutbox;

import com.aoverflow.mmb.question_service.question.entity.MessageOutbox;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageOutboxCustomRepositoryImpl implements MessageOutboxCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<MessageOutbox> findUnsentMessages() {
    return queryFactory
        .selectFrom(messageOutbox)
        .where(messageOutbox.sent.eq(false))
        .orderBy(messageOutbox.createdAt.asc())
        .fetch();
  }
}
