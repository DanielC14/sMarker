$("document").ready(function () {

    $.contextMenu({
        selector: '.sm-mybookmarks',
        callback: function (key, options) {
            if (key == "open") {
                openBlock($(this));
            }
            if (key == "comment") {
                openComments($(this).attr("id"));
            }
            if (key == "delete") {
                selected_bookmarks.push($(this).attr("id"));
                deleteBookmarks();

            }
            var m = "clicked: " + key;
        },
        items: {
            "open": {
                name: "Open",
                icon: "fa-external-link"
            },
            "comment": {
                name: "Comments",
                icon: "fa-comments"
            },
            "delete": {
                name: "Delete",
                icon: "fa-trash"
            }
        }
    });

    $.contextMenu({
        selector: '.sm-allbookmarks',
        callback: function (key, options) {
            if (key == "open") {
                openBlock($(this));
            }
            if (key == "comment") {
                openComments($(this).attr("id"));
            }
            if (key == "add") {
                bookmark_type = "add";
                $("#sm_form_bookmark_add_url").val($(this).attr("url"));
                $("#sm_form_bookmark_add_name").val($(this).find(".sm-block-title").html());
                $("#sm_form_bookmark_add_url").attr("disabled", true);
                $("#sm_form_bookmark_add_tags").hide();
                $("#sm_form_bookmark_add_category").hide();
                $('#sm_modal_bookmark_add').show();
                bookmark_block = $(this);
                // addBookmarks($(this));

            }
            var m = "clicked: " + key;
        },
        items: {
            "open": {
                name: "Open",
                icon: "fa-external-link"
            },
            "comment": {
                name: "Comments",
                icon: "fa-comments"
            },
            "add": {
                name: "Add to my bookmarks",
                icon: "fa-plus"
            }
        }
    });

    $(document).on('click', '.sm-block', openBlock);

    $(".sm-bookmark-color").click(function () {
        var previous_selected = $("#sm_form_bookmark_add_color").find(".sm-bookmark-color-selected");

        if (previous_selected.length > 0) {
            previous_selected.removeClass("sm-bookmark-color-selected");
            previous_selected.addClass("w3-border");
        }

        $(this).removeClass("w3-border");
        $(this).addClass("sm-bookmark-color-selected");
    });

    $("#sm_account_settings_button").click(function () {
        $("#sm_modal_settings").show();
    });

    $("#sm_select_sort").change(function () {
        sort = $("#sm_select_sort").find(":selected").val();
        if (my_bookmarks == true) {
            loadBookmarks();
        } else {
            loadAllBookmarks();
        }
    });

    $("#sm_open_form_bookmark_add").click(function () {
        bookmark_type = "create";
        $("#sm_form_bookmark_add_url").val("");
        $("#sm_form_bookmark_add_name").val("");
        $("#sm_form_bookmark_add_tags").val("");
        $('#sm_form_bookmark_add_category option:eq(0)').prop('selected', true);
        $("#sm_form_bookmark_add_url").attr("disabled", false);
        $("#sm_form_bookmark_add_tags").show();
        $("#sm_form_bookmark_add_category").show();
        $('#sm_modal_bookmark_add').show();
    });

    $(".sm-top-bar-dropdown-click").click(function () {
        if ($("#sm_dropdown_menu").hasClass("w3-show") == true) {
            $("#sm_dropdown_menu").removeClass("w3-show");
            $(".sm-top-bar-dropdown-click").find("i").addClass("fa-angle-down");
            $(".sm-top-bar-dropdown-click").find("i").removeClass("fa-angle-up");

        } else {
            $("#sm_dropdown_menu").addClass("w3-show");
            $(".sm-top-bar-dropdown-click").find("i").removeClass("fa-angle-down");
            $(".sm-top-bar-dropdown-click").find("i").addClass("fa-angle-up");
        }

    });

    $("#sm_bookmark_delete").click(function () {
        $("#sm_dropdown_menu").removeClass("w3-show");
        $(document).off('click', '.sm-block', openBlock);
        $(document).on('click', '.sm-block', deleteSelect);
        $("#sm_dropdown_delete").show();

    });

    $("#sm_bookmark_delete_cancel").click(function () {
        $(document).off('click', '.sm-block', deleteSelect);
        $(document).on('click', '.sm-block', openBlock);
        $(".sm-block-delete-selected").removeClass("sm-block-delete-selected");
        $("#sm_dropdown_delete").hide();
        selected_bookmarks = [];
    });

    $("#sm_bookmark_delete_submit").click(function () {
        deleteBookmarks();
    });

    $(".sm-logout").click(function () {
        submitLogout();
    });

    $("#sm_bookmark_add_submit").click(function () {
        if (bookmark_type == "create")
            createBookmark();
        else if (bookmark_type == "add")
            addBookmark();
    });

    $("#sm_user_login_submit").click(function () {
        submitLogin();
    });

    $("#sm_user_register_submit").click(function () {
        submitRegister();
    });

    $("#sm_comments_close").click(function () {
        $("#sm_comments").hide();
    });

    $("#sm_button_mybookmarks").click(function () {
        $(".sm-bookmark-type-button").removeClass("w3-black");
        $(this).addClass("w3-black");
        $("#sm_div_search_bookmark").hide();
        $("#sm_dropdown_bookmark_buttons").show();
        my_bookmarks = true;
        loadBookmarks();
    });
    $("#sm_button_allbookmarks").click(function () {
        $(".sm-bookmark-type-button").removeClass("w3-black");
        $(this).addClass("w3-black");
        $("#sm_div_search_bookmark").show();
        $("#sm_dropdown_bookmark_buttons").hide();
        my_bookmarks = false;
        loadAllBookmarks();
    });

    $("#sm_submit_search").click(function () {
        submitSearch();
    });

    $("#sm_comment_add").click(function () {
        submitComment($(this).attr("bookmarkId"));
    });

    $("#sm_settings_submit_name").click(function () {
        var data = {
            new_data: $("#sm_form_settings_name").val()
        }
        updateUser("name", data);
    });

    $("#sm_settings_submit_email").click(function () {
        var data = {
            new_data: $("#sm_form_settings_email").val()
        }
        updateUser("email", data);

    });

    $("#sm_settings_submit_password").click(function () {
        var data = {
            new_data: $("#sm_form_settings_password_new").val(),
            old_data: $("#sm_form_settings_password_old").val()
        }
        updateUser("password", data);
    });

    $(window).resize(function () {
        screenSize();
    });

    screenSize();
    $("#sm_div_search_bookmark").hide();
    $("#sm_bookmark_added").hide();
    $("#sm_comments").hide();

    if (localStorage.getItem("token") != null) {
        loadBookmarks();
    } else {
        $("#sm_button_mybookmarks").attr("disabled", true);
        $(".sm-bookmark-type-button").removeClass("w3-black");
        $("#sm_button_allbookmarks").addClass("w3-black");
        $("#sm_div_search_bookmark").show();
        $("#sm_dropdown_bookmark_buttons").hide();
        my_bookmarks = false;
        loadAllBookmarks();

    }

});

