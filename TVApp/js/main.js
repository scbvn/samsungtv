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
        showHidePopup();
        
    });

    initMqtt();

    AddItem();

    
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

function showPopup() {
  console.log("Show popup")
  if($("#bg-text").is(":hidden")) {
    var $popup = $("#bg-text");
    $popup.slideDown();
  }
}

function hidePopup() {
  if(!$("#bg-text").is(":hidden")) {
    var $popup = $("#bg-text");
    $popup.slideUp();
  } 
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
    
    var avarta = document.querySelector('#avarta');
  	avarta.style.display = 'none';
    
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
      var box = document.querySelector('#textbox');
      box.innerHTML = visitor;
        
      var subMessage = document.querySelector('#submessage');
      subMessage.innerHTML = "Have a good journey";
      
      var avarta = document.querySelector('#avarta');
      avarta.src = imageMapping[visitor.toLowerCase()];
	  avarta.style.display = 'block';
	  
	  var audio = new Audio('sounds/ding-dong.wav');
	  audio.volume = 0.7;
    //audio.play();
    window.setTimeout(showPopup, 500);

    //set timeout to hide panel
    window.setTimeout(hidePopup, 20000);

    }
}

function AddItem() {
  itemCount++;
  $('#slider-items').append("<li>item " + itemCount + "</li>");
}

function RemoveItem() {
  $('#slider-items li').first().remove();
  $('#slider-items').css('marginLeft', 0);
}




