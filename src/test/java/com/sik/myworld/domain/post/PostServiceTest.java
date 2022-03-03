package com.sik.myworld.domain.post;

import com.sik.myworld.domain.file.File;
import com.sik.myworld.domain.file.FileRepository;
import com.sik.myworld.domain.region.Region;
import com.sik.myworld.domain.region.RegionRepository;
import com.sik.myworld.web.post.dto.PostSaveDto;
import com.sik.myworld.web.post.dto.UploadFileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private PostService postService;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private PostRepository postRepository;


    @Test
    void insert(){

        Region region = regionRepository.findById(2L).get();

        IntStream.rangeClosed(1,50).forEach(i->{

            Post post = Post.builder()
                    .author("123"+i)
                    .content("12"+i)
                    .region(region)
                    .build();

            postService.save(post,new ArrayList<>());

        });

    }


    @Test
    @Transactional
    void List(){

        Region region = regionRepository.findById(6L).get(); //member 2 ( vnfthr@test.com)


       // List<Post> content = postRepository.findByRegion2(6L);

        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"regDate"));

        List<Post> content = postRepository.pagingPostByRegionId(6L, pageRequest).getContent();



        for (Post post : content) {
            System.out.println("post = " + post);
            System.out.println("post.getFiles() = " + post.getFiles());
        }


    }


    @Test
    @Transactional
    void pageable(){
        Region region = regionRepository.findById(6L).get(); //member 2 ( vnfthr@test.com)

        PageRequest pageRequest = PageRequest.of(0,4, Sort.by(Sort.Direction.DESC,"regDate"));

        Page<Post> page = postService.postList(region, pageRequest);


        List<Post> content = page.getContent();

        for (Post post : content) {
            System.out.println("post = " + post);
            System.out.println("post getFiles = " + post.getFiles());
        }


    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void save() {

        Region region = regionRepository.findById(3L).get(); //member 1 ( test@test.com)

        UploadFileDto fileDto = new UploadFileDto("UUID","PATH","FILENAME");
        UploadFileDto fileDto2 = new UploadFileDto("UUID2","PATH2","FILENAME2");
        UploadFileDto fileDto3 = new UploadFileDto("UUID3","PATH3","FILENAME3");

        List<UploadFileDto> fileDtos = new ArrayList<>();

        fileDtos.add(fileDto);
        fileDtos.add(fileDto2);
        fileDtos.add(fileDto3);





        List<File> files = new ArrayList<>();



    }

}