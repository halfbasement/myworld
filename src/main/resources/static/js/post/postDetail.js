let postDetail = {
    init: function (e) {
        let _this = this;

        _this.detail();
        _this.commentLoad();
        _this.createComment();


        //수정 버튼 클릭
        $(document).on('click','.comment_update_btn',function (e){

            let replyId = e.target.value;


            let url='/reply/'+replyId+'/updateModalInfo'
            $.getJSON(url, function (data) {


                if(data) {
                    //html생성
                    var str = "";

                    str += '<div class="updateComment_modal" >'
                    str += '<div className="mb-4" style="background: white; height: 120px ; height:140px; border: 1px solid black">'
                    str += '<div className="comment_updateBox" style="padding: 20px;">'
                    str += '<div  id="comment_update_author" value="'+data.loginMember+'" >' + data.loginMember + '</div>' //밸류 미리 박아둬야함 -> 로그인 정보 (컨트롤러에서 세션정보가져오기)
                    str += '<div><textarea className="form-control" rows="1" style="border: none; width: 100%" id="comment_update" placeholder="수정할 댓글을 입력해주세요">'+data.comment+'</textarea> </div>' // -> 입력
                    str += '<input type="hidden" id="update_commentId" value="'+replyId+'" >'
                    str += '<button type="button" id="comment_update_cancel"  style="float: right; border:none; background:white ">취소</button>'
                    str += '<button type="button" id="comment_update_submit"  style="float: right; border:none; background:white ">등록</button>'
                    str += '</div>'
                    str += '</div>'
                    str += '</div>'



                    $('#updateComment_box' + replyId).html(str);

                }

                $('#comment_update_submit').on('click', function () {
                    _this.update();
                    //  $('.updateComment_modal').remove();

                })

                $('#comment_update_cancel').on('click',function (){
                    $('.updateComment_modal').remove();
                    _this.commentLoad();
                })


            })



        })







        //삭제 버튼 클릭
        $(document).on('click','.comment_delete_btn',function (e){
            let replyId =  e.target.value;
            let deleteUrl = '/reply/'+replyId;




            $.ajax({
                type: 'DELETE',
                url: deleteUrl,
                //dataType: 'json', 데이터타입 json으로 하면 null(parent)을 못받아줘서 에러로 넘어감
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    alert("삭제 되었습니다.")


                },
                error: function (e) {
                    alert(JSON.stringify(e))
                }
            }).done(function (){
                console.log('delete dont')
                postDetail.commentLoad();
            })

        })

        // subComment modal On/off
        $(document).on('click','.subWrite_btn',function (e){

            let postId = $('#detailPostId').val();

            $('.subComment_modal').remove();


            // 현재 대댓글의 ID와 부모댓글의 ID 불러옴
            let value = e.target.value.split(",");
            let subCommentId =  value[0];
            let parent = value[1];

            //부모가 없다면 , 부모의 아이디 반환
            function checkId(subCommentId,parent){

                if(parent===null||parent===undefined){
                    return subCommentId;
                }else{
                    return parent;
                }

            }


            let url='/reply/'+postId+'/addModalInfo'
            $.getJSON(url, function (data) {


                if(data) {
                    //html생성
                    var str = "";

                    str += '<div class="subComment_modal" >'
                    str += '<div className="mb-4" style="background: white; height: 120px ; height:140px; border: 1px solid black">'
                    str += '<div className="sub_comment_addBox" style="padding: 20px;">'
                    str += '<div  id="comment_sub_author"  value="' + data.loginMember + '">' + data.loginMember + '</div>' //밸류 미리 박아둬야함 -> 로그인 정보 (컨트롤러에서 세션정보가져오기)
                    str += '<div><textarea className="form-control" rows="1" style="border: none; width: 100%" id="comment_sub" placeholder="댓글을 남겨보세요"></textarea> </div>' // -> 입력
                    str += '<input type="hidden" id="sub_comment_post_id" value="' + data.postId + '"/>' //밸류 미리 박아둬야함 -> @PathVariable
                    str += '<input type="hidden" id="sub_comment_parent" value="' + checkId(subCommentId, parent) + '"/>'
                    str += '<button type="button" id="comment_sub_cancel"  style="float: right; border:none; background:white ">취소</button>'
                    str += '<button type="button" id="comment_sub_submit"  style="float: right; border:none; background:white ">등록</button>'
                    str += '</div>'
                    str += '</div>'
                    str += ' <hr class="my-4">'
                    str += '</div>'


                    $('#subComment_input' + subCommentId).append(str);

                }

                $('#comment_sub_submit').on('click', function () {
                    _this.createSub();
                    $('.subComment_modal').remove();

                })

                $('#comment_sub_cancel').on('click',function (){
                    $('.subComment_modal').remove();
                })


            }).fail(function (){
                alert("로그인이 필요합니다.")
                window.location.href="/login?redirectURL=/post/"+postId;
            })

        })

    },



    update : function (){

        let replyId =  $('#update_commentId').val()
        let  data = {
            comment: $('#comment_update').val(),
            nickName: $('#comment_update_author').attr('value')
        }

        console.log(replyId)
        console.log('dataupdate',data)

        if(data.comment===""){
            alert("내용을 입력해주세요")
            return;
        }else{

            $.ajax({
                type: 'PUT',
                url: '/reply/'+replyId,
                //dataType: 'json', 데이터타입 json으로 하면 null(parent)을 못받아줘서 에러가 뜸
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data),
                success: function (data) {
                },
                error: function (e) {
                    alert(JSON.stringify(e))
                }
            }).done(function (){
                postDetail.commentLoad();
            })

        }

    }
    ,




    detail : function (){

        let nowImage = 1;

        $(document).ready(function (){




            $('.slide-prev').hide();

          let imageCount= $('#imagePrevBtn').data('image');


            if(nowImage==imageCount){
                $('.slide-next').hide();
            }


        })

        $('.slide-next ').on('click',function (){



            let imageCount = $(this).data('image');


            console.log(imageCount)

           if(nowImage<imageCount){
               $('.slide-box').css('transform', 'translateX(-' + (nowImage) + '00vw)');
               $('.slide-prev').show();
               nowImage++;

           }

           if(nowImage==imageCount){
               $('.slide-next').hide();
           }

        })
        $('.slide-prev ').on('click',function (){



            let imageCount = $(this).data('image');


            console.log(imageCount)

            if(nowImage>1){
                $('.slide-box').css('transform', 'translateX(-' + (nowImage-2) + '00vw)');

                nowImage--;

            }

            if(nowImage<=1){
                $('.slide-prev').hide();
                $('.slide-next').show();
            }
        })


        // delete

        $('#postDeleteBtn').on('click',function (){

            let data={
                pid:$('#detailPostId').val(),
                rid:$('#detailRegionId').val()
            }

            let url='/region/'+data.rid+"/post/"+data.pid+"/delete";

            $.ajax({
                type: 'delete',
                url: url,
                //  dataType: 'json', //데이터타입 json으로 하면 null(parent)을 못받아줘서 에러가 뜸
                contentType: 'text',
                success: function (result) {
                    alert(result)
                    window.location.replace("/region/"+data.rid+"/post");
                },
                error: function (e) {
                    alert("삭제실패")

                }
            })

        })





    },



    createSub: function () {


        var data = {

            postId: $('#sub_comment_post_id').val(),
            nickName:$('#replyMember').val(),
            comment:$('#comment_sub').val(),
            replyId:$('#sub_comment_parent').val()
        }

        console.log(data)


        if(data.comment===""){
            alert("내용을 입력해주세요")

            return;
        }else{
            $.ajax({
                type: 'POST',
                url: '/reply',
                //dataType: 'json', 데이터타입 json으로 하면 null(parent)을 못받아줘서 에러가 뜸
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data),
                success: function (data) {
                },
                error: function (e) {
                    alert(JSON.stringify(e))
                }
            }).done(function (){
                postDetail.commentLoad();
            })
        }



    },







    createComment : function (){

        let postId = $('#detailPostId').val();

        $('#mainCommentBtn').on('click',function (){

            let data={
                postId: postId,
                nickName:$('#replyMember').val(),
                comment:$('#mainComment').val(),
                replyId:$('#replyId').val(),
                parent:$('#sub_comment_parent').val()
            }

            if(data.comment == false){
                alert("내용을 입력하세요")
                return;
            }

            $.ajax({
                type: 'post',
                url: '/reply',
                //  dataType: 'json', //데이터타입 json으로 하면 null(parent)을 못받아줘서 에러가 뜸
                data : JSON.stringify(data),
                contentType: 'application/json; charset=utf-8',
                success: function (result) {
                    console.log(result);

                },
                error: function (e) {
                    alert("에러")

                }
            }).done(function (){
                postDetail.commentLoad();
                $('#mainComment').val('');
            })
        })
    },
    commentLoad : function () {


        function fomatDate(modifiedDate) {
            var date = new Date(modifiedDate);

            return date.getFullYear() + '/' +
                (date.getMonth() + 1) + '/' +
                date.getDate() + ' ' +
                date.getHours() + ':' +
                date.getMinutes();
        }


        let postId = $('#detailPostId').val();

        $.getJSON('/reply/' + postId, function (data) {



            console.log(data)

            if (data.sub && data.main == false) //데이터가 없으면
            {
                $('.replyList').html("<div></div>"); //아무것도 없이 반환 // 꼼수

                return;
            }

            let str = "";

            // 댓글 생성
            $.each(data.main, function (idx, main) {

                function checkMainEmail() {


                    if (data.loginMember !== false && data.loginMember === main.nickName) {
                        return ' <button class="btn btn-default comment_delete_btn" style="float: right" value="' + main.replyId + '" >삭제</button> ' +
                            ' <button class="btn btn-default comment_update_btn" style="float: right"  value="' + main.replyId + '">수정</button>';
                    } else {
                        return '';
                    }
                }


                str += '<div class="card-body" style="padding: 0px">'
                str += '<div id="updateComment_box' + main.replyId + '">'
                str += '<h5 class="card-title">' + main.nickName + '</h5>  '
                str += '<h6 class="card-subtitle mb-2 text-muted">' + main.comment + '</h6>'
                str += '<p class="card-text">' + fomatDate(main.regDate) + '</p>'
                str += ' <button class="btn btn-default subWrite_btn"   value="' + main.replyId + '">답글쓰기</button> '
                str += checkMainEmail();
                str += '</div>'
                str += ' <hr class="my-4">'
                str += '<div id="subComment_input' + main.replyId + '" >'
                str += '</div>'

                //자식이 있으면 자식도 생성
                $.each(data.sub, function (idx, sub) {

                    function checkSubEmail() {
                        if (data.loginMember !== false && data.loginMember === sub.nickName) {
                            return ' <button class="btn btn-default comment_delete_btn" style="float: right" value="' + sub.replyId + '">삭제</button> ' +
                                ' <button class="btn btn-default comment_update_btn" style="float: right" value="' + sub.replyId + '">수정</button>';
                        } else {
                            return '';
                        }
                    }

                    if (main.replyId == sub.parent) {


                        str += '<div id="updateComment_box' + sub.replyId + '" style="margin-left: 50px; padding: 0px">'
                        str += '<h5 class="card-title">' + sub.nickName + '</h5>  '
                        str += '<h6 class="card-subtitle mb-2 text-muted">' + sub.comment + '</h6>'
                        str += '<p class="card-text">' + fomatDate(sub.regDate) + '</p> '
                        str += '<button class="btn btn-default subWrite_btn"   value="' + sub.replyId + ',' + main.replyId + '">답글쓰기</button> '
                        str += checkSubEmail();
                        str += '</div>'
                        str += ' <hr class="my-4">'
                        str += '<div id="subComment_input' + sub.replyId + '" >'
                        str += '</div>'

                    }
                })


                str += '</div>'

                $('.replyList').html(str);

            })

        }) // 리스트 불러오기


    }

};
postDetail.init()