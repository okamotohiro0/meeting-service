package jp.kobe_u.cs27.zoomMei.controller.errorhandler;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import jp.kobe_u.cs27.zoomMei.configuration.exception.Response;
import jp.kobe_u.cs27.zoomMei.configuration.exception.ResponseCreater;
import jp.kobe_u.cs27.zoomMei.configuration.exception.UserValidationException;

import static jp.kobe_u.cs27.zoomMei.configuration.exception.ErrorCode.*;



@RestControllerAdvice("jp.kobe_u.cs27.zoomMei.controller.rest")
public class RestControllerErrorHandler {
      /*
   * UserValidationExceptionをBAD_REQUESTとして処理する
   */
  @ExceptionHandler(UserValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<Object> hanleBadRequestException(UserValidationException ex) {

    return ResponseCreater.fail(
        ex.getCode(),
        ex,
        null);
  }

    /*
   * コントローラーのバリデーションに違反したエラーをBAD_REQUESTとして処理する
   */
  @ExceptionHandler({ConstraintViolationException.class,
    MethodArgumentNotValidException.class})
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Response<Object> handleValidException(Exception ex) {

  return ResponseCreater.fail(
      CONTROLLER_VALIDATION_ERROR,
      ex,
      null);
}

  /*
   * コントローラーで受け取れない不正なリクエストをBAD_REQUESTとして処理する
   */
  @ExceptionHandler({HttpMessageNotReadableException.class,
    HttpRequestMethodNotSupportedException.class})
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Response<Object> handleConrollerException(Exception ex) {

  return ResponseCreater.fail(
      CONTROLLER_REJECTED,
      ex,
      null);
}

  /*
   * その他のエラーを重大な未定義エラーとみなしINTERNAL_SERVER_ERRORとして処理する
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response<Object> handleOtherException(Exception ex) {

    return ResponseCreater.fail(
        OTHER_ERROR,
        ex,
        null);
  }


}
