var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/stepmessage', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        
        //websocket connected, then start shell
        $.ajax({
    	  url: "/start",
    	  cache: false,
    	  success: function(html){
    		  showGreeting("开始...");
    	  }
    	});
    });
}

function disconnect() {
	$.ajax({
  	  url: "/stop",
  	  cache: false,
  	  success: function(html){
  		  alert(html);
  	  }
  	});
	
	//disconnect websocket
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function testADB() {
	$.ajax({
  	  url: "/test",
  	  cache: false,
  	  success: function(html){
  		  alert(html);
  	  }
  	});
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
    $('#stepsDiv').animate({scrollTop: $('#stepsDiv').prop('scrollHeight')});
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#testADB" ).click(function() { testADB(); });
});