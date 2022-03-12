# My World

- 1인개발
- http://ec2-54-180-216-229.ap-northeast-2.compute.amazonaws.com:8080/
- ID : guest@guest / Password :  000 (가입을 원치 않으시면..) 
- 디자인은 비중을 두지 않고 만들었습니다.
- 구글 애널리틱스 연동 예정

## 프로젝트 설명

- 자신만의 지도에 원하는 지역을 추가해서 그 지역에 대한 글을 작성하고 , 다른 회원들과 공유 할 수 있으며 , 댓글로 소통 할 수 있게 만들었습니다.


## DB 설계

![DB도식](https://user-images.githubusercontent.com/96690643/156735845-e0427ff9-a3c0-43a7-93df-a888cf1269c3.PNG)



<br>

## 기술 스택
<br>

### 백엔드


- java 11
- Spring Boot 2.6.3 
- Spring data jpa 
- QueryDSL
- Spring Security / Oauth2

- MySQL / H2


### 프론트엔드

- thymeleaf ( thymeleaf-layout-dialect , extras-springsecurity5 , extras-java8time )

- JavaScript( ES6 ),  Jquery 

### 기타 라이브러리

- kakao map ( clusterer )
- Spring validation , lombok 등

### Devops

- aws ( ec2 , rds )


## 기능 설명

### 1. 전국의 모든 지역을 검색 가능 



### 2. 검색으로 된 지역 클릭 시 내 지도에 해당 지역 생성

- 검색기능을 탑재한 기본 kakao Map을 커스텀해서 클릭 시 click event로 해당 지역의 위도 , 경도 , 상호명을 받아온 후 Rest 형식으로 접속한 회원의 지도에 지역을 추가해줍니다

- 실세계에서 좌표( 위도 & 경도 ) 특성상 위도나 경도가 겹칠 수가 있으나 위도와 경도를 조합한 값은 유니크 하고 kakao map이 원하는 데이터 규격이 있기에 로직과 테이블 설계에서 고민거리를 하나 던져 줬는데, 간단하게 위도경도를 조합한 값으로 컬럼을 하나 더 생성하여 풀었습니다

### 3. 내 지도에 생성 된 지역 클릭시 해당 지역에 해당하는 페이지로 넘어가고 , 게시글을 볼 수 있습니다
- kakao map clusterer(좌표 데이터를 받아서 해당하는 지역에 마킹을 해주는 라이브러리)에 2번에서 의도적으로 받아온 상호명을 같이 넘겨줘서 mouseover event로직을 구현 해 해당 좌표에 해당하는 상호명을 동적으로 띄워줍니다.

- 게시글 리스트엔 페이징을 사용하였고 Post와 File관계에 있어서 아무 생각 없이 페치조인을 사용 했는데 메모리 에러가 났습니다.<br>어찌보면 당연한 것 인데 @OneToMany 관계에서 일대다 페치조인을 해버렸을 때  1개의 글에 3개의 사진이 있다고 한다면  <br>
Post 1 / File 1<br>
Post 1 / File 2<br>
Post 1 / File 3<br>
distinct를 써도 실제 db에선 이런식으로 조인이 될텐데 여기서 페이징을 때려버리면 사이즈 3을 줬을때 똑같은 post1이 3번 불러와지기 때문에 페치조인은 사용하면 안되는 걸 알았습니다. @OneToMany인 File은 LAZY를 주고  @BatchSize옵션으로 상한선을 정해두면 초기화 되지 않은 File proxy를 불러 올 때 ( 초기화 시킬 때 ) where에 in을 줘서 해당 Post(File 입장에서 @ManyToOne)에 해당하는 File을 1:1로 가져옵니다 

- 페이징은 사이즈 3을 줬고 동적으로 번호 수를 늘렸다 줄이고  이전과 다음버튼도 만들었습니다.

### 4. 여러장의 사진과 함께 글을 첨부 할 수 있고  댓글 작성 할 수 있음

- 멀티 파일 업로드는 rest 방식을 사용했고 ( 이미지 특성상 썸네일 때문에 ) 파일만 첨부하면 원본 파일은 로컬에 저장이 되고 , 실제 db에 저장 될 파일 정보는 첨부시에 같이 저장 됩니다 ( 글 수정은 원글에서의 파일 정보를 다 밀어버리고 저장버튼 누를 때 남아있는 정보만 db에 생성합니다 )

- 댓글은 CRUD모두 rest방식으로 동작하며 Entity설계 할 떄 조금 까다로웠습니다 대댓글 기능을 구현 할 때 셀프참조하는 방향으로 설계를 했는데 부모댓글의 pk값을 셀프로 저장했는데 cascade나 orphanRemoval속성을 사용하기 까다로워서 좋은 설계는 아닌 것 같지만 부모댓글이 사라질 때 자식 댓글은 사라지게 되기 때문에 @OnCascade옵션으로 강제 삭제를 줬습니다 ( 더 고민해 볼 문제 같습니다. )



### 5. 메인 화면에선 본인을 제외 한 다른 회원들의 프로필이 생성되고 클릭 시 지도에 해당 회원의 지도가 그려지고 해당 회원의 지역에 방문 , 댓글 쓰기 가능

- 다른 회원들의 지도를 방문 할 때 고민한게 회원마다 마이페이지 만들어서 본인의 지도를 들고 다닐 것 인가  아니면 하나의 지도에 회원의 좌표만 새로 뿌려줄 것인가 고민 했었는데 이건 사용자 입장에서 봤을 때 ,  페이지에 다른 회원들의 프로필이 생성 되기 때문에 클릭만하면  하나의 지도에 바로 좌표를 뿌려주는게 접근성에서 더 좋을 것 같고  개발하는 입장에선 어차피 장소가 겹친다한들 좌표값은(region테이블) Member 1 : N Region으로 관리 하기 때문에 프로필 클릭시 Member로 조인해서 좌표값만 뿌려주면 되기 떄문에 후자로 정했습니다

- 다른회원들의 프로필 생성은 QueryDsl과 페이징으로 나를 제외하고 가입한순서 대로 3명씩 rest형식으로 뿌려줘서 실시간으로 여러명의 회원을 볼 수 있습니다.


### 6. 로그인 , 기타 보안

- 기본적으로 Spring Security를 이용하였고 기본적인 CRUD권한은 다 설정 했습니다
- oauthLoginService에서 실제 oauth사용자의 db를 반영 할 때 profile의 name값으로 nickname을 채우면 기존 사용자와 겹칠수도 있고 ( 검증로직을 짠다 해도 후처리가 애매해지기 때문에 ) oauth사용자가 본인의 profile에 지정한 name값을 nickname으로 사용하고싶지 않을 수도 있기 때문에 바로 수정폼으로 redirect하도록 짰습니다 oauth사용자의 정보를  db에 반영시 nickname값은 일단 본인의 이메일값과 동일하게 반영하고 @Validated로 일반 회원가입이나  수정에 nickname값을 정규식을 사용하여 특수문자를 못넣게 로직을 짰습니다 이렇게 되면 oauth 사용자가 로그인 하면서 nickname값이 email이기 @가 들어가는데 유도한 수정창에서 닉네임을 변경 할 수밖에 없습니다.
강제성이 있고 중간에 흐름을 끊으면 애매해지기 때문에  좋은 로직은 아니라고 생각합니다 ( 메일인증을 사용하면 조금 더 깔끔해질 것 같습니다.)


## 기타 설정

- 기본적이고 공통적인 스프링 웹 검증  error message , @Validated , BindingResult 등
- 에러페이지 처리 ( 4xx , 5xx  등등 )
- Api에러 처리  
- PRG 등 기본으로 처리해야 할 웹 CRUD처리 , 보안

## 그 외 문제점 , 아쉬운 점

- osiv문제를 생각안하고 로직을 짜서 조금 아쉬웠습니다  프로젝트를 개발하는 중에  로그에 warn이 뜬 걸 발견해서 알아보니 osiv문제 였습니다 <br>
실제 서비스 트랜잭션 영역에서 LAZY로 갖고온 연관관계의 엔티티를 초기화 해주지 않고 뷰까지 가져오면 당연히 비어있는 프록시를 접근해서 문제가 생기는건데 스프링부트에선 이걸 방지하는 옵션이 default로 켜져 있어서 잊고 개발 했던 것 같습니다  이 옵션은 뷰 단까지 DB커넥션을 계속 열어둬서 뷰에서 초기화 되지 않은 프록시를 초기화 시킬 수 있는 기능인데 편의성 면에선 정말 편하고  개인 프로젝트는 별 상관이 없지만 대용량 트래픽을 마주 했을 때 DB커넥션을 끝내지않고 물고있는건 자원을 고갈시킬 수 있기 때문에 앞으로의 개발에선 고려를 해야 하는 부분이라고 생각 합니다 

- 쌩 html을 동적으로 생성하는건 언제나 시간을 많이 잡아 먹습니다 , 댓글기능은 리액트가 절실 했습니다.



