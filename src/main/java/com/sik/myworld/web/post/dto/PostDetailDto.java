package com.sik.myworld.web.post.dto;

import com.sik.myworld.domain.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostDetailDto {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime regDate;
    private List<UploadFileDto> uploadFiles;

    public PostDetailDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.regDate = post.getRegDate();
        this.uploadFiles = post.getFiles().stream().map(file -> new UploadFileDto(file.getFileName(), file.getUuid(), file.getPath())).collect(Collectors.toList());
    }
}
