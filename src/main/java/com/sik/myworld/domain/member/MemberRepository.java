package com.sik.myworld.domain.member;

import com.sik.myworld.domain.region.Region;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> , MemberRepositoryCustom {


    /**
     * FETCH: entity graph에 명시한 attribute는 EAGER로 패치하고, 나머지 attribute는 LAZY로 패치
     * LOAD: entity graph에 명시한 attribute는 EAGER로 패치하고, 나머지 attribute는 entity에 명시한 fetch type이나 디폴트 FetchType으로 패치
     * (e.g. @OneToMany는 LAZY, @ManyToOne은 EAGER 등이 디폴트이다.)
     */

    //login 검증
    @EntityGraph(attributePaths = {"roleSet"},type = EntityGraph.EntityGraphType.LOAD) //
    @Query("select m from Member m where m.social = :social and m.email = :email")
    Optional<Member> findByEmailAndSocial(@Param("email") String email,@Param("social") boolean social);

    @EntityGraph(attributePaths = {"roleSet"},type = EntityGraph.EntityGraphType.LOAD) //
    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    Optional<Member> findByNickname(String nickName);


}