var sort = "abc";

var selected_bookmarks = [];

var my_bookmarks = true;

var bookmark_type = "";

var bookmark_block;


function deleteSelect() {
    console.log($(this))
    selected_bookmarks.push($(this).attr("id"));
    $(this).addClass("sm-block-delete-selected");
}

function openBlock() {
    var url = $(this).attr("url");
    if (url.indexOf("http://") == -1 && url.indexOf("https://") == -1) {
        window.open("http://" + $(this).attr("url"));
    } else {
        window.open($(this).attr("url"));
    }

    updateCounter($(this).attr("id"));
}

function addBookmark() {

    var selected_color = $("#sm_form_bookmark_add_color").find(".sm-bookmark-color-selected");

    selected_color.removeClass("sm-bookmark-color-selected");
    var color = selected_color.attr("class").split(" ").pop();

    var bookmark = {
        name: $("#sm_form_bookmark_add_name").val(),
        color: color
    }

    $.ajax({
        type: 'POST',
        data: JSON.stringify(bookmark),
        contentType: "application/json",
        url: 'http://localhost:8080/sMarker/users/' + localStorage.getItem("token") + "/bookmarks/" + bookmark_block.attr("id"),
        success: function (data) {
            $("#sm_modal_bookmark_add").hide();
            $("#sm_bookmark_added").fadeIn("fast", function () {

                $("#sm_bookmark_added").fadeOut("slow");

            })
            // Bookmarks();
        }

    });

}

