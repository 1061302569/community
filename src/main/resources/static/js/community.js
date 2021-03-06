function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_comment").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function (response) {
            if(response.code == 200){
                $("#comment_section").hide();
            }else{
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=7a1f191fefba88c37086&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                } else{
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
    console.log(questionId);
    console.log(content);
}


function selectTag(e){
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}

//标签点击事件
function showSelectTag(){
    $("#select-tag").show();
}