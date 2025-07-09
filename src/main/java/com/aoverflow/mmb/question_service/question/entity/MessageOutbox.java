package com.aoverflow.mmb.question_service.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MessageOutbox {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_outbox_id")
  private Long id;

  private String topic;

  @Lob
  private String payload;

  private boolean sent = false;

  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder
  public MessageOutbox(String topic, String payload, boolean sent, LocalDateTime createdAt) {
    this.topic = topic;
    this.payload = payload;
    this.sent = sent;
    this.createdAt = createdAt;
  }

  public static MessageOutbox of(String topic, String payload, boolean sent, LocalDateTime createdAt) {
    return MessageOutbox.builder()
        .topic(topic)
        .payload(payload)
        .sent(sent)
        .createdAt(createdAt)
        .build();
  }
}
