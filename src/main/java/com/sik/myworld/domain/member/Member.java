package com.sik.myworld.domain.member;

import com.sik.myworld.domain.BaseEntity;
import com.sik.myworld.domain.comment.Reply;
import com.sik.myworld.domain.region.Region;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@ToString(exclude = {"regions","replies"})
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true ,length = 10)
    private String nickname;

    private boolean social;

    public void updateNickName(String nickname){
        this.nickname = nickname;
    }

    @OneToMany(mappedBy = "member",orphanRemoval = true)
    private List<Region> regions = new ArrayList<>();

    @OneToMany(mappedBy = "member",orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY) //값 타입 컬렉션 매핑
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole memberRole){
        roleSet.add(memberRole);
    }


    @Builder
    public Member(String email, String password, String nickname, boolean social) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.social = social;
    }


}
