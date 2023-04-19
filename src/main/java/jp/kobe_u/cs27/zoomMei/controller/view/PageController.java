package jp.kobe_u.cs27.zoomMei.controller.view;



import jp.kobe_u.cs27.zoomMei.domain.service.UserService;
import jp.kobe_u.cs27.zoomMei.form.UserForm;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequiredArgsConstructor
@Controller
public class PageController {
  private final UserService uservice;
  @GetMapping("/")
  public String showLandingPage(){
    return "index";
  }

  @GetMapping("/user/signup")
  public String showUserRegistrationPage(Model model){

    // UserFormをModelに追加する(Thymeleaf上ではuserForm)
    model.addAttribute(new UserForm());

    return "register";
  }
  
  @GetMapping("/{uid}/zoommei")
  public String showMeetingInputPage(Model model, @PathVariable String uid){

    // HealthConditionFormをModelに追加する(Thymeleaf上ではhealthConditionForm)

    String nickname=uservice.getUser(uid).getNickname();

    model.addAttribute("nickname",nickname);
    return "menu";
  }
   


}