var client;

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
    
    //mqtt
    // Create a client instance
    client = new Paho.MQTT.Client("192.168.1.31", 15675, "/ws", "clientId-12345");    
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
      client.subscribe("/topic/test");
//      message = new Paho.MQTT.Message("Hello");
//      message.destinationName = "World";
//      client.send(message);
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
      console.log("onMessageArrived:" + message.payloadString);
      
      showPopup();
      var visitor = message.payloadString;
      var box = document.querySelector('#textbox');
      box.innerHTML = visitor;
      
      
      var subMessage = document.querySelector('#submessage');
      subMessage.innerHTML = "Have a good journey";
      
      var avarta = document.querySelector('#avarta');
      if(message.payloadString.includes("Sum")) {
    	  avarta.src = "images/kisimita.jpg";
    	  avarta.style.display = 'block';
      } else {
    	  avarta.style.display = 'none';
      }
    }
    
};

function showPopup() {
  $("#popup").show().animate({top: (window.innerHeight / 2 - 50) + "px"}, 1000);
}


