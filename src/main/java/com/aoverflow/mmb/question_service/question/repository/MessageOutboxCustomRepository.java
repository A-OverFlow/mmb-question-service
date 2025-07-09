package com.aoverflow.mmb.question_service.question.repository;

import com.aoverflow.mmb.question_service.question.entity.MessageOutbox;
import java.util.List;

public interface MessageOutboxCustomRepository {

  List<MessageOutbox> findUnsentMessages();
}
