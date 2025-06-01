package com.aoverflow.mmb.question_service.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDto {

  private String name;
  private String email;
  private String picture;
}
