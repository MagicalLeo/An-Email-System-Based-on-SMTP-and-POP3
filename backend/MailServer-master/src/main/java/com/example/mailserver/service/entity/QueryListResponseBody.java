package com.example.mailserver.service.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class QueryListResponseBody <T>{
  private Integer total;
  private List items;

  @Tolerate
  QueryListResponseBody() {
  }
}
