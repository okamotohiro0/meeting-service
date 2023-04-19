package jp.kobe_u.cs27.zoomMei.configuration.exception;

import lombok.Getter;

@Getter
public class UserValidationException extends RuntimeException{
  
    private final static long serialVersionUID = 1L;
  private final ErrorCode code;

  /**
   * 例外を生成するコンストラクタ
   *
   * @param code エラーコード
   * @param error 発生したエラーの内容を説明する文字列
   * @param cause 発生したエラーの原因を説明する文字列
   */
  public UserValidationException(
      ErrorCode code,
      String error,
      String cause) {

    super(String.format(
        "fail to %s, because %s.",
        error,
        cause));

    this.code = code;
  }
}
