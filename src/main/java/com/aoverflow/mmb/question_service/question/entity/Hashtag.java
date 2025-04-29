package com.aoverflow.mmb.question_service.question.entity;

import com.aoverflow.mmb.question_service.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Hashtag extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "hashtag_id")
  private Long id;

  private String name;

  @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuestionHashtag> questionHashtags = new ArrayList<>();

  public void addQuestionHashtag(QuestionHashtag questionHashtag) {
    this.questionHashtags.add(questionHashtag);
    questionHashtag.setHashtag(this);
  }
}
