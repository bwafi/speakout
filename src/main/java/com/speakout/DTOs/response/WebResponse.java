package com.speakout.DTOs.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WebResponse<T> {
  private Integer statusCode;
  private String message;
  private T data;
  private PagingResponse pagingResponse;
}
