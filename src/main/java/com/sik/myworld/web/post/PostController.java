package com.sik.myworld.web.post;

import com.sik.myworld.domain.file.File;
import com.sik.myworld.domain.post.Post;
import com.sik.myworld.domain.post.PostRepository;
import com.sik.myworld.domain.post.PostService;
import com.sik.myworld.domain.region.Region;
import com.sik.myworld.domain.region.RegionService;
import com.sik.myworld.security.dto.MemberAuthDto;
import com.sik.myworld.web.post.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/region/{rid}")
@RequiredArgsConstructor
@Slf4j
public class PostController {


 /*   @GetMapping("/post/{rid}")
    @ResponseBody
    public String postList(@PathVariable Long rid){

    }*/

    private final PostRepository postRepository;

    private final RegionService regionService;
    private final PostService postService;


    @GetMapping("/post/{pid}")
    public String postDetail(@PathVariable Long rid, @PathVariable Long pid, Model model,
                             @AuthenticationPrincipal MemberAuthDto loginMember) {
        Region findRegion = regionService.findByRegionId(rid);

        System.out.println("findRegion = " + findRegion.getMember().getNickname());

        Post post = postService.findById(pid);

        boolean isLoginMember = findRegion.getMember().getEmail().equals(loginMember.getEmail());


        PostDetailDto postDetailDto = new PostDetailDto(post);

        postDetailDto.setAuthor(findRegion.getMember().getNickname());

        model.addAttribute("post", postDetailDto);
        model.addAttribute("rid", rid);
        model.addAttribute("loginMember",loginMember.getNickName());
        model.addAttribute("isLoginMember",isLoginMember);
        return "post/postDetail";
    }


    @GetMapping("/post/{pid}/edit")
    public String postEditForm(@PathVariable Long rid, @PathVariable Long pid,
                               @AuthenticationPrincipal MemberAuthDto loginMember,
                               Model model) {
        Region region = memberCheck(loginMember, rid).orElseThrow(() -> new IllegalArgumentException("잘못 된 접근"));//널이 들어오면 잘못된 접근


        Post findPost = postService.findById(pid);


        PostUpdateDto post = new PostUpdateDto(findPost);


        post.setRegionName(region.getRegionName());


        model.addAttribute("post",post);



        return "post/postEditForm";
    }
    @PostMapping("/post/{pid}/edit")
    public String editPost(@PathVariable Long rid,
                           @PathVariable Long pid,
                           @ModelAttribute PostUpdateDto dto,
                           @AuthenticationPrincipal MemberAuthDto loginMember,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        Region region = memberCheck(loginMember, rid).orElseThrow(() -> new IllegalArgumentException("잘못된 접근"));



        postService.update(pid,dto);

        return "redirect:/region/"+rid+"/post/"+pid;
    }

    @DeleteMapping("/post/{pid}/delete")
    @ResponseBody
    public ResponseEntity postDelete(@PathVariable Long pid,
                                     @PathVariable Long rid,
                                     @AuthenticationPrincipal MemberAuthDto loginMember
    ) {


        memberCheck(loginMember, rid).orElseThrow(() -> new IllegalArgumentException("잘못 된 접근")); //널이 들어오면 잘못된 접근

        postService.deleteByPostId(pid);


        return new ResponseEntity("게시글이 삭제 되었습니다", HttpStatus.OK);
    }


