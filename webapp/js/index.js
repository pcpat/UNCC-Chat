$(document).ready(function() {
    $("#loginform").hide();

    $("#left").click(function () {
        $("#signupform").hide();
        $("#loginform").show();
    });

    $("#right").click(function () {
        $("#loginform").hide();
        $("#signupform").show();
    });

    $("#submit2").click(function () {
        $(location).attr('href', 'dash.html')
    });
});