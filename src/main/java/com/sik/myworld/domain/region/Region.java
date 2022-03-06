package com.sik.myworld.domain.region;

import com.sik.myworld.domain.BaseEntity;
import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.post.Post;
import lombok.*;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "member")
public class Region extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    private String latitude; //위도
    private String longitude; //경도
    private String location; //위도 + 경도
    @Column(name = "regionname")
    private String regionName; //지역 이름

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "region",orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Region(String latitude, String longitude, String regionName, Member member) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = latitude+"/"+longitude;
        this.regionName = regionName;
        this.member = member;
    }
}
