$(document).ready(function(){
    $.getJSON( "http://demo4166466.mockable.io/", function( data ) {
        $("aside").empty();
        $("aside").append("<a target='_blank' href='" + data.events[0].link + "'>" + "<h2>" + data.events[0].name + "</h2>" + "</a>");
        $("aside").append("<p>" + data.events[0].description + "</p>");
        $("aside").append("<a href='event.html' > <img id='img-center' src='images/chat.png' ></a>");

        $("aside").append("<a target='_blank' href='" + data.events[1].link + "'>" + "<h2>" + data.events[1].name + "</h2>" + "</a>");
        $("aside").append("<p>" + data.events[1].description + "</p>");
        $("aside").append("<a href='event.html' > <img id='img-center' src='images/chat.png' ></a>");

        $("aside").append("<a target='_blank' href='" + data.events[2].link + "'>" + "<h2>" + data.events[2].name + "</h2>" + "</a>");
        $("aside").append("<p>" + data.events[2].description + "</p>");
        $("aside").append("<a href='event.html' > <img id='img-center' src='images/chat.png' ></a>");

        $("aside").append("<a target='_blank' href='" + data.events[3].link + "'>" + "<h2>" + data.events[3].name + "</h2>" + "</a>");
        $("aside").append("<p>" + data.events[3].description + "</p>");
        $("aside").append("<a href='event.html' > <img id='img-center' src='images/chat.png' ></a>");

        $("aside").append("<a target='_blank' href='" + data.events[4].link + "'>" + "<h2>" + data.events[4].name + "</h2>" + "</a>");
        $("aside").append("<p>" + data.events[4].description + "</p>");
        $("aside").append("<a href='event.html' > <img id='img-center' src='images/chat.png' ></a>");
    });

    $("#ham").click(function() {
        alert("yeerr");
    });
});