var main = {
    init: function () {
        let _this = this,
            page = 0;

        /* 검색결과인 경우 관련 인물 리스트 불러오기 */
        if (document.getElementById("search-keyword")) {
            console.log("키워드 있음!")
            _this.loadPerson();
        }else{
            console.log("키워드 없음")
        }

        /* 코멘트 불러오기 */
        _this.loadComment(page);

        
        /* 페이징 */
        $(document).on("click", "#pagination-ul li a", (function (e) {
            let scrollPosition = $("#listTableBody").offset();
            let num = $(this).attr("page");
            _this.loadComment(num);
            $('html, body').animate({scrollTop: scrollPosition.top}, 300);
        }));

    },

    loadPerson: async function () {
        let _this = this,
            data = '',
            keyword = document.getElementById("search-keyword").value;
            console.log(keyword);
            personTable = document.getElementById("person-table");

        await $.get(`/api/personList?keyword=${keyword}`,
            function (result) {
                data = result;
                $(personTable).empty();
                $(personTable).append(_this.toPersonTable(data));
            }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    toPersonTable: function (personList) {

        let row = '';

        if (personList.length > 0) {
                row += "<ul>"
            personList.forEach(person => {
                row +=
                    "<li><a href='/admin/comment/search?keyword=" + person.name + "&personId=" + person.id + "&tab=2'>" +
                    "<div class='person-related-header'><span class='person-category'>" + person.category + "</span></div>" +
                    "<div class='person-related-body' '>" +
                    "<span class='person-name'>" + person.name + "</span>" +
                    "<span class='person-job'>" + person.job + "</span>" +
                    "</div>" +
                    "</a></li>";
            })
            row += "</ul>";
        }
        return row;
    },

    loadComment: async function (page) {
        
        console.log("loadComment 실행")

        const size = 10;
        let _this = this,
            keyword = "",
            tab = 1,
            personId = 0,

            data = '',
            commentTable = document.getElementById("comment-table");


        if (document.getElementById("search-tab")) {
            tab = document.getElementById("search-tab").value;
        }

        if (document.getElementById("search-keyword")) {
            keyword = document.getElementById("search-keyword").value;
        }

        if (document.getElementById("search-personId")) {
            personId = document.getElementById("search-personId").value;
        }

        let commentSearchCondition =
            {
                keyword: keyword,
                tab: tab,
                personId: personId
            };

        await $.get(`/api/comment?page=${page}&size=${size}`,
            commentSearchCondition
            , function (result) {
                data = result;
                $(commentTable).empty();
                $("#comment-table").append(_this.toCommentTable(data.commentList));
                $(".comment-pagination").empty();
                $(".comment-pagination").append(_this.pagination(data.pageMetadata));
            }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },

    toCommentTable: function (list) {
        let row = "";

        if (!list.length == 0) {
            let i = 1;
            list.forEach(comment => {
                row += "<div class='comment-row' id='comment-row-" + i++ + "' data-comment-id='" + comment.commentId + "' style='padding: 1.5rem;'>";

                <!-- 태그 -->
                if (comment.tags.length > 0) {
                    row += "<div class='comment-tag'>"
                    comment.tags.forEach(tag => {
                        row +=
                            "<a href='/admin/comment/search?keyword=" + tag.name + "&tab=3'><span class='me-2'>#" + tag.name + "</span></a>"
                    });
                    row += "</div>"
                }

                row +=
                    <!-- 발언 내용 -->
                    "<div class='comment-content mt-2'>" +
                    "<pre style='margin-bottom: 0'><p>" + comment.content + "</p></pre></div>" +

                    <!-- 출처 & 발언날짜 컨테이너 -->
                    "<div class='d-flex justify-content-between'>" +

                    <!-- 출처 -->
                    "<div class='comment-source d-flex align-items-center' style='margin : 0 0 0 0.3rem;'>" +
                    "<a href='" + comment.sourceUrl + "'  target='_blank'><svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-link-45deg' viewBox='0 0 16 16'>\n" +
                    "  <path d='M4.715 6.542 3.343 7.914a3 3 0 1 0 4.243 4.243l1.828-1.829A3 3 0 0 0 8.586 5.5L8 6.086a1.002 1.002 0 0 0-.154.199 2 2 0 0 1 .861 3.337L6.88 11.45a2 2 0 1 1-2.83-2.83l.793-.792a4.018 4.018 0 0 1-.128-1.287z'/>\n" +
                    "  <path d='M6.586 4.672A3 3 0 0 0 7.414 9.5l.775-.776a2 2 0 0 1-.896-3.346L9.12 3.55a2 2 0 1 1 2.83 2.83l-.793.792c.112.42.155.855.128 1.287l1.372-1.372a3 3 0 1 0-4.243-4.243L6.586 4.672z'/>\n" +
                    "</svg>" + comment.sourceSort + "</a>" +
                    "</div>" +

                    <!-- 발언 날짜 -->
                    "<div>" +
                    "<div class='comment-date d-flex justify-content-end' style='margin : 0 1.2rem 0 0;'><span class='text-muted'>" + comment.commentDate_format + "</span></div>" +

                    <!-- 발언인 -->
                    "<div class='comment-person d-flex justify-content-end' style='margin : 0 1.2rem 0 0;'>" +
                    "<span class='text-muted person-job'>" + comment.person.job + "</span>" +
                    "<span style='margin : 0 0 0 0.3rem;'><a class='person-name' href='/admin/person/" + comment.person.id + "'>" + comment.person.name + "</a></span></div>" +
                    "</div></div>";

                <!-- 등록일 -->
                row +=
                    "<div class='d-flex justify-content-between pt-2' style='border-top: 0.05rem solid rgba(0,0,0,.125);\n" +
                    "    margin-top: 1rem;'>" +
                    "<div class='pt-1'>" +
                    "<div class='col-xs-6'><span><small class='text-muted'>" + comment.createdDate + " 등록됨</small></span></div>";

                <!-- 수정일 -->
                if (comment.status == 'UPDATED') {
                    row +=
                        "<div class='col-xs-6'><span><small class='text-muted'>" + comment.updatedDate + " 수정됨</small></span></div></div>";

                } else {
                    row +=
                        "</div>";
                }

                row +=
                    "<div class='d-flex align-items-center'>" +
                    "<a class='btn btn-outline-secondary btn-sm me-md-2' href='/admin/comment/" + comment.commentId + "'>수정</a>" +
                    "<button class='btn btn-outline-danger btn-sm btn-delete' onclick='main.delete(" + comment.commentId + ")'>삭제</button>" +
                    "<input type='hidden' name='commentId' value='" + comment.commentId + "'></div>" +
                    "</div></div>";


            });

        } else {
            row +=
                "<div class='comment-row comment-empty pt-5 pb-5'><h6 class='text-center'> 검색 결과가 없습니다. </h6></div>";
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
        nav += "<ul class='pagination pagination-sm' id='pagination-ul'>";

        if (!totalElements == 0 && totalPages > 1) {
            if (!pageMetadata.first) {
                nav += "<li class='page-item'><a class='page-link' aria-label='Previous' page='" + (page - 1) + "'><span aria-hidden='true'>&lt;</span></a></li>";
            } else {
                nav += "<li class='page-item disabled'><a class='page-link' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>";
            }
        }

        if (!startBlock == 0) {
            nav += "<li class='page-item'><a class='page-link' page='0'>1</a></li>";
            nav += "<li class='page-item active disabled'><span class='page-link' style='cursor:default'>...</span></li>";
        }

        for (let num = startBlock; num < endBlock; num++) {
            if (num == page) {
                nav += "<li class='page-item active'><span class='page-link' style='cursor:default'>" + (num + 1) + "</span></li>";
            } else {
                nav += "<li class='page-item'><a class='page-link' page='" + num + "'>" + (num + 1) + "</a></li>";
            }
        }

        if (totalPages > 1) {
            if (!pageMetadata.last) {
                nav += "<li class='page-item'><a class='page-link' aria-label='Next' page='" + (page + 1) + "'><span aria-hidden='true'>&gt;</span></a></li>";
            } else {
                nav += "<li class='page-item disabled'><a class='page-link' aria-label='Next'><span aria-hidden='true'>&gt;</span></a></li>";
            }
        }

        nav += "</ul></nav>"


        return nav;

    },

    delete: async function (id) {

        if (confirm("정말 삭제하시겠습니까? \n삭제한 발언은 복원할 수 없습니다.") == true) {

            $.ajax({
                type: 'DELETE',
                url: '/admin/comment/' + id,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
            }).done(function () {
                let row = $('div[data-comment-id=' + id + ']');
                row.remove();
                alert('발언이 삭제되었습니다.');
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        } else {
            return;
        }
    },

    searchKeyword: function (keyword, tab) {
        $('.comment-content:contains')

    },

    pagingClick: function (num) {

        console.log("온클릭");
        const _this = this;
        _this.loadComment(num);
    }


}
main.init();