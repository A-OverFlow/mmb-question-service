package com.aoverflow.mmb.question_service.common.feignClients;

import com.aoverflow.mmb.question_service.member.dto.MemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "member-service", url = "http://mmb-member-service:8082/api/v1")
public interface MemberServiceClient {

  @GetMapping("/members/me")
  MemberDto getName(@RequestHeader("X-User-Id") Long authorId);
}
