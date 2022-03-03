package com.sik.myworld.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query("select r from Reply r where r.post.id = :pid order by r.regDate asc ")
    List<Reply> replyList(@Param("pid") Long pid);

}
