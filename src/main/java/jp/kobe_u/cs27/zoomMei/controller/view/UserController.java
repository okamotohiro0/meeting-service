package jp.kobe_u.cs27.zoomMei.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.kobe_u.cs27.zoomMei.configuration.exception.UserValidationException;
import jp.kobe_u.cs27.zoomMei.form.MeetingForm;
import jp.kobe_u.cs27.zoomMei.form.UidForm;
import jp.kobe_u.cs27.zoomMei.form.UserForm;
import jp.kobe_u.cs27.zoomMei.domain.service.UserService;
import jp.kobe_u.cs27.zoomMei.domain.entity.User;
import lombok.RequiredArgsConstructor;


/**
 * ユーザ操作を行うコントローラークラス
 * Thymeleafに渡す値をModelに登録し、ページを表示する
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
      /**
   * ユーザIDを指定して必要な情報を取得し、会議登録済ページに入る
   * 未登録のユーザIDが指定された場合、初期ページに移動する
   *
   * @param model
   * @param attributes
   * @param form  ユーザID
   * @return 会議登録済ページ
   */
  @GetMapping("/user/enter")
  public String confirmUserExistence(
      Model model,
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          UidForm form,
      BindingResult bindingResult) {

    // ユーザIDに使用できない文字が含まれていた場合
    if (bindingResult.hasErrors()) {
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUidValidationError",
          true);

      // 初期画面に戻る
      return "redirect:/";
    }

    // ユーザIDを変数に格納する
    final String uid = form.getUid();
    final String password=form.getPassword();

    


    // ユーザIDからニックネームを取得する
    // ユーザが登録済みかどうかの確認も兼ねている
    try {
     service.getUser(uid);
    } catch (UserValidationException e) { // ユーザIDが未登録であった場合
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserDoesNotExistError",
          true);
      // 初期ページに戻る
      return "redirect:/";
    }

    if(!(password.equals(service.getUser(uid).getPassword()))){
            // エラーフラグをオンにする
            attributes.addFlashAttribute(
              "isUserDoesNotExistError",
              true);
          // 初期ページに戻る
          return "redirect:/";
    }

    

    // // ユーザIDとニックネームをModelに追加する
    //     model.addAttribute(
    //     "userInfo",
    //     UserInfo.build(user));


    // 体調入力ページ
    return "redirect:/"+uid+"/zoommei";
    // return "menu";
  }

    /**
   * ユーザの情報を取得し、確認画面を表示する
   *
   * @param model
   * @param attributes
   * @param form  ユーザID
   * @return ユーザ情報確認ページ
   */
  @GetMapping("/user/{uid}/information")
  public String searchUserInformation(
      Model model,
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          UidForm form,
      BindingResult bindingResult,@PathVariable String uid) {

    // ユーザ情報をDBから取得する
    // ユーザが登録済みかどうかの確認も兼ねている
    User user;
    try {
      user = service.getUser(uid);
    } catch (UserValidationException e) {
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserDoesNotExistError",
          true);
      // 初期ページに戻る
      return "redirect:/";
    }

    // ユーザ情報をModelに登録する
    model.addAttribute(
        "uid",
        uid);
    model.addAttribute(
        "nickname",
        user.getNickname());
    model.addAttribute(
        "password",
        user.getPassword());

    // ユーザ情報確認ページ
    return "information";
  }



    /**
   * ユーザを登録する
   *
   * @param model
   * @param attributes
   * @param form  UserForm
   * @return 体調記録ページ
   */
  @PostMapping("/user/{uid}/register")
  public String registerUser(
      Model model,
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          UserForm form,
      BindingResult bindingResult) {

    // フォームにバリデーション違反があった場合
    if (bindingResult.hasErrors()) {
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserFormError",
          true);

      // ユーザ登録ページ
      return "redirect:/user/signup";
    }

    // ユーザを登録する
    try {
      service.createUser(form);
    } catch (UserValidationException e) {
      // ユーザが登録済みであった場合
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserAlreadyExistsError",
          true);

      // ユーザ登録ページ
      return "redirect:/user/signup";
    }

    // ユーザIDとニックネームをModelに登録する
    model.addAttribute(
        "uid",
        form.getUid());
    model.addAttribute(
        "nickname",
        form.getNickname());

    // MeetingFormをModelに追加する(Thymeleaf上ではhealthConditionForm)
    model.addAttribute(new MeetingForm());

    // 体調記録ページ
    return "menu";
  }

    /**
   * ユーザ登録が可能か確認する
   * ユーザが登録済みであった場合、ユーザ登録ページに戻る
   *
   * @param model
   * @param attributes
   * @param form  UserForm
   * @return ユーザ登録確認ページ
   */
  @GetMapping("/user/register/confirm")
  public String confirmUserRegistration(
      Model model,
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          UserForm form,
      BindingResult bindingResult) {

      System.out.println(form);

    // フォームにバリデーション違反があった場合
    if (bindingResult.hasErrors()) {
      // エラーフラグをオンにする
      System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      attributes.addFlashAttribute(
          "isUserFormError",
          true);

      // ユーザ登録ページ
      return "redirect:/user/signup";
    }

    // ユーザIDを変数に格納する
    final String uid = form.getUid();

    final String nickname=form.getNickname();

    // ユーザが既に存在するか確認する
    if (service.existsUser(uid)) {
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserAlreadyExistsError",
          true);

      // ユーザ登録ページに戻る
      return "redirect:/user/signup";
    }

    //ユーザのニックネームが存在するか確認する
    if(service.existsNickname(nickname)){
      attributes.addFlashAttribute(
        "isUserAlreadyExistsError",
        true);

    // ユーザ登録ページに戻る
    return "redirect:/user/signup";
    }

    // ユーザ情報をModelに追加する
    model.addAttribute(
        "uid",
        uid);
    model.addAttribute(
        "nickname",
        nickname);
    model.addAttribute(
        "password",
        form.getPassword());

    // ユーザ登録確認ページ
    return "confirmRegistration";
  }

  /**
   * ユーザ情報を更新する
   *
   * @param model
   * @param attributes
   * @param form  UserForm
   * @return 体調記録ページ
   */
  @PostMapping("/user/{uid}/update")
  public String updateUser(
      Model model,
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          UserForm form,
      BindingResult bindingResult,@PathVariable String uid) {

    // フォームにバリデーション違反があった場合
    if (bindingResult.hasErrors()) {
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserFormError",
          true);

      // リダイレクト先の引数としてユーザIDを渡す
      attributes.addAttribute(
          "uid",
          form.getUid());

      // ユーザ情報取得メソッドにリダイレクトする
      return "redirect:/user/" + uid + "/information";
    }

    // ユーザ情報を更新する
    try {
      service.updateUser(form);
    } catch (UserValidationException e) {
      // ユーザが存在しない場合
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserDoesNotExistError",
          true);
      // 初期ページに戻る
      return "redirect:/";
    }

    // ユーザIDとニックネームをModelに追加する
    model.addAttribute(
        "uid",
        form.getUid());
    model.addAttribute(
        "nickname",
        form.getNickname());

    // 体調記録ページ
    return "menu";
  }

  /**
   * ユーザ情報更新が可能か確認する
   *
   * @param model
   * @param attributes
   * @param form  UserForm
   * @return ユーザ情報更新確認ページ
   */
  @GetMapping("/user/{uid}/update/confirm")
  public String confirmUserUpdate(
      Model model,
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          UserForm form,
      BindingResult bindingResult, @PathVariable String uid) {

    // フォームにバリデーション違反があった場合
    if (bindingResult.hasErrors()) {
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserFormError",
          true);

      // リダイレクト先の引数としてユーザIDを渡す
      attributes.addAttribute(
          "uid",
          form.getUid());

      // ユーザ情報取得メソッドにリダイレクトする
      return "redirect:/user/" + uid + "/information";
    }

    // ユーザが登録済みか確認する
    if (!service.existsUser(uid)) {
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserAlreadyExistsError",
          true);

      // リダイレクト先の引数としてユーザIDを渡す
      attributes.addAttribute(
          "uid",
          form.getUid());

      // ユーザ情報取得メソッドにリダイレクトする
      return "redirect:/user/" + uid + "/information";
    }

    // ユーザ情報をModelに追加する
    model.addAttribute(
        "uid",
        uid);
    model.addAttribute(
        "nickname",
        form.getNickname());
    model.addAttribute(
        "password",
        form.getPassword());

    // ユーザ情報更新確認ページ
    return "confirmUpdate";
  }







}

