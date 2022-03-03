package com.sik.myworld.domain.post;

import com.sik.myworld.domain.BaseEntity;
import com.sik.myworld.domain.comment.Reply;
import com.sik.myworld.domain.file.File;
import com.sik.myworld.domain.region.Region;
import com.sik.myworld.web.post.dto.UploadFileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@ToString(exclude = {"files","region"})
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String content;
    private String author;


    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL) //고아객체
    private List<File> files = new ArrayList<>();


    @OneToMany(mappedBy = "post" , cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;




    public void updatePost(String content){
        this.content = content;

    }


    @Builder
    public Post(String content, String author, Region region) {
        this.content = content;
        this.author = author;
        this.region = region;
    }
}
