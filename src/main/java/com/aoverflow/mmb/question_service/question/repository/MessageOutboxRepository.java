package com.aoverflow.mmb.question_service.question.repository;

import com.aoverflow.mmb.question_service.question.entity.MessageOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageOutboxRepository extends JpaRepository<MessageOutbox, Long>, MessageOutboxCustomRepository {

}
