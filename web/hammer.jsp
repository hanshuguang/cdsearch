<script src="include/hammer.js"></script>

<input type="hidden" id="panup" value="" />
<input type="hidden" id="pandown" value="" />
<input type="hidden" id="panleft" value="" />
<input type="hidden" id="panright" value="" />
<input type="hidden" id="press" value="" />

<script language="javascript">

var action = "load";
var time = new Date().getTime();

function logReading() {
  var interval = new Date().getTime() - time;
  updatePageEvent(action + "->unload", interval);
}

window.onload = function() {
  action = "load";
  time = new Date().getTime();
}

window.onfocus = function() {
  action = "focus";
  time = new Date().getTime();
}

window.onmousemove = function(event) {
  if(event.pageY <= 5) {
    var interval = new Date().getTime() - time;	
    updatePageEvent(action + "->unload", interval);
	time = new Date().getTime();
  }
}

window.onblur = function() {
  var interval = new Date().getTime() - time;
  updatePageEvent(action + "->blur", interval);
}

window.onbeforeunload = function() {
  var interval = new Date().getTime() - time;
  updatePageEvent(action + "->unload", interval);
}

var pageType = "<%=pageType%>";
var container = document.getElementById('crystalcontainer');
if(container != null) {
    var hammer = new Hammer(container);
	hammer.get('pan').set({direction: Hammer.DIRECTION_ALL});

	// listen to events...
	hammer.on("panup pandown panleft panright panend press pressup", function(event) {
		
		var type = event.type;
		
		var deltaX = event.deltaX;
		var deltaY = event.deltaY;
		var deltaTime = event.deltaTime;
		var distance = event.distance;
		
		var direction = event.direction;
		var angle = event.angle;
		
		var velocityX = event.velocityX;
		var velocityY = event.velocityY;
		var velocity = event.velocity;
		
		var touches = event.scale;
		
		var centerX = event.center.x;
		var centerY = event.center.y;
		
		var events = type + " " + touches + " " + centerX + " " + centerY + " "
		  + deltaX + " " + deltaY + " " + deltaTime + " " + distance + " "
		  + velocityX + " " + velocityY + " " + velocity + " "
		  + angle + " " + direction + "::";
		
		if(type == 'panup') {
		  document.getElementById('panup').value += events;
		} else if(type == 'pandown') {
		  document.getElementById('pandown').value += events;
		} else if(type == 'panleft') {
		  document.getElementById('panleft').value += events;
		} else if(type == 'panright') {
		  document.getElementById('panright').value += events;
		}else if(type == 'press') {
		  document.getElementById('press').value += events;
		} else if(type == 'panend' || type == 'pressup') {
		  var html = event.target.innerHTML;
		  
		if(event.target.tagName.toLowerCase() == 'strong'
		  || event.target.tagName.toLowerCase() == 'span') {
			html = event.target.parentElement.innerHTML;
		}
		  
		var panupdata = document.getElementById('panup').value;
		var pandowndata = document.getElementById('pandown').value;
		var pressdata = document.getElementById('press').value;
		var panleftdata = document.getElementById('panleft').value;
		var panrightdata = document.getElementById('panright').value;
		var data = "<pl>" + panleftdata + "</pl> " + "<pr>" + panrightdata + "</pr>"
			+ "<pu>" + panupdata + "</pu> " + "<pd>" + pandowndata + "</pd>"
			+ "<ps>" + pressdata + "</ps> ";
		  updatelogs(data, html);
		}	
	});
}
		  
function updatelogs(event, touchHtml){
	var xmlhttp = GetXmlHttpObject();
	if (xmlhttp == null){
		alert("Your browser does not support Ajax HTTP");
		return;
	}
	
	var urlSend = "updatelogs.jsp?logType=mtievent&username=<%=username%>&query=<%=URLEncoder.encode(query, "utf-8")%>&pageType="
	  + pageType + "&event=" + event + "&pageUrl=<%=URLEncoder.encode(pageUrl, "utf-8")%>" + "&touchHtml=" + encodeURIComponent(touchHtml);
			
	xmlhttp.onreadystatechange = function(){
	  if (this.readyState == 4 && this.status == 200) {
		document.getElementById('panup').value = "";
		document.getElementById('pandown').value = "";
		document.getElementById('press').value = "";
		document.getElementById('panleft').value = "";
		document.getElementById('panright').value = "";
	  }
	};
	xmlhttp.open("GET", urlSend, true);
	xmlhttp.setRequestHeader('If-Modified-Since', 'Sat, 1 Jan 2000 00:00:00 GMT');
	xmlhttp.send();
}

function updatePageEvent(eventType, interval) {
	var xmlhttp = GetXmlHttpObject();
	if (xmlhttp == null){
		alert("Your browser does not support Ajax HTTP");
		return;
	}
	
	var urlSend = "updatelogs.jsp?logType=pageevent&username=<%=username%>&query=<%=URLEncoder.encode(query, "utf-8")%>&pageType="
	  + pageType + "&pageUrl=<%=URLEncoder.encode(pageUrl, "utf-8")%>" + "&eventType=" + eventType + "&interval=" + interval;
			
	xmlhttp.onreadystatechange = function(){};
	xmlhttp.open("GET", urlSend, true);
	xmlhttp.setRequestHeader('If-Modified-Since', 'Sat, 1 Jan 2000 00:00:00 GMT');
	xmlhttp.send();
}

function GetXmlHttpObject() {
	if (window.XMLHttpRequest) {
	   return new XMLHttpRequest();
	}
	if (window.ActiveXObject) {
	  return new ActiveXObject("Microsoft.XMLHTTP");
	}
	return null;
}
</script>