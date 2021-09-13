var main = {
    init: function () {
        let _this = this,
            page = 0,
            oldHstate = history.state;
        hstate = history.state;


        if (document.getElementById("search-pageNum")) {
            page = document.getElementById("search-pageNum").value;
        }


        /* 검색결과인 경우 관련 인물 리스트 불러오기 */
        if (document.getElementById("search-keyword")) {
            _this.loadPerson();
        }

        /* 코멘트 불러오기 */
        if (hstate != null && hstate.page) {
            _this.loadRemark(hstate.page);
        } else {
            _this.loadRemark(page);
        }

        /* 페이징 */
        $(document).on("click", ".page-item a", (function (e) {
            let scrollPosition = $("#remark-scroll-position").offset();
            $('html, body').animate({scrollTop: (0)});
            let num = $(this).attr("page");
            _this.loadRemark(num);
        }));

    },

    loadPerson: async function () {
        let _this = this,
            data = '',
            keyword = document.getElementById("search-keyword").value,
            personTable = document.getElementById("person-table");

        await $.get(`/api/search/personList?keyword=${keyword}`,
            function (result) {
                data = result;
                $(personTable).empty();
                $(personTable).append(_this.toPersonTable(data));
            }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    toPersonTable: function (personList) {
        let titleDiv = document.getElementById("person-related-title"),
            title = '',
            row = '';

        title += "<h6>관련 인물</h6>"
        $(titleDiv).append(title);

        if (personList.length > 0) {
            personList.forEach(person => {
                row +=
                    "<div><a href='/remark/search?keyword=" + person.name + "&personId=" + person.id + "&tab=2'>" +
                    "<div class='person-related-header'>" +
                    "<span class='person-category'>" +
                    "<svg xmlns='http://www.w3.org/2000/svg' width='13' height='13' fill='currentColor' class='bi bi-tags' viewBox='0 1 16 16'>\n" +
                    "  <path d='M3 2v4.586l7 7L14.586 9l-7-7H3zM2 2a1 1 0 0 1 1-1h4.586a1 1 0 0 1 .707.293l7 7a1 1 0 0 1 0 1.414l-4.586 4.586a1 1 0 0 1-1.414 0l-7-7A1 1 0 0 1 2 6.586V2z'/>\n" +
                    "  <path d='M5.5 5a.5.5 0 1 1 0-1 .5.5 0 0 1 0 1zm0 1a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3zM1 7.086a1 1 0 0 0 .293.707L8.75 15.25l-.043.043a1 1 0 0 1-1.414 0l-7-7A1 1 0 0 1 0 7.586V3a1 1 0 0 1 1-1v5.086z'/>\n" +
                    "</svg>" + person.category + "</span></div>" +
                    "<div class='person-related-body' '>" +
                    "<span class='person-name'>" + person.name + "</span>" +
                    "<span class='person-job'>" + person.job + "</span>" +
                    "</div>" +
                    "</a></div>";
            })
            row += "</ul>";
        }
        return row;
    },

    loadRemark: async function (page) {
        const size = 10;
        let _this = this,
            keyword = "",
            tab = 1,
            personId = 0,
            sort="",
            url = '',
            data = '',
            remarkTable = document.getElementById("remark-table");



        document.getElementById("search-pageNum").value = page;


        if (document.getElementById("search-tab")) {
            tab = document.getElementById("search-tab").value;
        }

        if (document.getElementById("search-keyword")) {
            keyword = document.getElementById("search-keyword").value;
            url = `/remark/search?keyword=${keyword}&tab=${tab}&page=${parseInt(page) + 1}`;
        } else {
            url = `/remark?page=${parseInt(page) + 1}`;
        }

        if (document.getElementById("search-personId")) {
            personId = document.getElementById("search-personId").value;
        }

        if (document.getElementById("remark-sorting")){
            sort = document.getElementById("remark-sorting").value;
        }

        let remarkSearchCondition =
            {
                keyword: keyword,
                tab: tab,
                personId: personId,
                sort: sort
            };

        await $.get(`/api/search/remark?page=${page}&size=${size}`,
            remarkSearchCondition
            , function (result) {
                data = result;
                $(remarkTable).empty();
                $("#remark-table").append(_this.toRemarkTable(data.remarkList));
                $(".remark-pagination").empty();
                $(".remark-pagination").append(_this.pagination(data.pageMetadata));
                $(".remark-page-count").empty();

                if (data.pageMetadata.totalPages > 0) {
                    $(".remark-page-count").append(_this.subPagination(data.pageMetadata));
                }

                main.oldHstate = history.state;
                history.pushState({'page': page}, '', url);

                /* 툴팁 활성화 */
                const tooltips = document.querySelectorAll(".tt")
                tooltips.forEach(t => {
                    new bootstrap.Tooltip(t)
                })


            }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },

    toRemarkTable: function (list) {
        let row = "";



        if (!list.length == 0) {
            let i = 1;
            list.forEach(remark => {
                <!-- 코멘트 컨테이너 -->
                row += "<div class='remark-row container' id='remark-row-" + i++ + "' data-remark-id='" + remark.remarkId + "'>";



                /* 코멘트 1열 프로필 - 발언자 - 태그 */
                row +="<div class='row remark-row-first'>";

                row +="<div class='col-1 remark-col-first d-flex justify-content-end'>";
                /* 프로필 이미지 */
                row +="<div class='remark-profile-icon-wrap'>";
                row +="<a class='remark-profile-icon-link d-flex align-items-center justify-content-center' href='/remark/search?keyword=" + remark.person.name + "&personId=" + remark.person.id + "&tab=2'>";
                row +="<i class='fas fa-user-alt remark-profile-icon' style='font-size: 1.4rem; color: white'></i>";
                row +="</a>";
                row +="</div>";

                row +="</div>";

                /* 임시 */
                row += "<div class='col'>";
                <!-- 타이틀 -->
                row += "<div class='remark-detail-title-wrap'>";

                <!-- 발언인 -->
                row += "<div class='remark-detail-title-person-wrap d-flex align-items-baseline mb-1' style='flex-wrap : wrap;'>";
                row += "<a class='remark-detail-title-person-name-link' href='/remark/search?keyword=" + remark.person.name + "&personId=" + remark.person.id + "&tab=2'>";
                row += "<span class='remark-detail-title-person-name-list'>" + remark.person.name + "</span></a>";
                row += "<span class='remark-detail-title-person-job-list'>" + remark.person.job + "</span>";
                row += "<span class='' style='font-size: .813rem;'>｜</span>";
                row += "<span class='remark-detail-title-date remark-detail-title-date-list'>" + remark.remarkDate_format+" </span></div>";

                row += "</div>";


                <!-- 태그 -->
                row += "<div class='d-flex align-items-top'>";
                row += "<div class='remark-tag'>";
                if (remark.tags.length > 0) {
                    remark.tags.forEach(tag => {
                        row +=
                            "<a href='/remark/search?keyword=" + tag.name + "&tab=3'><span>#" + tag.name + "</span></a>"
                    });
                }
                row += "</div>";
                row += "</div>";
                <!-- 태그 끝 -->

                row += "</div>";

                <!-- 글 정보 시작 -->
                row += "<div class='col-1 d-flex justify-content-end'>";
                row += "<span class='remark-info tt' data-bs-html='true' data-bs-placement='bottom' title='<div class= \"d-flex align-items-center justify-content-center\"><img src=" + remark.user.picture + " width=20 height=20 class=\"rounded-circle remark-info-picture\"><span class=\"remark-info-nickname\">" + remark.user.nickname + "</span></div> <div><span class=remark-info-date>" + remark.createdDate + " 등록됨</span></div>'>";
                row += "<i class='remark-info-icon fas fa-info-circle'></i></span>";
                row += "</div>";

                row += "</div>";
                <!-- 1열 끝 -->



                /* 코멘트 2열 따옴표 - 본문 */
                row +="<div class='row remark-row-second''>";

                row += "<div class='col-1 d-flex remark-col-first justify-content-end'>";

                row += "<div>"
                row += "<i class='fas fa-quote-left remark-quote-icon' style='margin-right: 0'></i>"
                row += "</div>";

                row += "</div>";


                row += "<div class='col'>";

                <!-- 발언 내용 -->
                row += "<div class='remark-content'><a href='/remark/" + remark.remarkId + "'>" +
                    "<pre style='margin-bottom: 0'><p>" + remark.content + "<i class='fas fa-quote-right remark-quote-icon'></i></p></pre></a></div>";

                /* 등록일 */
                /*row += "<div class='remark-created-date-wrap d-flex justify-content-end'>";
                row += "<span class='remark-created-date'>" + remark.createdDate + " 등록됨</span>";
                row += "</div>";*/

                row += "</div>";
                row += "</div>";


                <!-- 하단 버튼 -->

                row += "<div class='remark-center-bottom-wrap d-flex justify-content-center'>";

                row += "<div class='like-icon-container remark-bottom-icon-containers' style='margin-left: 0rem;'>";
                row += "<a href='javascript:;' class='btn like-btn' data-islike='1' onclick='main.like(this," + remark.remarkId + ")'>";
                row += "<i class='far fa-thumbs-up like-icon remark-bottom-icon'></i>";
                /*row += "<img src='/css/icon/smile.png' width='25px' height='25px'>";*/

                row += "<span id='like-count-" + remark.remarkId + "' class='like-count remark-bottom-icon-text'>" + remark.likeCount + "</span></a></div>";

                row += "<div class='like-icon-container remark-bottom-icon-containers'>";
                row += "<a href='javascript:;' class='btn dislike-btn' data-islike='-1' onclick='main.like(this," + remark.remarkId + ")'>";
                row += "<i class='far fa-thumbs-down dislike-icon remark-bottom-icon'></i>";
                /*row += "<img src='/css/icon/angry.png' width='25px' height='25px'>";*/
                row += "<span id='dislike-count-" + remark.remarkId + "' class='dislike-count remark-bottom-icon-text'>" + remark.dislikeCount + "</span></a></div>";


                <!-- 공유 -->
                row += "<div class='share-icon-container remark-bottom-icon-containers d-flex align-items-center'>";
                row += "<a id='share-btn' class='btn share-btn' href='' onclick='openShareModal(this," + remark.remarkId + ")' data-name='" + remark.person.name + "' data-date='" + remark.remarkDate_format + "' data-bs-toggle='modal' data-bs-target='#shareModal'>";
                row += "<div class='d-flex align-items-center'>"
                row += "<i class='remark-bottom-icon fas fa-share'></i><span class='remark-bottom-icon-text remark-bottom-icon-text-right'>공유</span></div></a></div>";


                <!-- 출처 -->
                if (!remark.sourceSort == "") {
                    row += "<div class='source-icon-container remark-bottom-icon-containers d-flex align-items-center'>";
                    row += "<a class='btn source-btn' href='" + remark.sourceUrl + "'  target='_blank' rel='noopener'>";
                    row += "<div class='d-flex align-items-center'>"
                    if (remark.sourceSort == "영상") {
                        row += "<i class='remark-bottom-icon fas fa-play-circle'></i>";
                    } else if (remark.sourceSort == "SNS") {
                        row += "<i class='remark-bottom-icon fas fa-mobile-alt'></i>";
                    } else if (remark.sourceSort == "기사") {
                        row += "<i class='remark-bottom-icon fas fa-newspaper'></i>";
                    }
                    row += "<span class='remark-bottom-icon-text remark-bottom-icon-text-right'>출처</span></div></a>";
                    row += "</div>";
                }



                /*row += "</div>";
                row += "</div>";*/


                row += "</div>";


                row += "</div>";


            });

        } else {
            row +=
                "<div class='remark-row remark-empty pt-5 pb-5'><h6 class='text-center'> 검색 결과가 없습니다. </h6></div>";
        }


        return row;
    },


    pagination: function (pageMetadata) {

        let nav = "",
            totalPages = pageMetadata.totalPages,
            totalElements = pageMetadata.totalElements,
            size = pageMetadata.size,
            page = pageMetadata.number,
            blockSize = 5,
            startBlock = Math.floor(page / blockSize) * blockSize,
            endBlock = startBlock + blockSize;

        main.page = page;

        if (endBlock > totalPages) {
            endBlock = totalPages;
        }

        nav += "<nav aria-label='Page navigation'>";
        nav += "<ul class='pagination pagination-sm d-flex' id='pagination-ul'>";


        if (!pageMetadata.first && totalElements != 0) {
            nav += "<li class='page-item'><a class='page-link' aria-label='Previous' page='" + (page - 1) + "'><span aria-hidden='true'><i class='bi bi-chevron-left'></i>" +
                "</svg></span></a></li>";
        } else if (pageMetadata.first && totalElements != 0) {
            nav += "<li class='page-item disabled'><a class='page-link' aria-label='Previous' page='" + (page - 1) + "'><span aria-hidden='true'><i class='bi bi-chevron-left'></i>" +
                "</svg></span></a></li>";
        } else {

        }

        if (!startBlock == 0) {
            nav += "<li class='page-item'><a class='page-link' page='0'>1</a></li>";
            nav += "<li class='page-item disabled'><span class='page-link' style='cursor:default'>...</span></li>";
        }

        for (let num = startBlock; num < endBlock; num++) {
            if (num == page) {
                nav += "<li class='page-item active'><span class='page-link' style='cursor:default'>" + (num + 1) + "</span></li>";
            } else {
                nav += "<li class='page-item'><a class='page-link' page='" + num + "'>" + (num + 1) + "</a></li>";
            }
        }

        if (!pageMetadata.last && totalElements != 0) {
            nav += "<li class='page-item'><a class='page-link' aria-label='Next' page='" + (page + 1) + "'><span aria-hidden='true'><i class='bi bi-chevron-right'></i>" +
                "</svg></span></a></li>";
        } else if (pageMetadata.last && totalElements != 0) {
            nav += "<li class='page-item disabled'><a class='page-link' aria-label='Next' page='" + (page + 1) + "'><span aria-hidden='true'><i class='bi bi-chevron-right'></i>" +
                "</svg></span></a></li>";

        }

        nav += "</ul></nav>"


        return nav;

    },

    subPagination: function (pageMetadata) {
        let nav = "";
        let page = main.page;

        if (!pageMetadata.first && pageMetadata.totalElements != 0) {
            nav += "<a class='page-link d-inline' aria-label='Previous' page='" + (page - 1) + "'><i class='fas fa-chevron-left'></i>" +
                "</a>";
        } else if (pageMetadata.first && pageMetadata.totalElements != 0) {
            nav += "<span class='page-link d-inline' aria-label='Previous'><i class='fas fa-chevron-left'></i>" +
                "</span>";
        }

        nav += "<span style='margin: 0 !important;'>" + (pageMetadata.number + 1) + " / " + pageMetadata.totalPages + " 페이지</span>";

        if (!pageMetadata.last && pageMetadata.totalElements != 0) {
            nav += "<a class='page-link d-inline' aria-label='Next' page='" + (page + 1) + "'><i class='fas fa-chevron-right'></i>" +
                "</a>";
        } else if (pageMetadata.last && pageMetadata.totalElements != 0) {
            nav += "<span class='page-link d-inline' aria-label='Next'><i class='fas fa-chevron-right'></i>" +
                "</span>";
        }


        return nav;


    },

    delete: async function (id) {

        if (confirm("정말 삭제하시겠습니까? \n삭제한 발언은 복원할 수 없습니다.") == true) {

            $.ajax({
                type: 'DELETE',
                url: '/api/remark/' + id,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
            }).done(function () {
                let row = $('div[data-remark-id=' + id + ']');
                row.remove();
                alert('발언이 삭제되었습니다.');
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        } else {
            return;
        }
    },

    like: function (e, remarkId) {

        let isRun = false;
        if(isRun==true){
            return false;
        }

        if (document.getElementById("user_id")) {


            let isLike = parseInt(e.dataset.islike),
                /*userId = document.getElementById("user_id").value,*/
                likeCountDom = document.getElementById("like-count-" + remarkId),
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
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        } else {
            location.replace("/login");
        }
    }


}
main.init();