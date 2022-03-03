package com.sik.myworld.domain.region;

import com.sik.myworld.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region,Long> {

    List<Region> findByMember(Member member);


}
