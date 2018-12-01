$(document).ready(function() {
    $.getJSON("js/event.json", function(data) {
        $("aside").empty();
        $("aside").append("<h1>" + data.events[0].name + "</h1>");
        $("aside").append("<a>" + data.events[0].link + "</a>");
        $("aside").append("<p>" + data.events[0].description + "</p>");
        $("aside").append("<h1>" + data.events[1].name + "</h1>");
        $("aside").append("<a>" + data.events[1].link + "</a>");
        $("aside").append("<p>" + data.events[1].description + "</p>");
    });

});