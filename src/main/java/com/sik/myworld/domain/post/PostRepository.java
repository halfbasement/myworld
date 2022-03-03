package com.sik.myworld.domain.post;

import com.sik.myworld.domain.region.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {



    @EntityGraph(attributePaths = "files",type = EntityGraph.EntityGraphType.LOAD)// 페이징을 위해 지워줌
    Page<Post> findByRegion(Region region, Pageable pageable); //left join*/



    @Query("select DISTINCT p from Post p left join p.files where p.region.id = :rid")
    Page<Post> pagingPostByRegionId(@Param("rid") Long rid, Pageable pageable); //left join






}
