package com.fis.hrmservice.infra.persistence.repository.user;

import com.fis.hrmservice.infra.persistence.entity.Avatar;
import com.fis.hrmservice.infra.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

  @Query("SELECT a.avatarUrl FROM Avatar a WHERE a.user.id = :userId")
  String findAvatarUrlByUserId(@Param("userId") Long userId);

  Long user(User user);
}
