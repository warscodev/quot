let isRun = false;


function highlightKeyword() {
    const keywordEl = document.querySelector("#r-l-keyword");
    if (keywordEl) {
        let keyword = keywordEl.value;

        if (keyword != null && keyword !== "" && keyword !== undefined) {
            let tab = document.querySelector("#r-l-tab").value,
                remarkContentEl = document.querySelectorAll(".r-l-t-content"),
                remarkPersonEl = document.querySelectorAll(".r-l-t-person-name"),
                regex = new RegExp(keyword, "g");

            switch (tab) {
                case '1':
                    remarkContentEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-highlight'>" + keyword + "</span>");
                    })
                    remarkPersonEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-highlight'>" + keyword + "</span>");
                    })
                    break;

                case '2':
                    remarkPersonEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-highlight'>" + keyword + "</span>");
                    })
                    break;

                case '3':
                    let remarkTagEl = document.querySelectorAll(".r-l-t-tags");
                    remarkTagEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-tag-highlight'>" + keyword + "</span>");
                    })
                    break;
            }
        }
    }
}


function like(e, remarkId, isLike) {
    if (isRun) {
        alert("잠시만 기다려주세요.")
        return false;
    } else {
        isRun = true;
    }
    let likeCountDom = document.getElementById("like-count-" + remarkId),
        dislikeCountDom = document.getElementById("dislike-count-" + remarkId);


    $.ajax({
        type: 'PUT',
        url: `/api/remark/${remarkId}/like/${isLike}`,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (xhr) {

            xhr.setRequestHeader("XMLHttpRequest", "true");
        }
    }).done(function (result) {
        let likeInfo = result;
        likeCountDom.innerText = likeInfo.likeCount;
        dislikeCountDom.innerText = likeInfo.dislikeCount;
        changeLikeIcon();
        setTimeout(function () {
            isRun = false
        }, 300)
    }).fail(function (error) {
        if (error.status == 403) {
            location.href = "/oauth_login";
        } else {
            alert(JSON.stringify(error));
        }
    });


    function changeLikeIcon() {
        let otherBtn = '';
        const child = e.firstElementChild;
        const tooltip = bootstrap.Tooltip.getInstance(child);

        if (e.classList.contains("r-l-t-like-btn")) {
            otherBtn = document.getElementById("dislike-btn-" + remarkId);
            if(e.classList.contains("like-active")) {
                child.setAttribute("data-bs-original-title", "이 발언이 좋아요");
            }else {
                child.setAttribute("data-bs-original-title", "좋아요 취소")
            }
            if (otherBtn.classList.contains("like-active")) {
                otherBtn.classList.toggle("like-active")
                otherBtn.firstElementChild.setAttribute("data-bs-original-title", "이 발언이 싫어요")
            }

        } else {
            otherBtn = document.getElementById("like-btn-" + remarkId);
            if(e.classList.contains("like-active")) {
                child.setAttribute("data-bs-original-title", "이 발언이 싫어요");
            }else {
                child.setAttribute("data-bs-original-title", "싫어요 취소")
            }
            if (otherBtn.classList.contains("like-active")) {
                otherBtn.classList.toggle("like-active")
                otherBtn.firstElementChild.setAttribute("data-bs-original-title", "이 발언이 좋아요")
            }
        }

        tooltip.show();

        e.classList.toggle("like-active");

    }

    function getFirstChild(element) {
        let firstChild = element.firstChild;
        while (firstChild != null && firstChild.nodeType === 3) {
            firstChild = firstChild.nextSibling;
        }
        return firstChild;
    }
}

