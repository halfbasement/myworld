package com.sik.myworld.domain.file;

import com.sik.myworld.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File,Long> {

    List<File> findByPost(Post post);

    @Query("delete from File f where f.post.id = :pid")
    @Modifying
    void deleteByPostId(@Param("pid") Long pid);
}