function createBookmark() {

    $(".sm-error").html("");

    var name = $("#sm_form_bookmark_add_name").val();
    var url = $("#sm_form_bookmark_add_url").val();
    var category = $("#sm_form_bookmark_add_category").find(":selected").val();

    var tags_temp = $("#sm_form_bookmark_add_tags_input").val();
    var tags = [];
    console.log(tags_temp)
    for (var i = 0; i < tags_temp.split(" ").length; i++) {
        tags.push(tags_temp.split(" ")[i]);
    }

    var selected_color = $("#sm_form_bookmark_add_color").find(".sm-bookmark-color-selected");

    selected_color.removeClass("sm-bookmark-color-selected");
    var color;
    try{
        color = selected_color.attr("class").split(" ").pop();
    }
    catch(err){

    }

    var bookmark = {
        "name": name,
        "url": url,
        "category": category,
        "tags": tags,
        "color": color
    }
    console.log(name, url, category, tags, color);

    if(name == "" || url == "" || category == "" || tags_temp == "" || color == undefined)
    {
        $(".sm-error").html("Fill all the inputs before adding!");
    }
    else
    {
        $.ajax({
            type: 'POST',
            data: JSON.stringify(bookmark),
            contentType: "application/json",
            url: 'http://localhost:8080/sMarker/users/' + localStorage.getItem("token") + "/bookmarks",
            success: function (data) {
                loadBookmarks();
                $("#sm_modal_bookmark_add").hide();
            }
    
        });
    }

    


}

function loadBookmarks() {

    var token = localStorage.getItem("token");

    $.ajax({
        type: 'GET',
        data: "",
        contentType: "application/x-www-form-urlencoded",
        url: 'http://localhost:8080/sMarker/users/  ' + token + '/bookmarks?sort=' + sort,
        success: function (data) {

            if (data.length == 0) {
                $("#sm_bookmark_display").html('To add bookmarks check the tab \"All Bookmarks\" or add them in the \"Options\" menu!');
            } else {
                var text = "";

                for (var i = 0; i < data.length; i++) {
                    text += '<div id="' + data[i].id + '" url="' + data[i].url + '" class="w3-col ' + data[i].color + ' sm-block sm-mybookmarks w3-border">' +
                        '<img src="' + data[i].icon_url + '" class="sm-block-img" alt="">' +
                        '<div class="w3-clear"></div>' +
                        '<p class="sm-block-title">' + data[i].name + '</p>' +
                        '</div>';
                }
                $("#sm_bookmark_display").html(text);
            }


        }
    });

}

function loadAllBookmarks() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/sMarker/bookmarks?sort=' + sort,
        success: function (data) {

            var sm_class = "sm-allbookmarks";

            if (localStorage.getItem("token") == null) {
                sm_class = "";
            }
            var text = "";

            for (var i = 0; i < data.length; i++) {
                text += '<div id="' + data[i].id + '" url="' + data[i].url + '" class="w3-col ' + data[i].color + ' sm-block ' + sm_class + ' w3-border">' +
                    '<img src="' + data[i].icon_url + '" class="sm-block-img" alt="">' +
                    '<div class="w3-clear"></div>' +
                    '<p class="sm-block-title">' + data[i].name + '</p>' +
                    '</div>';
            }
            $("#sm_bookmark_display").html(text);

        }
    });
}

function submitRegister() {
    var email = $("#sm_form_register_email").val();
    var name = $("#sm_form_register_name").val();
    var password = $("#sm_form_register_password").val();

    var registerData = {
        email: email,
        name: name,
        password: password
    }

    $.ajax({
        type: 'POST',
        data: registerData,
        contentType: "application/x-www-form-urlencoded",
        url: 'http://localhost:8080/sMarker/users',
        success: function (data) {
            $(".sm-error").html("");
            if (data.code == "1001") {

                $(".sm-error").html(data.message);
            } else {
                localStorage.setItem("token", data);
                location.reload();
            }

        }
    });

}

function submitLogin() {
    var email = $("#sm_form_login_email").val();
    var password = $("#sm_form_login_password").val();

    var loginData = {
        email: email,
        password: password
    }

    $.ajax({
        type: 'POST',
        data: loginData,
        contentType: "application/x-www-form-urlencoded",
        url: 'http://localhost:8080/sMarker/users/login',
        success: function (data) {
            $(".sm-error").html("");
            if (data.code == "1002") {

                $(".sm-error").html(data.message);
            } else {
                localStorage.setItem("token", data);
                $("#sm_modal_login").hide();
                $(".sm-bookmark-type-button").removeClass("w3-black");
                $("#sm_button_mybookmarks").addClass("w3-black");
                $("#sm_button_mybookmarks").attr("disabled", false);
                $("#sm_div_search_bookmark").hide();
                $("#sm_dropdown_bookmark_buttons").show();
                my_bookmarks = true;
                loadBookmarks();
                screenSize();
            }

        }
    });
}

function submitLogout() {
    localStorage.removeItem("token");
    location.reload();
}



