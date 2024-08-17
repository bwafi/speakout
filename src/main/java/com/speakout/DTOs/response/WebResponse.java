package com.speakout.DTOs.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Builder
@Data
public class WebResponse<T> {
  private Integer statusCode;
  private String message;
  private T data;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Optional<PagingResponse> pagingResponse;
  
  
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
