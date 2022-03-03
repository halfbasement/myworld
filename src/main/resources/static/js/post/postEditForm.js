let postEditForm = {
    init: function (e) {
        let _this = this;

        _this.editForm();



    },


    editForm : function (){


        let postId = $('#postEditId').val();



        $.getJSON('/file/' + postId, function (data) {


            console.log(data)
            let uploadUL = $(".uploadResult ul");



            let str="";

            $(data).each(function (i,obj){
                str+= "<li data-name='"+ obj.fileName + "' data-path='"+obj.path+"'data-uuid='"+obj.uuid+"'>";
                str+="<div>";
                str+="<button  type='button' data-file=\'"+ obj.imageURL + "\' >X</button><br>";
                str+="<img src='/display?fileName="+obj.imageURL+"' style='width: 100px; height: 100px'>";
                str+="</div>";
                str+="</li>";
            })



            uploadUL.append(str);




        })


        let regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
        let maxSize = 5242880;

        function checkExtension(fileName,fileSize){
            if(fileSize >= maxSize){
                alert("파일 사이즈 초과");
                return false;
            }

            if(regex.test(fileName)){
                alert("해당 종류의 파일은 업로드 할 수 없습니다.");
                return false;
            }

            return true;
        }


        $('#editFiles').on('change',function (){



            let formData = new FormData();

            let inputFile = $('#editFiles');

            let files = inputFile[0].files;

            for( let i= 0; i< files.length; i++){
                console.log(files[i]);
                formData.append('uploadFiles',files[i])
            }

            //ajax
            $.ajax({
                url:'/uploadFile',
                processData: false,
                contentType: false,
                data: formData,
                type: 'POST',
                dataType: 'json',
                success: function (result){
                    console.log(result)
                    showImage(result);
                },
                error: function (jqXHR,textStatus, errorThrown){
                    console.log(textStatus);
                }
            })//전송 ajax 끝




        })

        //삭제
        $('.uploadResult').on('click','li button',function (e){

            let targetLi =$(this).closest('li');

                    targetLi.remove();
        })

        //등록
        $('#postEditBtn').on('click',function (e){
            e.preventDefault();

            let str = "";

            console.log(str);

            $('.uploadResult li').each(function (i,obj){
                let target = $(obj);
                str +="<input type='hidden' name='uploadFiles["+i+"].fileName' value='"+target.data('name')+"'> ";
                str +="<input type='hidden' name='uploadFiles["+i+"].path' value='"+target.data('path')+"'> ";
                str +="<input type='hidden' name='uploadFiles["+i+"].uuid' value='"+target.data('uuid')+"'> ";

            })

            console.log(str);

            $(".fileHiddenBox").html(str);

           if(str==false){
                alert("사진을 등록해주세요")
                return;
            }

           $("#postEditForm").submit();
        })


        function showImage(arr){

            console.log(arr)
            let uploadUL = $(".uploadResult ul");



            let str="";

            $(arr).each(function (i,obj){
                str+= "<li data-name='"+ obj.fileName + "' data-path='"+obj.path+"'data-uuid='"+obj.uuid+"'>";
                str+="<div>";
                str+="<button  type='button' data-file=\'"+ obj.imageURL + "\' >X</button><br>";
                str+="<img src='/display?fileName="+obj.imageURL+"' style='width: 100px; height: 100px'>";
                str+="</div>";
                str+="</li>";
            })



            uploadUL.append(str);




         /*   for(let i=0; i<arr.length; i++){

                divArea.css({"width":(i+1)+"00vw"})

                divArea.append(`<div class="slide-box" style="width: 50vw;float:left;" >
                                    <img class="displayImage" style="width: auto;" src=/display?fileName=${arr[i].imageURL}>
                                </div> `)
            }*/

        }
    },









};
postEditForm.init()