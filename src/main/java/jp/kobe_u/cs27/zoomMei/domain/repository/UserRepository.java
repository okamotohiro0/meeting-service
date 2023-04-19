package jp.kobe_u.cs27.zoomMei.domain.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import jp.kobe_u.cs27.zoomMei.domain.entity.User;

/**
 * ユーザのリポジトリ
 */
@Repository
public interface UserRepository extends CrudRepository<User,String> {
    
    boolean existsByNickname(String nickname);

    User findByNickname(String nickname);
}
