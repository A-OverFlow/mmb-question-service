package com.aoverflow.mmb.question_service.question.service;

import com.aoverflow.mmb.question_service.question.entity.MessageOutbox;
import com.aoverflow.mmb.question_service.question.repository.MessageOutboxRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagePublishingJob {

  private final MessageOutboxRepository messageOutboxRepository;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Scheduled(fixedDelay = 5000)
  public void publishMessages() {
    List<MessageOutbox> unsentMessages = messageOutboxRepository.findUnsentMessages();
    for (MessageOutbox message : unsentMessages) {
      try {
        kafkaTemplate.send(message.getTopic(), message.getPayload());
        message.setSent(true);
        messageOutboxRepository.save(message);
        System.out.println("✅ Kafka 메시지 발행됨: " + message.getTopic());
      } catch (Exception e) {
        System.err.println("❌ Kafka 메시지 발행 실패: " + e.getMessage());
      }
    }
  }
}
