package com.aoverflow.mmb.question_service.answer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emotion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "emotion_id")
  private Long id;

  private String author;

  @Enumerated(EnumType.STRING)
  private Emoji emoji;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "answer_id")
  private Answer answer;

  public void setAnswer(Answer answer) {
    this.answer = answer;
  }
}
