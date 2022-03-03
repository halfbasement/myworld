package com.sik.myworld.web.post.dto;

import com.sik.myworld.domain.region.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveDto {


    @NotBlank
    private String content;
    private String author;


    private List<UploadFileDto> uploadFiles;


    public PostSaveDto(Region region) {
        this.author = region.getMember().getNickname();
    }
}
