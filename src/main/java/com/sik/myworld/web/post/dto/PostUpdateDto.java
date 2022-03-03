package com.sik.myworld.web.post.dto;

import com.sik.myworld.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDto {

    private Long id;
    private String content;
    private String author;
    private String regionName;

    private List<UploadFileDto> uploadFiles;


    public PostUpdateDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.uploadFiles = post.getFiles().stream().map(file -> new UploadFileDto(file.getFileName(), file.getUuid(), file.getPath())).collect(Collectors.toList());
    }
}
