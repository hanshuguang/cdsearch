function sendRequest(url, callback) {
  var xmlhttp = GetXmlHttpObject();
	if (xmlhttp == null){
		alert("Your browser does not support Ajax HTTP");
		return;
	}
	
	xmlhttp.onreadystatechange = function(){
	    callback(xmlhttp.responseText);
	};
	xmlhttp.open("GET", url, true);
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