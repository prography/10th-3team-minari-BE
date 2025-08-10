package com.prography.minari.common.execption;

public class TossApiException extends RuntimeException {
    private final String code;
    private final String message;

    public TossApiException(String code, String message) {
        this.code = code;
        this.message = message;
    }

  public String getCode() {
      return code;
  }

  public String getMessage() {
      return message;
  }
}
