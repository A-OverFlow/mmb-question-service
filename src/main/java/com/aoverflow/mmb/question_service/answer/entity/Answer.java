package com.aoverflow.mmb.question_service.answer.entity;

import com.aoverflow.mmb.question_service.common.entity.BaseEntity;
import com.aoverflow.mmb.question_service.question.entity.Question;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "answer_id")
  private Long id;

  private String content;
  private String author;

  @Enumerated(EnumType.STRING)
  private AnswerStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id")
  private Question question;

  @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Emotion> emotions = new ArrayList<>();

  public void setStatus(AnswerStatus status) {
    this.status = status;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
    comment.setAnswer(this);
  }

  public void addEmotion(Emotion emotion) {
    this.emotions.add(emotion);
    emotion.setAnswer(this);
  }

  public void removeEmotion(Emotion emotion) {
    this.emotions.remove(emotion);
    emotion.setAnswer(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Answer other)) {
      return false;
    }
    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return id != null ? id.intValue() : 0;
  }
}
