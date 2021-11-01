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
        }, 500)
    }).fail(function (error) {
        if (error.status == 403) {
            location.href = "/oauth_login";
        } else {
            alert(JSON.stringify(error));
        }
    });


    function changeLikeIcon() {
        let otherBtn = '';

        if (e.classList.contains("r-l-t-like-btn")) {
            otherBtn = document.getElementById("dislike-btn-" + remarkId);
        } else {
            otherBtn = document.getElementById("like-btn-" + remarkId);
        }

        e.classList.toggle("like-active");
        if (otherBtn.classList.contains("like-active")) {
            otherBtn.classList.toggle("like-active")
        }
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
    await fetch("/api/remark/bookmark", {
        method: "post",
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: remarkId
    })
    try{
        toggleBookmarkIcon(e);
    } catch (error){
        alert(JSON.stringify(error));
    }
}

function toggleBookmarkIcon(e){
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

    if (typeof keywordDom !== "undefined" && keywordDom.value !== '') {

        let keyword = keywordDom.value;

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

    const remarkLinks = document.querySelectorAll(".r-l-t-content-link");
        remarkLinks.forEach(r =>{
            r.addEventListener("click" ,function(e){
                e.preventDefault();
                e.stopPropagation();
                alert("이동");
                location.href = r.getAttribute('href');
            })
        });

    const quoteIcon = "<i class='r-l-t-quote-icon fas fa-quote-right' style='margin-right: .75rem'></i>";

    const contents = document.querySelectorAll(".r-l-t-content");

    contents.forEach(c => {
        const sourceIcon = "<a class='tt r-l-t-source-btn' data-bs-html='true' data-bs-placement='right' data-bs-trigger='focus click' " +
            "title='<a class=\"r-l-t-source-link\" href=\""+c.dataset.source+"\" target=\"_blank\" rel=\"noopener noreferer nofollow\">"+c.dataset.source+"</a>'</a>"+
            "<i class='r-l-t-source-btn-icon fas fa-link'></i>출처</a>";

        $(c).append(quoteIcon)
        $(c).append(sourceIcon)

        c.addEventListener("click", function (e){
            e.preventDefault();
            e.stopPropagation();
            location.href = c.nextElementSibling.getAttribute('href');
        })
    });

    const sourceBtn = document.querySelectorAll(".r-l-t-source-btn");
    sourceBtn.forEach(s => {
        s.addEventListener("click", function (e){
            e.preventDefault();
            e.stopPropagation();
        })
    })

    const tooltips = document.querySelectorAll(".tt");

    tooltips.forEach(t => {
        new bootstrap.Tooltip(t)
    });


    highlightKeyword();
})

