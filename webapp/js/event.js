$(document).ready(function() {
    $.getJSON("http://demo4166466.mockable.io/", function(data) {
        $("#title").append("<a target='_blank' href='" + data.events[0].link + "'>" + "<h1>" + data.events[0].name + "</h1>" + "</a>");
        $("#stuff").append("<h3>" + "Description: " + data.events[0].description + "</h3>");
    });
});