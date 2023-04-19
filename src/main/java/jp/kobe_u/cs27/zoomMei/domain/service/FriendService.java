package jp.kobe_u.cs27.zoomMei.domain.service;

import org.springframework.stereotype.Service;

import jp.kobe_u.cs27.zoomMei.configuration.exception.UserValidationException;
import jp.kobe_u.cs27.zoomMei.domain.dto.FollowerWithNickname;
import jp.kobe_u.cs27.zoomMei.domain.dto.FriendList;
import jp.kobe_u.cs27.zoomMei.domain.dto.FriendWithNickname;
import jp.kobe_u.cs27.zoomMei.domain.entity.Friend;
import jp.kobe_u.cs27.zoomMei.domain.repository.FriendRepository;
import jp.kobe_u.cs27.zoomMei.domain.repository.UserRepository;
import jp.kobe_u.cs27.zoomMei.form.FriendForm;
import lombok.RequiredArgsConstructor;

import static jp.kobe_u.cs27.zoomMei.configuration.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 友達リスト・フォロー機能を提供するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userrepo;
    private final FriendRepository friendrepo;
    private final UserService uservice;
 



   /**
   * 指定したユーザがフォローDBに登録済みかどうか確認する
   *
   * @param fid followerID
   * @return 指定したユーザが存在するかどうかの真偽値(存在する場合にtrue)
   */
  public boolean existsFollow(String uid,String fid) {

    // 指定したユーザがDBに登録済みか確認し、結果を戻り値として返す
    return friendrepo.existsByUidAndFid(uid,fid);
  }

   /**
   * フォローする人を記録する
   *
   * @param form FriendForm
   * @return 記録した友達リスト
   */
  public Friend record(
    FriendForm form) {

  // ユーザIDを変数に格納する
  final String uid = form.getUid();
  final String fid = form.getFid();

  // ユーザが登録されていない場合エラーを返す
  if (!userrepo.existsById(uid)) {
    throw new UserValidationException(
        USER_DOES_NOT_EXIST,
        "record the friend",
        String.format(
            "user %s does not exist",
            uid));
  }


  
  
  // 体調の記録をDBに保存し、保存した記録を戻り値として返す
  return friendrepo.save(new Friend(
      null,
      fid,
      form.getRelationship(),
      uid
      ));
}

  /**
   * フレンドの情報を取得する
   *
   * @param uid ユーザID
   * @return ユーザの情報
   */
  public Friend getFriend(Long listid) {

    // ユーザをDB上で検索し、存在すれば戻り値として返し、存在しなければ例外を投げる
    return friendrepo
        .findById(listid)
        .orElseThrow(() -> new UserValidationException(
            USER_DOES_NOT_EXIST,
            "get the user",
            String.format(
                "user %s does not exist",
                listid)));

  }

  public FriendList query(String uid){


    //フレンドのリストから自分がuidのリストを拾ってくる。
    List<Friend> friends=(List<Friend>) friendrepo.findByUid(uid);
    
    //フレンドのリストから自分がuidのリストを拾ってくる。
    List<Friend> followers=(List<Friend>) friendrepo.findByFid(uid);

    //フレンドのリストとフォローする人のニックネームも足したリスト
    List<FriendWithNickname> friendname=new ArrayList<FriendWithNickname>();

    //フレンドのリストとフォロワーのニックネームも足したリスト
    List<FollowerWithNickname> followername=new ArrayList<FollowerWithNickname>();

    String relation;
    
    for(Friend friend:friends){
      String fNickname=uservice.getUser(friend.getFid()).getNickname();
      if(friend.getRelationship()==1){
        relation="家族";
      }else if(friend.getRelationship()==2){
        relation="友達";
      }else{
        relation="介護者";
      }
      friendname.add(new FriendWithNickname(friend.getListid(),friend.getFid(),fNickname,relation,friend.getUid()));
    }

    for(Friend follower:followers){
      String followerNickname=uservice.getUser(follower.getUid()).getNickname();
      followername.add(new FollowerWithNickname(follower.getListid(),follower.getFid(),followerNickname,follower.getRelationship(),follower.getUid()));
    }

    // System.out.println(friendname);

    return new FriendList(
      friendname,
      followername
    );
  }


}