    @GetMapping("/post")
    public String postList(@PathVariable Long rid,
                           @RequestParam(value = "num", required = false, defaultValue = "1") Integer num,
                           Model model,
                           @AuthenticationPrincipal MemberAuthDto loginMember) {

        Region findRegion = regionService.findByRegionId(rid);


        boolean isLoginMember = findRegion.getMember().getEmail().equals(loginMember.getEmail());



        PostPageDto page = new PostPageDto();

        page.setNum(num);


        System.out.println("page.display = " + page.getDisplayPost());



        PageRequest pageRequest = PageRequest.of(page.getDisplayPost(), page.getPostNum(), Sort.by(Sort.Direction.DESC, "regDate"));




        Page<Post> pagePosts = postService.postList(findRegion, pageRequest);


        page.setCount((int)pagePosts.getTotalElements());


     /*   int number = pagePosts.getNumber(); //현재 페이지
        int totalPages = pagePosts.getTotalPages(); //총 페이지 갯수
        long totalElements = pagePosts.getTotalElements(); //총 게시물 수
        boolean isFirst = pagePosts.isFirst(); // 현재 페이지가 첫번째인지
        pagePosts.isLast();//현재 페이지가 마지막인지
        boolean hasNext = pagePosts.hasNext(); //다음 페이지가 있는지
        boolean hasPrevious = pagePosts.hasPrevious();//전 페이지가 있는지
*/


        List<PostListDto> posts = pagePosts.getContent().stream()
                .map(post -> new PostListDto(post)).collect(Collectors.toList());

        String regionName = findRegion.getRegionName();

        String nickname = findRegion.getMember().getNickname();


        model.addAttribute("regionName", regionName);
        model.addAttribute("postMaster",nickname);
        model.addAttribute("posts", posts);
        model.addAttribute("rid", rid);
        model.addAttribute("page",page);
//현재 페이지
        model.addAttribute("select",num);
        model.addAttribute("isLoginMember",isLoginMember);

        return "post/postList";
    }


    //save
    @GetMapping("/post/save")
    public String saveForm(@PathVariable Long rid,
                           @AuthenticationPrincipal MemberAuthDto loginMember,
                           Model model) {

        //검증 로직
        Region region = memberCheck(loginMember, rid).orElseThrow(() -> new IllegalArgumentException("잘못 된 접근"));

        model.addAttribute("post", new PostSaveDto(region)); //폼 생성시 지역 이름 뿌려줌


        model.addAttribute("regionName",region.getRegionName());
        return "post/postSaveForm";
    }

    @PostMapping("/post/save")
    public String savePost(@PathVariable Long rid,
                           @ModelAttribute PostSaveDto dto,
                           @AuthenticationPrincipal MemberAuthDto loginMember,
                           Model model,
                           RedirectAttributes redirectAttributes) {



        Region region = memberCheck(loginMember, rid).orElseThrow(() -> new IllegalArgumentException("잘못된 접근"));


        redirectAttributes.addAttribute("rid",rid);


        //converting
        Post post = Post.builder()
                .content(dto.getContent())
                .author(dto.getAuthor())
                .region(region)
                .build();


        //이미지 여부 체크
        try {
            List<File> files = dto.getUploadFiles().stream().map(uploadFileDto -> {
                File file = File.builder()
                        .path(uploadFileDto.getPath())
                        .uuid(uploadFileDto.getUuid())
                        .fileName(uploadFileDto.getFileName())
                        .build();
                return file;
            }).collect(Collectors.toList());

            Post save = postService.save(post, files);

            redirectAttributes.addAttribute("savePostId",save.getId());

            return "redirect:/region/{rid}/post/{savePostId}";

        } catch (NullPointerException e) {


            Post save = postService.save(post, new ArrayList<File>());

            redirectAttributes.addAttribute("savePostId",save.getId());


            return "redirect:/region/{rid}/post/{savePostId}";

        }


        //converting


    }


    //현재 지역을 저장한 회원의 이메일과 로그인한 회원의 이메일을 비교 , 같으면 찾은 지역을 반환 다르면 null
    private Optional<Region> memberCheck(MemberAuthDto loginMember, Long rid) {

        //현재 지역 찾고
        Region findRegion = regionService.findByRegionId(rid);

        boolean equals = findRegion.getMember().getEmail()
                .equals(loginMember.getEmail());
        if (equals == true) {
            return Optional.of(findRegion);
        }
        return null;
    }


}
