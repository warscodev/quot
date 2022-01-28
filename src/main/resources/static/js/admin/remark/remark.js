let isRun = false;


function highlightKeyword() {
    const keywordEl = document.querySelector("#r-l-keyword");
    if (keywordEl) {
        let keyword = keywordEl.value;

        if (keyword != null && keyword !== "" && keyword !== undefined && keyword !== '.') {
            let tab = document.querySelector("#r-l-tab").value,
                remarkContentEl = document.querySelectorAll(".r-l-t-content"),
                remarkPersonEl = document.querySelectorAll(".r-l-t-person-name"),
                remarkTagEl = document.querySelectorAll(".r-l-t-tags"),
                regex = new RegExp(keyword.replace("."), "g");

            switch (tab) {
                case '1':
                    remarkContentEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-highlight'>" + keyword + "</span>");
                    })
                    remarkPersonEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-highlight'>" + keyword + "</span>");
                    })
                    remarkTagEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-tag-highlight'>" + keyword + "</span>");
                    })
                    break;

                case '2':
                    remarkPersonEl.forEach(e => {
                        e.innerHTML = e.innerHTML.replace(regex, "<span class='search-highlight'>" + keyword + "</span>");
                    })
                    break;

                case '3':
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
        /*const tooltip = bootstrap.Tooltip.getInstance(child);*/

        if (e.classList.contains("r-l-t-like-btn")) {
            otherBtn = document.getElementById("dislike-btn-" + remarkId);
            /*if(e.classList.contains("like-active")) {
                child.setAttribute("data-bs-original-title", "좋아요");
            } else {
                child.setAttribute("data-bs-original-title", "좋아요 취소")
            }*/
            if (otherBtn.classList.contains("like-active")) {
                otherBtn.firstElementChild.firstElementChild.classList.toggle('fill');
                otherBtn.classList.toggle("like-active")
                /*otherBtn.firstElementChild.setAttribute("data-bs-original-title", "싫어요")*/
            }

        } else {
            otherBtn = document.getElementById("like-btn-" + remarkId);
            /*if(e.classList.contains("like-active")) {
                child.setAttribute("data-bs-original-title", "싫어요");
            }else {
                child.setAttribute("data-bs-original-title", "싫어요 취소")
            }*/
            if (otherBtn.classList.contains("like-active")) {
                otherBtn.firstElementChild.firstElementChild.classList.toggle('fill');
                otherBtn.classList.toggle("like-active")
                /*otherBtn.firstElementChild.setAttribute("data-bs-original-title", "좋아요")*/
            }


        }

        child.firstElementChild.classList.toggle('fill');

        /*tooltip.show();*/

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
    /*const tooltip = bootstrap.Tooltip.getInstance(child)*/ // Returns a Bootstrap tooltip instance

    /*if(e.classList.contains("bookmark-active")) {
        child.setAttribute("data-bs-original-title", "스크랩")
    }else {
        child.setAttribute("data-bs-original-title", "스크랩 취소")
    }*/
    /*tooltip.show();*/

    e.classList.toggle("bookmark-active");
    child.firstElementChild.classList.toggle('fill');

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

function setCookie(name, value, exp) {
    const date = new Date();
    date.setTime(date.getTime() + exp*24*60*60*1000);
    document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
}

function getCookie(name){
    const value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

function makeBackUrl(){
    setCookie("listBtnUrl", location.href, 1);
}

function goToList(){
    const listBtnUrl = getCookie("listBtnUrl");
    if(listBtnUrl != null){
        location.href= listBtnUrl;
    } else{
        location.href = "/remark"
    }
}

function setAdsBetweenRemarks(){
    if(document.querySelectorAll(".r-l-t-row-container.row") != null){


        const remarkRowElements = document.querySelectorAll(".r-l-t-row-container.row"),
            numberOfRows = remarkRowElements.length;

        if(numberOfRows > 2){

            const adContainer = document.createElement("div"),
                adDom = document.createElement("ins"),
                adScript = document.createElement("script"),
                indexForAd = Math.ceil(numberOfRows/2) -1;

            adContainer.setAttribute("class", "r-l-t-ads-container");

            adDom.setAttribute("class", "adsbygoogle between");
            adDom.style.display = "block";
            adDom.setAttribute("data-ad-client", "ca-pub-5145635293228409");
            adDom.setAttribute("data-ad-slot", "3028451032");
            adDom.setAttribute("data-ad-layout-key", "-gw-3+1f-3d+2z");
            adDom.setAttribute("data-ad-format", "fluid");

            adScript.innerHTML = "(adsbygoogle = window.adsbygoogle || []).push({});";

            adContainer.appendChild(adDom);
            adContainer.appendChild(adScript);

            remarkRowElements[indexForAd].after(adContainer);
        }
    }

}



document.addEventListener("DOMContentLoaded", function () {

    setAdsBetweenRemarks();

    highlightKeyword();

    const quoteIcon = "<i class='r-l-t-quote-icon fas fa-quote-right'></i>";

    const contents = document.querySelectorAll(".r-l-t-content, .r-d-t-content");

    contents.forEach(c => {
        c.addEventListener("click", function (e){
            makeBackUrl();
            location.href = c.nextElementSibling.getAttribute('href');
        })

        $(c).append(quoteIcon)
    });

    const tooltips = document.querySelectorAll(".tt");

    tooltips.forEach(t => {
        new bootstrap.Tooltip(t)
    });

})