async function saveOrDeleteBookmark(e, remarkId) {
    if (isRun) {
        alert("잠시만 기다려주세요.")
        return false;
    } else {
        isRun = true;
    }

    const response = await fetch("/api/remark/bookmark", {
        method: "post",
        headers: {
            'XMLHttpRequest': 'true',
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: remarkId
    })
    try{
        if(response.ok){
            toggleBookmarkIcon(e);
            setTimeout(function () {
                isRun = false
            }, 300)
        }else if(response.status===405 || response.status===403){
            location.href = "/oauth_login";
        }
    } catch (error){
        alert(JSON.stringify(error));
    }
}

function toggleBookmarkIcon(e){

    const child = e.firstElementChild;
    const tooltip = bootstrap.Tooltip.getInstance(child) // Returns a Bootstrap tooltip instance

    if(e.classList.contains("bookmark-active")) {
        child.setAttribute("data-bs-original-title", "즐겨찾기")
    }else {
        child.setAttribute("data-bs-original-title", "즐겨찾기 취소")
    }
    tooltip.show();

    e.classList.toggle("bookmark-active");


}



function deleteRemark(id) {
    if (confirm("정말 삭제하시겠습니까? \n삭제한 발언은 복원할 수 없습니다.") === true) {
        $.ajax({
            type: 'DELETE',
            url: '/api/remark/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            alert('발언이 삭제되었습니다.');
            location.href = '/remark';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    } else {
        return;
    }
}

function showMorePersonList(e, page) {
    const keywordDom = document.getElementById('r-l-keyword');
    const categoryDom = document.getElementById('r-l-category');
    const userDom = document.getElementById('r-l-user');

    if (typeof keywordDom !== "undefined" && keywordDom.value !== '') {
        const keyword = keywordDom.value;
        $.ajax({
            type: 'GET',
            url: `/api/search/personList?page=${page}&size=10`,
            data: {
                keyword: keyword,
                page: page
            },
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("XMLHttpRequest", "true");
            }
        }).done(function (result) {
            $('#show-more-btn-container').remove();
            $('#search-person-container').append(toSearchPersonContainer(result));
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }else if(typeof categoryDom !== "undefined" && categoryDom.value === '팔로우'
        && typeof userDom !== "undefined" && userDom.value !== null){

        $.ajax({
            type: 'GET',
            url: `/api/search/followerList?page=${page}&size=10`,
            data: {
                userId: userDom.value,
                page: page
            },
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("XMLHttpRequest", "true");
            }
        }).done(function (result) {
            $('#show-more-btn-container').remove();
            $('#search-person-container').append(toSearchPersonContainer(result));
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
}

function toSearchPersonContainer(pageObj) {
    let list = pageObj.content,
        last = pageObj.last,
        page = pageObj.number,
        row = '';

    list.forEach(person => {
        row += "<div class='r-l-related-person-nav-container'>";
        row += "<a class='r-l-related-person-nav' href='/person/" + person.id + "'>";
        row += "<div class='r-l-related-person-nav-row'>";
        row += "<span class='r-l-related-person-nav-name'>" + person.name + "</span>";
        row += "<span class='r-l-related-person-nav-job'> " + person.job + "</span>";
        row += "</div>";
        row += "</a>";
        row += "</div>";
    })

    if (!last) {
        row += "<div class='r-l-related-person-nav-more-btn-container' id='show-more-btn-container'>";
        row += "<a id='show-more-btn' class='r-l-related-person-nav-more-btn-wrap' onclick='showMorePersonList(this," + (page + 1) + ")'>";
        row += "<span class='r-l-related-person-nav-more-btn-text'>더보기</span>";
        row += "<i class='r-l-related-person-nav-more-btn-icon fas fa-chevron-down'></i>";
        row += "</a>";
        row += "</div>";
    }

    return row;
}



document.addEventListener("DOMContentLoaded", function () {

    highlightKeyword();

    const quoteIcon = "<i class='r-l-t-quote-icon fas fa-quote-right'></i>";

    const contents = document.querySelectorAll(".r-l-t-content");

    contents.forEach(c => {
        const sourceIcon = "<button class='tt r-l-t-source-btn' data-bs-html='true' data-bs-placement='bottom' data-bs-trigger='click' " +
            "title='<a class=\"r-l-t-source-link\" href=\""+c.dataset.source+"\" target=\"_blank\" rel=\"noopener noreferer nofollow\">"+c.dataset.source+"</a>'</a>"+
            "<i class='r-l-t-source-btn-icon fas fa-link'></i>출처</button>";

        $(c).append(quoteIcon)
        $(c).append(sourceIcon)

        c.addEventListener("click", function (e){
            location.href = c.nextElementSibling.getAttribute('href');
        })
    });

    const tooltips = document.querySelectorAll(".tt");

    tooltips.forEach(t => {
        new bootstrap.Tooltip(t)
    });

    const sourceBtn = document.querySelectorAll(".r-l-t-source-btn");
    sourceBtn.forEach(s => {
        s.addEventListener("click", function (e){
            e.preventDefault();
            e.stopPropagation();
        })
    })


})