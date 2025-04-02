package com.aoverflow.mmb.question_service.question.entity;

import com.aoverflow.mmb.question_service.answer.entity.Answer;
import com.aoverflow.mmb.question_service.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Question extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "question_id")
  private Long id;

  private String subject;
  private String content;
  private String author;

  @Enumerated(EnumType.STRING)
  private QuestionStatus status;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Answer> answers = new ArrayList<>();

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuestionHashtag> questionHashtags = new ArrayList<>();

  public void setStatus(QuestionStatus status) {
    this.status = status;
  }

  public void addAnswer(Answer answer) {
    this.answers.add(answer);
    answer.setQuestion(this);
  }

  public void addQuestionHashtag(QuestionHashtag questionHashtag) {
    this.questionHashtags.add(questionHashtag);
    questionHashtag.setQuestion(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Question other)) {
      return false;
    }
    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return id != null ? id.intValue() : 0;
  }
}
