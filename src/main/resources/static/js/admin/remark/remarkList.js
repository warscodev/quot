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
            keyword = document.getElementById("search-keyword").value;
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

        let remarkSearchCondition =
            {
                keyword: keyword,
                tab: tab,
                personId: personId
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
                /*if (main.oldHstate != null) {
                } else {
                }*/
                history.pushState({'page': page}, '', url);

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
                row += "<div class='remark-row' id='remark-row-" + i++ + "' data-remark-id='" + remark.remarkId + "'>";

                <!-- 태그 -->
                row += "<div class='remark-first-row d-flex align-items-top'>";
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


                <!-- 발언 내용 -->
                row += "<div class='remark-content mt-2'><a href='/remark/" + remark.remarkId + "'>" +
                    "<pre style='margin-bottom: 0'><p>" + remark.content + "</p></pre></a></div>";

                <!-- 좋아요 버튼 -->
                row += "<div class='remark-center-bottom-wrap d-flex justify-content-center'>";
                row += "<div class='like-icon-container' style='margin-left: 0;'><a class='btn''><i class='bi bi-hand-thumbs-up-fill like-icon' style='color: #439ff9;'></i><span class='like-count' style='color: #439ff9;'>101</span></a></div>";
                row += "<div class='like-icon-container'><a class='btn'><i class='bi bi-hand-thumbs-down-fill like-icon'></i><span class='like-count'>5</span></a></div>";
                row += "</div>";


                <!-- 출처공유 & 좋아요 & 발언날짜 컨테이너 -->
                row += "<div class='d-flex justify-content-between align-items-center'>";


                <!-- 출처 & 공유 -->
                row +=
                    "<div class='remark-source d-flex'>";

                <!-- 출처 -->
                if (!remark.sourceSort == "") {
                    row +=
                        "<a href='" + remark.sourceUrl + "'  target='_blank' rel='noopener'>" +
                        "<i class='bi bi-link-45deg'>" +
                        "</i>" + remark.sourceSort + "</a>";
                }
                <!-- 공유 -->
                row +=
                    "<a id='share-btn' class='share-btn' href='' onclick='openShareModal(this," + remark.remarkId + ")' data-name='" + remark.person.name + "' data-date='" + remark.remarkDate_format + "' data-bs-toggle='modal' data-bs-target='#shareModal'>" +
                    "<i class='bi bi-box-arrow-up-right'></i> 공유</a>" +
                    "</div>";


                row += "<div class='remark-right-bottom-wrap'>";

                <!-- 발언 날짜 -->
                row += "<div class='remark-date d-flex justify-content-end'><span>" + remark.remarkDate_format + "</span></div>" +

                    <!-- 발언인 -->
                    "<div class='remark-person d-flex align-items-center justify-content-end'>" +
                    <!-- 발언인>직업 -->
                    "<span class='person-job'>" + remark.person.job + "</span>" +
                    <!-- 발언인>이름 -->
                    "<a style='margin-left: .3rem;' href='/remark/search?keyword=" + remark.person.name + "&personId=" + remark.person.id + "&tab=2" + "'><span class='person-name'>" + remark.person.name + "</span></a></div>" +
                    "</div></div>";


                <!-- 등록일 -->
                /*row +=
                    "<div class='d-flex justify-content-between pt-2' style='border-top: 0.05rem solid rgba(0,0,0,.125);\n" +
                    "    margin-top: 1rem;'>" +
                    "<div class='pt-1'>" +
                    "<div class='col-xs-6'><span><small class='text-muted'>" + remark.createdDate + " 등록됨</small></span></div>";

                <!-- 수정일 -->
                if (remark.status == 'UPDATED') {
                    row +=
                        "<div class='col-xs-6'><span><small class='text-muted'>" + remark.updatedDate + " 수정됨</small></span></div></div>";

                } else {
                    row +=
                        "</div>";
                }*/

                /*row +=
                    "<div class='remark-delete-and-update-btn-wrap d-flex align-items-center'>" +
                    "<a class='btn btn-outline-secondary btn-sm me-2' href='/remark/" + remark.remarkId + "/update'>수정</a>" +
                    "<button class='btn btn-outline-danger btn-sm btn-delete' onclick='main.delete(" + remark.remarkId + ")'>삭제</button>" +
                    "<input type='hidden' name='remarkId' value='" + remark.remarkId + "'></div>";
                    */

                row += "</div></div></div>";


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
        } else if(pageMetadata.first && totalElements != 0){
            nav += "<li class='page-item disabled'><a class='page-link' aria-label='Previous' page='" + (page - 1) + "'><span aria-hidden='true'><i class='bi bi-chevron-left'></i>" +
                "</svg></span></a></li>";
        } else{

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
        }else if(pageMetadata.last && totalElements != 0){
            nav += "<li class='page-item disabled'><a class='page-link' aria-label='Next' page='" + (page + 1) + "'><span aria-hidden='true'><i class='bi bi-chevron-right'></i>" +
                "</svg></span></a></li>";

        }

        nav += "</ul></nav>"


        return nav;

    },

    subPagination: function(pageMetadata){
        let nav = "";
        let page = main.page;

        if (!pageMetadata.first && pageMetadata.totalElements != 0) {
            nav += "<a class='page-link' aria-label='Previous' page='" + (page - 1) + "'><i class='bi bi-chevron-left'></i>" +
                "</a>";
        } else if(pageMetadata.first && pageMetadata.totalElements != 0){
            nav += "<span class='page-link' aria-label='Previous'><i class='bi bi-chevron-left'></i>" +
                "</span>";
        }

        nav += (pageMetadata.number + 1) + " / " + pageMetadata.totalPages + " 페이지";

        if (!pageMetadata.last && pageMetadata.totalElements != 0) {
            nav += "<a class='page-link' aria-label='Next' page='" + (page + 1) + "'><i class='bi bi-chevron-right'></i>" +
                "</a>";
        }else if(pageMetadata.last && pageMetadata.totalElements != 0){
            nav += "<span class='page-link' aria-label='Next'><i class='bi bi-chevron-right'></i>" +
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
    }
}
main.init();