function screenSize() {
    if ($(window).width() < 426) {
        if (localStorage.getItem("token") == null) {
            $("#sm_account_buttons").hide();
            $("#sm_logged_buttons").hide();
            $("#sm_logged_dropdown").hide();
            $("#sm_account_dropdown").show();
        } else {
            $("#sm_account_buttons").hide();
            $("#sm_logged_buttons").hide();
            $("#sm_account_dropdown").hide();
            $("#sm_logged_dropdown").show();
        }

    } else {
        if (localStorage.getItem("token") == null) {
            $("#sm_account_buttons").show();
            $("#sm_logged_buttons").hide();
            $("#sm_logged_dropdown").hide();
            $("#sm_account_dropdown").hide();
        } else {
            $("#sm_account_buttons").hide();
            $("#sm_logged_buttons").show();
            $("#sm_account_dropdown").hide();
            $("#sm_logged_dropdown").hide();
        }
    }
}

function updateCounter(bookmarkId) {

    var token = localStorage.getItem("token");

    $.ajax({
        type: 'PUT',
        data: "",
        contentType: "application/json",
        url: 'http://localhost:8080/sMarker/users/  ' + token + '/bookmarks/' + bookmarkId,
        success: function (data) {
            ("updated")
            if (my_bookmarks == true)
                loadBookmarks();
            else
                AllBookmarks();

        }
    });

}

function deleteBookmarks() {

    var token = localStorage.getItem("token");

    $.ajax({
        type: 'DELETE',
        data: JSON.stringify({
            bookmarks: selected_bookmarks
        }),
        contentType: "application/json",
        url: 'http://localhost:8080/sMarker/users/  ' + token + '/bookmarks',
        success: function (data) {
            $(document).off('click', '.sm-block', deleteSelect);
            $(document).on('click', '.sm-block', openBlock);
            $(".sm-block-delete-selected").removeClass("sm-block-delete-selected");
            $("#sm_dropdown_delete").hide();
            selected_bookmarks = [];
            loadBookmarks();

        }
    });
}


function openComments(bookmarkId) {
    $("#sm_comment_add").attr("bookmarkId", bookmarkId);
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/sMarker/bookmarks/' + bookmarkId + '/comments',
        success: function (data) {

            var text = "";
            for (var i = 0; i < data.length; i++) {
                text += '<div class="w3-border sm-comment">' +
                    '<span><b>' + data[i].username + '</b></span><p>' + data[i].message + '</p></div>';
            }
            $("#sm_comments_display").html(text);
            $("#sm_comments").show();
        }
    });

}


function submitSearch() {
    var url = "http://localhost:8080/sMarker/bookmarks/filter?";

    var name = $("#sm_bookmark_search_name").val();
    if (name != "") {
        url += "name=" + name + "&";
    }
    var category = $("#sm_bookmark_search_category").find(":selected").val();
    if (category != "") {
        url += "category=" + category + "&";
    }

    var temp_tag = $("#sm_bookmark_search_tags").val();

    var tags = temp_tag.replace(" ", ",");
    if (tags != "") {
        url += "tags=" + tags + "&";
    }
    console.log(url)
    $.ajax({
        type: 'GET',
        url: url,
        success: function (data) {
            var text = "";

            for (var i = 0; i < data.length; i++) {
                text += '<div id="' + data[i].id + '" url="' + data[i].url + '" class="w3-col ' + data[i].color + ' sm-block sm-allbookmarks w3-border">' +
                    '<img src="' + data[i].icon_url + '" class="sm-block-img" alt="">' +
                    '<div class="w3-clear"></div>' +
                    '<p class="sm-block-title">' + data[i].name + '</p>' +
                    '</div>';
            }
            $("#sm_bookmark_display").html(text);
        }
    });

}

function submitComment(bookmarkId) {

    var comment = {
        token: localStorage.getItem("token"),
        bookmarkId: bookmarkId,
        message: $("#sm_comment").val()
    }

    $.ajax({
        type: 'POST',
        data: JSON.stringify(comment),
        contentType: "application/json",
        url: "http://localhost:8080/sMarker/comments",
        success: function (data) {
            $("#sm_comment").val("");
            openComments(bookmarkId);
        }
    });

}

function updateUser(section, data) {

    $.ajax({
        type: 'PUT',
        data: JSON.stringify(data),
        contentType: "application/json",
        url: "http://localhost:8080/sMarker/users/" + localStorage.getItem("token") + "?section=" + section,
        success: function (data) {
            $(".sm-error").html("");
            $(".sm-success").html("");
            if (data.code == "1106" || data.code == "1001") {
                $(".sm-error").html(data.message);
            } else {
                $("#sm_form_settings_password_new").val("");
                $("#sm_form_settings_password_old").val("");
                $("#sm_form_settings_name").val("");
                $("#sm_form_settings_email").val("");
                $(".sm-success").html(data.message);
                console.log("done")
            }

        }
    });

}