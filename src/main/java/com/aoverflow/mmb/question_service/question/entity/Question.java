package com.aoverflow.mmb.question_service.question.entity;

import com.aoverflow.mmb.question_service.answer.entity.Answer;
import com.aoverflow.mmb.question_service.common.entity.BaseEntity;
import com.aoverflow.mmb.question_service.question.dto.QuestionCreateDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Question extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "question_id")
  private Long id;

  private String subject;
  private String content;

  private Long authorId;
  private String authorName;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Answer> answers = new ArrayList<>();

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuestionHashtag> questionHashtags = new ArrayList<>();

  // ==연관관계 메서드== //
  public void addAnswer(Answer answer) {
    this.answers.add(answer);
    answer.setQuestion(this);
  }

  public void addQuestionHashtag(QuestionHashtag questionHashtag) {
    this.questionHashtags.add(questionHashtag);
    questionHashtag.setQuestion(this);
  }

  public void update(QuestionUpdateDto questionUpdateDto) {
    this.subject = questionUpdateDto.getSubject();
    this.content = questionUpdateDto.getContent();
  }

  @Builder
  public Question(String subject, String content, Long authorId, String authorName) {
    this.subject = subject;
    this.content = content;
    this.authorId = authorId;
    this.authorName = authorName;
  }

  public static Question of(String subject, String content, Long authorId, String authorName) {
    return Question.builder()
        .subject(subject)
        .content(content)
        .authorId(authorId)
        .authorName(authorName)
        .build();
  }

  public static Question of(Long authorId, String authorName, QuestionCreateDto questionCreateDto) {
    return Question.builder()
        .subject(questionCreateDto.getSubject())
        .content(questionCreateDto.getContent())
        .authorId(authorId)
        .authorName(authorName)
        .build();
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
