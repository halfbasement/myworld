package com.sik.myworld.domain.comment;

import com.sik.myworld.domain.BaseEntity;
import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","post"})
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;


    private String comment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_id")
    private Reply parent;
    //널이면 부모댓글


    public void updateReply(String comment){
        this.comment = comment;
    }

    @Builder
    public Reply(String comment, Member member, Post post ,Reply reply) {
        this.comment = comment;
        this.member = member;
        this.post = post;
        this.parent = reply;
    }
}
