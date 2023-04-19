package jp.kobe_u.cs27.zoomMei.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import jp.kobe_u.cs27.zoomMei.domain.service.FriendService;
import jp.kobe_u.cs27.zoomMei.domain.service.UserService;
import jp.kobe_u.cs27.zoomMei.form.FriendForm;
import lombok.RequiredArgsConstructor;

/**
 * フォロー・フォロワーを行うコントローラークラス
 * Thymeleafに渡す値をModelに登録し、ページの表示先にリダイレクトする
 */
@Controller
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

//     /**
//    * フレンド登録が可能か確認する
//    * ユーザが登録済みであった場合、ユーザ登録ページに戻る
//    *
//    * @param model
//    * @param attributes
//    * @param form  UserForm
//    * @return ユーザ登録確認ページ
//    */
//   @GetMapping("/friend/input/confirm")
//   public String confirmInputFriend(
//       Model model,
//       RedirectAttributes attributes,
//       @ModelAttribute
//       @Validated
//           FriendForm form,
//       BindingResult bindingResult) {

//       System.out.println(form);

//     // フォームにバリデーション違反があった場合
//     if (bindingResult.hasErrors()) {
//         // エラーフラグをオンにする
//         System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//         attributes.addFlashAttribute(
//             "isFriendFormError",
//             true);
  
//         // ユーザ登録ページ
//         return "redirect:/user/signup";
//       }
  
//       // ユーザIDを変数に格納する
//       final String fid = form.getFid();
//       final String uid = form.getUid();

  
//       // ユーザが既に登録済みでないか確認する
//       if (!userService.existsUser(fid)) {
//         // エラーフラグをオンにする
//         attributes.addFlashAttribute(
//             "isUserNotExistsError",
//             true);
  
//         // ユーザ登録ページに戻る
//         return "redirect:/friend";
//       }

//      // フォローする人が自分かどうか確認する 
//      if(fid==uid){
//       // エラーフラグをオンにする
//       attributes.addFlashAttribute(
//           "isUserNotExistsError",
//           true);

//       // ユーザ登録ページに戻る
//       return "redirect:/user/signup";
// }
//     if(friendService.existsFollow(uid,fid)){
//         attributes.addFlashAttribute(
//             "isUserNotExistsError",
//             true);
  
//         // ユーザ登録ページに戻る
//         return "redirect:/friend";
//     }




//     // フォローするユーザ情報をModelに追加する
//     model.addAttribute(
//         "uid",
//         uid);
//     model.addAttribute(
//         "fid",
//         fid);
//     model.addAttribute(
//         "nickname",
//         userService.getUser(fid).getNickname());  
//     model.addAttribute(
//         "relationship",
//         form.getRelationship());

//     // ユーザ登録確認ページ
//     return "confirmFriendList";
//   }


/**
   * 体調を記録する
   *
   * @param attributes
   * @param form HealthConditionForm
   * @return 体調記録ページ
   */
  @PostMapping("/{uid}/friend/input")
  public String inputFriend(
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          FriendForm form,
      BindingResult bindingResult,@PathVariable String uid) {

    // ユーザIDに使用できない文字が含まれていた場合
    if(bindingResult.getFieldErrors().stream().anyMatch(it -> it.getField().equals("uid"))){
        // エラーフラグをオンにする
        attributes.addFlashAttribute(
            "isUidValidationError",
            true);
  
        // 初期画面に戻る
        return "redirect:/";
      }
  

      // ユーザIDを変数に格納する
    final String fid = form.getFid();

      // ユーザが既に登録済みでないか確認する
      if (!userService.existsUser(fid)) {
        // エラーフラグをオンにする
        attributes.addFlashAttribute(
            "isUserNotExistsError",
            true);
  
        // ユーザ登録ページに戻る
        return "redirect:/" + uid + "/friend";
      }

        // ユーザが既にフォロー済みかどうか確かめる
        if (friendService.existsFollow(uid,fid)) {
            // エラーフラグをオンにする
            attributes.addFlashAttribute(
                "isUserNotExistsError",
                true);
          
            // ユーザ登録ページに戻る
            return "redirect:/" + uid + "/friend";
              }



     // フォローする人が自分かどうか確認する 
     if(userService.getUser(fid)==userService.getUser(uid)){
      // エラーフラグをオンにする
      attributes.addFlashAttribute(
          "isUserNotExistsError",
          true);

      // ユーザ登録ページに戻る
      return "redirect:/" + uid + "/friend";
}
    
      
      // 体調を記録する
      friendService.record(form);
          // ユーザ情報を更新する




    // 友達リストページ
    return "redirect:/" + uid + "/friend";
  }




     /**
   * ユーザの情報を取得し、友達リスト画面を表示する
   *
   * @param model
   * @param attributes
   * @param form  ユーザID
   * @return ユーザ情報確認ページ
   */
  @GetMapping("/{uid}/friend")
  public String searchFriend(
      Model model,
      @PathVariable String uid) {




    // ユーザ情報をDBから取得する
    // ユーザが登録済みかどうかの確認も兼ねている
     


    // ユーザ情報をModelに登録する
    model.addAttribute(
        "uid",
        uid);
        
        // 体調を検索し、結果をModelに格納する
        model.addAttribute(
            "logs",
            friendService
            .query(uid)
                .getLogs());  
         
                model.addAttribute(
            "flogs",
            friendService
            .query(uid)
                .getFlogs());          



    // FriendFormをModelに追加する(Thymeleaf上ではfriendForm)
    model.addAttribute(new FriendForm());

// 体調振り返りページ
return "friendList";
}



}

