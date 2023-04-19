package jp.kobe_u.cs27.zoomMei.controller.view;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.kobe_u.cs27.zoomMei.domain.entity.Friend;
import jp.kobe_u.cs27.zoomMei.domain.entity.User;
import jp.kobe_u.cs27.zoomMei.domain.repository.FriendRepository;
import jp.kobe_u.cs27.zoomMei.domain.repository.ZoomRepository;
import jp.kobe_u.cs27.zoomMei.domain.service.MeetingService;
import jp.kobe_u.cs27.zoomMei.domain.service.UserService;
import jp.kobe_u.cs27.zoomMei.form.ConductForm;
import jp.kobe_u.cs27.zoomMei.form.MeetingForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MeetingController {
  private final FriendRepository friendRepository;
  private final UserService userService;
  private final MeetingService meetingService;
  private final ZoomRepository zoomrepo;
  
    /**
   * 体調を記録する
   *
   * @param attributes
   * @param form HealthConditionForm
   * @return 体調記録ページ
   */
  @PostMapping("/{uid}/meeting/input")
  public String inputFriend(
      RedirectAttributes attributes,
      @ModelAttribute
      @Validated
          MeetingForm form,
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
        final List<String> pid=form.getPid();
        final LocalDateTime starttime=form.getStarttime();
        LocalDateTime ldt=LocalDateTime.now();
        
  
  
            // ユーザIDとニックネームをModelに追加する
            attributes.addFlashAttribute(
           "uid",
           uid);
            attributes.addFlashAttribute(
        "nickname",
              userService
             .getUser(uid)
             .getNickname());

      //招待者がまったくいない場合       
      if(pid.size()==0){
        attributes.addFlashAttribute(
          "isUserNotExistsError",
          true);

      // ユーザ登録ページに戻る
      return "redirect:/"+uid+"/meeting";
      }

            //同じ会議の時間があるかどうかを確認
            if(zoomrepo.existsByHostidAndStarttime(uid,starttime)){
              attributes.addFlashAttribute(
                "isUserNotExistsError",
                true);
      
            // ユーザ登録ページに戻る
            return "redirect:/"+uid+"/meeting";
            }

      //会議の時間が過去の場合
      if(ldt.isAfter(starttime)){
        attributes.addFlashAttribute(
          "isUserNotExistsError",
          true);

      // ユーザ登録ページに戻る
      return "redirect:/"+uid+"/meeting";
      }



      
      //uidの会議を登録する
      meetingService.record(form);  

      //相手の会議を登録する
      meetingService.pidrecord(form);




    // 友達リストページ
    return "redirect:/"+uid+"/meeting";
  }


    /**
   * ユーザの情報を取得し、友達リスト画面を表示する
   *
   * @param model
   * @param attributes
   * @param form  ユーザID
   * @return ユーザ情報確認ページ
   */
  @GetMapping("/{uid}/meeting")
  public String searchMeeting(
      Model model,
      User user,
      @PathVariable String uid) {
    // ユーザ情報をDBから取得する
    // ユーザが登録済みかどうかの確認も兼ねている
             //フレンドのリストから自分がuidのリストを拾ってくる。
             List<Friend> friends=(List<Friend>) friendRepository.findByUid(uid); 


             Map<String, String>  UserCheckBox= new LinkedHashMap<String , String>();
             for(Friend friend:friends){
               String fNickname=userService.getUser(friend.getFid()).getNickname();
               UserCheckBox.put(friend.getFid(),fNickname);
             }

             model.addAttribute("UserCheckBox",UserCheckBox);


    // ユーザ情報をModelに登録する
    model.addAttribute(
        "uid",
        uid);
    
    //会議情報を表にまとめる
    model.addAttribute(
      "meetingLogs",
      meetingService
      .put(user.getUid())
      .getMeetinglogs());

    model.addAttribute(
      "invitedLogs",
      meetingService
      .put(user.getUid())
      .getPidLogs()
    ) ; 
    model.addAttribute(
      "conductForm",
      new ConductForm()
    ) ; 
    

    
      
    


    // FriendFormをModelに追加する(Thymeleaf上ではfriendForm)
    model.addAttribute(new MeetingForm());

// 体調振り返りページ
return "meetingList";
}

@PostMapping("/{uid}/meeting/{groupid}/conduct")
public String conductMeeting(
RedirectAttributes attributes,
@ModelAttribute
@Validated
    ConductForm form,
BindingResult bindingResult,@PathVariable String uid,@PathVariable Long groupid
){



        
      meetingService.answer(form);
      // 友達リストページ
      return "redirect:/"+ uid +"/meeting";
}

}
