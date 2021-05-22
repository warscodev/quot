var main = {
    init: function () {
        let _this = this,
            page = 0;

        _this.loadComment(page);

        $(document).on("click","#pagination-ul li a",(function (e) {
            let scrollPosition = $("#listTableBody").offset();
            //e.preventDefault();
            let num = $(this).attr("page");
            _this.loadComment(num);
            $('html, body').animate({scrollTop : scrollPosition.top}, 300);
        }));

    },

    loadComment: async function (page) {


        const size = 10;
        let _this = this,
            keyword = "",
            tab = 1,
            personId = 0,

            data = '',
            tableBody = document.getElementById("listTableBody");


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
                $(tableBody).empty();
                $("tbody").append(_this.toTrList(data.commentList));
                $(".comment-pagination").empty();
                $(".comment-pagination").append(_this.pagination(data.pageMetadata));
            }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },

    toTrList: function (list) {
        let trList = "";

        if (!list.length == 0) {
            list.forEach(comment => {

                trList +=
                    "<tr class='comment-row' id='comment-row-" + comment.commentId + "'>" +
                    "<td class='p-3'>" +
                    <!-- 발언인 -->
                    "<div class='comment-person'><a class='person-name' href='/admin/person/" + comment.person.id + "'>" + comment.person.name + "</a></div>" +
                    <!-- 발언 날짜 -->
                    "<div class='comment-date'><small class='text-muted'>" + comment.commentDate_format + "</small></div>" +
                    <!-- 발언 내용 -->
                    "<div class='comment-content mt-2'>" +
                    "<pre><p>" + comment.content + "</p></pre></div>";

                <!-- 태그 -->
                if (comment.tags.length > 0) {
                    trList += "<div>"
                    comment.tags.forEach(tag => {
                        trList +=
                            "<a href='/admin/comment/search?keyword=" + tag.name + "&tab=3'><span class='badge bg-light text-dark me-1'>" + tag.name + "</span></a>"
                    });
                    trList += "</div>"
                }

                <!-- 등록일 -->
                trList +=
                    "<div class='d-flex justify-content-between'>" +
                    "<div class='pt-1'>" +
                    "<div class='col-xs-6'><span><small class='text-muted'>" + comment.createdDate + " 등록됨</small></span></div>";

                <!-- 수정일 -->
                if (comment.status == 'UPDATED') {
                    trList +=
                        "<div class='col-xs-6'><span><small class='text-muted'>" + comment.updatedDate + " 수정됨</small></span></div></div>";

                } else {
                    trList +=
                        "</div>";
                }

                trList +=
                    "<div>" +
                    "<a class='btn btn-outline-secondary btn-sm me-md-2' href='/admin/comment/" + comment.commentId + "'>수정</a>" +
                    "<button class='btn btn-outline-danger btn-sm btn-delete' onclick='main.delete(" + comment.commentId + ")'>삭제</button>" +
                    "<input type='hidden' name='commentId' value='" + comment.commentId + "'></div>" +
                    "</div></td></tr>";


            });

        } else {
            trList +=
                "<div class='pt-5 pb-5'><h6 class='text-center'> 검색 결과가 없습니다. </h6></div>";
        }


        return trList;
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

        if (!totalElements == 0) {
            if (!pageMetadata.first) {
                nav += "<li class='page-item'><a class='page-link' aria-label='Previous' page='"+ (page - 1) +"'><span aria-hidden='true'>&lt;</span></a></li>";
            } else {
                nav += "<li class='page-item disabled'><a class='page-link' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>";
            }

            if (!startBlock == 0) {
                nav += "<li class='page-item'><a class='page-link' page='0'>1</a></li>";
                nav += "<li class='page-item active disabled'><span class='page-link' style='cursor:default'>...</span></li>";
            }

            for (let num = startBlock; num < endBlock; num++) {
                if (num == page) {
                    nav += "<li class='page-item active'><span class='page-link' style='cursor:default'>" + (num + 1) + "</span></li>";
                } else {
                    nav += "<li class='page-item'><a class='page-link' page='"+ num +"'>" + (num + 1) + "</a></li>";
                }
            }

            if (!pageMetadata.last) {
                nav += "<li class='page-item'><a class='page-link' aria-label='Next' page='"+ (page + 1) +"'><span aria-hidden='true'>&gt;</span></a></li>";
            } else {
                nav += "<li class='page-item disabled'><a class='page-link' aria-label='Next'><span aria-hidden='true'>&gt;</span></a></li>";
            }

            nav += "</ul></nav>"
        }

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
                let commentRowId = "comment-row-" + id;
                let row = document.getElementById(commentRowId);
                row.parentNode.removeChild(row);
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