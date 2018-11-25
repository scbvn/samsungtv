var client;
var timeToHidePopup;

var imageMapping = {
		"mr.duy": "images/members/duy/Duy.jpg",
		"mrs.huong": "images/members/huong/Huong.jpg",
		"mr.markus": "images/members/markus/Markus.jpg",
		"mr.my": "images/members/my/My.jpg",
		"mr.sang": "images/members/sang/Sang.jpg",
		"mr.son": "images/members/son/Son.jpg",
		"mr.sum": "images/members/sum/Sum.jpg",
		"mr.trong": "images/members/trong/Trong.jpg",
		"mr.tuan": "images/members/tuan/tuan.jpg",
		"mr.binh": "images/members/binh/binh.jpg",
		"mr.sonpham": "images/members/sonpham/SonPham.jpg",
		"mr.son": "images/members/son/Son.jpg",
		"stranger": "images/members/unknow/firework.png"
}

var itemCount = 0;
var pixelsToScroll = "50px";

window.onload = function () {
    // TODO:: Do your initialization job

    // add eventListener for tizenhwkey
    document.addEventListener('tizenhwkey', function(e) {
        if(e.keyName === "back") {
		try {
		    	tizen.application.getCurrentApplication().exit();
			} catch (ignore) {
				
			}
        }
      });
      
      $( document.body ).click(function() {
        //showPopup();
        onMessageArrived("{payloadString:'Mr.Sum'}");
    });

    initMqtt();
};

function showHidePopup() {
  console.log("Show popup")
  if($("#bg-text").is(":hidden")) {
    var $popup = $("#bg-text");
    $popup.slideDown();
  } else {
      var $popup = $("#bg-text");
      $popup.slideUp();
  }
}

function showPopup(action) {
  console.log("Show popup")
  if($("#bg-text").is(":hidden")) {
    var $popup = $("#bg-text");
    $popup.slideDown("slow", action);
  }
}

function hidePopup() {
  removeAllPersons();
  if(!$("#bg-text").is(":hidden")) {
    var $popup = $("#bg-text");
    $popup.slideUp();
  } 
  loadIframe("iframe", "http://192.168.1.3:51200/#/dashboard");
}

function guid() {
	  function s4() {
	    return Math.floor((1 + Math.random()) * 0x10000)
	      .toString(16)
	      .substring(1);
	  }
	  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
  }
  
function initMqtt() {
  //mqtt
    // Create a client instance
    client = new Paho.MQTT.Client("192.168.1.31", 15675, "/ws", "clientId-" + guid());

    console.log("client:" + client);
    
    // set callback handlers
    client.onConnectionLost = onConnectionLost;
    client.onMessageArrived = onMessageArrived;

    // connect the client
    client.connect({onSuccess:onConnect});
    
  
}

function addNewPerson(name, image) {
  var template = '<div class="img-with-text"> <h1 style="font-size: 30px; width: 100%; margin: 10px;" align="center" id="textbox">' + name + '</h1> <div class="container"><img style="display: "block"; width: "200px"; height: "200px"" id="avarta1" class="avarta_img" alt="Avatar" src="' + image + '"></div></div>';
  var container = $('#list-container');
  container.append(template);
  console.log("add new person");
  
}

function removeAllPersons() {
  var container = $('#list-container');
  container.empty();
}

function AddItem() {
  itemCount++;
  $('#slider-items').append("<li>item " + itemCount + "</li>");
}

function RemoveItem() {
  $('#slider-items li').first().remove();
  $('#slider-items').css('marginLeft', 0);
}

function loadIframe(iframeName, url) {
    var $iframe = $('#' + iframeName);
    if ( $iframe.length ) {
        $iframe.attr('src',url);   
        return false;
    }
    return true;
	
//	document.getElementsByName(iframeName)[0].src = site;
}

//Mqtt
//called when the client connects
function onConnect() {
  // Once a connection has been made, make a subscription and send a message.
  console.log("onConnect");
  var textboxTemp = document.querySelector('#textbox');
  client.subscribe("test/detection");
}

// called when the client loses its connection
function onConnectionLost(responseObject) {
  if (responseObject.errorCode !== 0) {
    console.log("onConnectionLost:"+responseObject.errorMessage);
    var box = document.querySelector('#textbox');
    box.innerHTML = "Have a good journey";
  }
}

// called when a message arrives
function onMessageArrived(message) {
  //clear timeout
  window.clearTimeout();

  console.log("onMessageArrived:" + message.payloadString);
  
  
  var visitor = message.payloadString;
    
  var subMessage = document.querySelector('#submessage');
  subMessage.innerHTML = "Have a good journey";
  
var audio = new Audio('sounds/ding-dong.wav');
audio.volume = 0.7;
//audio.play();
showPopup(addNewPerson(visitor, "images/members/sang/Sang.jpg"));

window.setTimeout(hidePopup, 20000);


}





