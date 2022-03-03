package com.sik.myworld.domain.file;

import com.sik.myworld.domain.BaseEntity;
import com.sik.myworld.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@ToString(exclude = "post")
@Getter
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;


    private String uuid;
    private String path;
    @Column(name = "filename")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public void addFile(Post post){
        this.post = post;
        post.getFiles().add(this);
    }




    @Builder
    public File(String uuid, String path, String fileName, Post post) {
        this.uuid = uuid;
        this.path = path;
        this.fileName = fileName;
    }
}
