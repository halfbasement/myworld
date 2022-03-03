package com.sik.myworld.web.post.dto;

import com.sik.myworld.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPageDto {

    // 현재 페이지 번호
    private int num;

    // 게시물 총 갯수
    private int count;

    // 한 페이지에 출력할 게시물 갯수
    private int postNum = 3;

    // 하단 페이징 번호 ([ 게시물 총 갯수 ÷ 한 페이지에 출력할 갯수 ]의 올림)
    private int pageNum;

    // 출력할 게시물
    private int displayPost;

    // 한번에 표시할 페이징 번호의 갯수
    private int pageNumCnt = 5;

    // 표시되는 페이지 번호 중 마지막 번호
    private int endPageNum;

    // 표시되는 페이지 번호 중 첫번째 번호
    private int startPageNum;

    // 다음/이전 표시 여부
    private boolean prev;
    private boolean next;

    public void setCount(int count){
        this.count = count;
        dataCalc();
    }

    public void setNum(int num){
        this.num = num;
        this.displayPost = num-1;
    }
    private void dataCalc() {

        // 마지막 번호
        endPageNum = (int)(Math.ceil((double)num / (double)pageNumCnt) * pageNumCnt);

        // 시작 번호
        startPageNum = endPageNum - (pageNumCnt - 1);

        // 마지막 번호 재계산
        int endPageNum_tmp = (int)(Math.ceil((double)count / (double)postNum));

        if(endPageNum > endPageNum_tmp) {
            endPageNum = endPageNum_tmp;
        }

        prev = startPageNum == 1 ? false : true;
        next = endPageNum * postNum >= count ? false : true;


    }


}
