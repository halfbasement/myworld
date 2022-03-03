package com.sik.myworld.domain.post;

import com.sik.myworld.domain.file.File;
import com.sik.myworld.domain.file.FileRepository;
import com.sik.myworld.domain.region.Region;
import com.sik.myworld.web.post.dto.PostUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final FileRepository fileRepository;


    public Page<Post> postList(Region region, PageRequest pageRequest){

      return   postRepository.pagingPostByRegionId(region.getId(),pageRequest);

    }


    public void deleteByPostId(Long pid){
        postRepository.deleteById(pid);
    }

    public Post findById(Long id){

      return   postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글 입니다"));
    }

    @Transactional
    public Post save(Post post, List<File> files){

        Post savePost = postRepository.save(post);

        if(!files.isEmpty()){
            files.stream().forEach(file -> {
                file.addFile(savePost);
             //   fileRepository.save(file);
            });
        }

        return savePost;
    }

    @Transactional
    public void update(Long pid, PostUpdateDto dto){

        Post findPost = postRepository.findById(pid).orElseThrow(() -> new IllegalArgumentException("없는 게시물 입니다"));

        fileRepository.deleteByPostId(findPost.getId());

        findPost.updatePost(dto.getContent());


        if(dto.getUploadFiles() != null){


            List<File> files = dto.getUploadFiles().stream().map(uploadFileDto -> {
                File file = File.builder()
                        .path(uploadFileDto.getPath())
                        .uuid(uploadFileDto.getUuid())
                        .fileName(uploadFileDto.getFileName())
                        .build();
                return file;
            }).collect(Collectors.toList());


            files.stream().forEach(file -> {
                file.addFile(findPost);
            });

        }



    }



}
