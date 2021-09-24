function like(e, remarkId, isLike) {

    let isRun = false;
    if(isRun==true) {
        return false;
    }

    if (document.getElementById("user_id")) {


        let likeCountDom = document.getElementById("like-count-" + remarkId),
            dislikeCountDom = document.getElementById("dislike-count-" + remarkId);

        $.ajax({
            type: 'PUT',
            url: `/api/remark/${remarkId}/like/${isLike}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (result) {
            isRun =true;
            let likeInfo = result;
            likeCountDom.innerText = likeInfo.likeCount;
            dislikeCountDom.innerText = likeInfo.dislikeCount;
            changeLikeIcon();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    } else {
        location.href= "/login";
    }


    function changeLikeIcon(){
        let otherBtn='';

        if(e.classList.contains("like-btn")){
            otherBtn = document.getElementById("dislike-btn-"+remarkId);
        }else{
            otherBtn = document.getElementById("like-btn-"+remarkId);
        }

        getFirstChild(e).classList.toggle("fas");
        e.classList.toggle("like-active");
        if(otherBtn.classList.contains("like-active")){
            getFirstChild(otherBtn).classList.toggle("fas");
            otherBtn.classList.toggle("like-active")
        }
    }

    function getFirstChild(element) {
        let firstChild = element.firstChild;
        while (firstChild != null && firstChild.nodeType == 3) {
            firstChild = firstChild.nextSibling;
        }
        return firstChild;
    }
}

function deleteRemark(id) {
    if (confirm("정말 삭제하시겠습니까? \n삭제한 발언은 복원할 수 없습니다.") == true) {
        $.ajax({
            type: 'DELETE',
            url: '/api/remark/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            alert('발언이 삭제되었습니다.');
            history.back();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    } else {
        return;
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const tooltips = document.querySelectorAll(".tt")
    tooltips.forEach(t => {
        new bootstrap.Tooltip(t)
    })
})

