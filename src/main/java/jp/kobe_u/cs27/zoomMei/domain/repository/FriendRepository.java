package jp.kobe_u.cs27.zoomMei.domain.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.kobe_u.cs27.zoomMei.domain.entity.Friend;

/**
 * フォロー管理のリポジトリ
 */
@Repository
public interface FriendRepository extends CrudRepository<Friend, Long> {
    
    //uid,fid,relationshipが一致するものの存在確認
  /**
   * ユーザIDが一致する体調記録を全て削除する
   *
   * @param uid ユーザID
 * @return 
   */
    boolean existsByUidAndFid(String uid,String fid);

    List<Friend> findByUid(String uid);

    List<Friend> findByFid(String fid);
}
