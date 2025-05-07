package com.aoverflow.mmb.question_service.question.controller;

import com.aoverflow.mmb.question_service.question.dto.QuestionCreateDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionDto;
import com.aoverflow.mmb.question_service.question.dto.QuestionUpdateDto;
import com.aoverflow.mmb.question_service.question.service.QuestionService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService;

  @GetMapping("/{id}")
  public ResponseEntity<QuestionDto> get(@PathVariable("id") Long id) {
    return ResponseEntity.ok(questionService.get(id));
  }

  @GetMapping
  public ResponseEntity<Page<QuestionDto>> getAll(
      @PageableDefault(
          page = 0,
          size = 10,
          sort = {"createdAt", "id"},
          direction = Direction.DESC
      ) Pageable pageable
  ) {
    return ResponseEntity.ok(questionService.getAll(pageable));
  }

  @PostMapping
  public ResponseEntity<QuestionDto> create(@Valid @RequestBody QuestionCreateDto questionCreateDto) {
    QuestionDto createdQuestionDto = questionService.create(questionCreateDto);
    return ResponseEntity.created(URI.create("/questions/" + createdQuestionDto.getId())).body(createdQuestionDto);
  }

  @PutMapping
  public ResponseEntity<QuestionDto> update(@Valid @RequestBody QuestionUpdateDto questionUpdateDto) {
    return ResponseEntity.ok(questionService.update(questionUpdateDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    questionService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
