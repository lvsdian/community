/**
 * 提交回复
 */
function postComment() {
    var questionId = $('#question_id').val();
    var content = $('#comment_content').val();
    comment2target(questionId,1,content);

}
function comment2target(targetId,type,content) {
    if(!content){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type:"post",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        dataType:"json",
        success:function (response) {
            if(response.code === 200){
                window.location.reload();
            }else {
                if(response.code === 2003){
                    var isAccept = confirm(response.message);
                    if(isAccept){
                        window.open("https://github.com/login/oauth/authorize?client_id=28976c45b11404fcbc21&redirect_uri=http://localhost:8081/callback&scope=user&state=1");
                        //设置一个本地存储(localStorage)，在github登录后跳转的index.html页面中取出这个值进行判断，判断index页面是否需要关闭
                        window.localStorage.setItem("closable",true);
                    }
                }
            }
        }
    });
}
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content);
}
/**
 * 展开二级评论
 */
function collapseComments(e) {
    //获得评论图标的data-id属性值
    var id = e.getAttribute("data-id");
    //获得二级评论的div
    var comments = $("#comment-"+id);
    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        //用户再次点击图标时，给二级评论div去除in class,折叠
        comments.removeClass("in");
        //给评论图标标记二级评论折叠状态
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        var subCommentContainer = $("#comment-"+id);
        if(subCommentContainer.children().length != 1){
            //用户点击图标时，给二级评论div加in class,展开
            comments.addClass("in");
            //给评论图标标记二级评论展开状态
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }else{
            $.getJSON("/comment/"+id,function (data) {
                $.each(data.data.reverse(),function (index,comment) {

                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                            "class":"media-object img-rounded",
                            "src":comment.user.avatarUrl
                        }));

                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                            "class":"media-heading",
                            "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"menu"
                    })).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //用户点击图标时，给二级评论div加in class,展开
                comments.addClass("in");
                //给评论图标标记二级评论展开状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            });
        }
    }
}

function  selectTag(e){
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if(previous.indexOf(value) == -1){
        if(previous){
            $("#tag").val(previous + ','+value);
        }else{
            $("#tag").val(value);
        }
    }
}
function  showSelectedTags() {
    $("#select-tag").show();
}