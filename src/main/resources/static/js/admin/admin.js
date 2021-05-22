/*
var main = {
    init: function () {
        var _this = this;
        $('#btn-search').on('click', function () {
            _this.search()
        });
    },

    search: function () {

        let keyword = $('#keyword').val()


        //검색
        $('#btn-search').on('click', function () {
            console.log(keyword)

            $.ajax({
                type: 'GET',
                url: '/admin/comment/search/keyword='+keyword,
                contentType: 'application/json; charset=utf-8',
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });

        });

    }

};

main.init();



*/
