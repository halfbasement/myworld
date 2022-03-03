package com.sik.myworld.web.post.dto;

import com.sik.myworld.domain.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostListDto {

    private Long id;
    private String content;
    private String author;
    private LocalDateTime regDate;
    private UploadFileDto uploadFiles;
    private Integer replyCount;

    public PostListDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.regDate = post.getRegDate();
        this.replyCount = post.getReplies().size();
        this.uploadFiles = post.getFiles().stream().findFirst()
                .map(file->new UploadFileDto(file.getFileName(), file.getUuid(), file.getPath())).orElse(null);
    }
}
