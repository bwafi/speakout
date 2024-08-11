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
  
  
  public static <T> WebResponse<T> success(Integer statusCode, String message, T data) {
    return WebResponse.<T>builder()
        .message(message)
        .data(data)
        .statusCode(statusCode)
        .build();
  }
  
  public static <T> WebResponse<T> error(Integer statusCode, String message) {
    return WebResponse.<T>builder()
        .statusCode(statusCode)
        .message(message)
        .build();
  }
}
