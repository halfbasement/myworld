package com.sik.myworld.domain.post;

import com.sik.myworld.domain.region.Region;
import com.sik.myworld.domain.region.RegionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    EntityManager em;



    @Test
    @Transactional
    void savePost(){




    }

    @Test
    void findPostsByRegionId(){


    }

    @Test
    void findAllPosts(){

        List<Post> all = postRepository.findAll();

        for (Post post : all) {
            System.out.println("post = " + post);
        }
    }

}