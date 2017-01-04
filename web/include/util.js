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

function generateFooter(device) {
    if(device === 'D') {
        var qrCode = document.getElementById("crystal-qrcode");
        var iframe = document.createElement('iframe');
        iframe.style = "border:0px";
        iframe.src = "https://api.qrserver.com/v1/create-qr-code/?size=70x70&data="
            + window.location.href;
        qrCode.appendChild(iframe);
    }
}

function displayVisits(content) {
    var array = content.split(";");
    
    // Stores inner HTML
    var htmls = {};
    for(var i = 0; i < array.length - 1; i++) {
        if(array[i] == '') {
            continue;
        }
        var meta = array[i].split(',');
        var id = meta[1];
        htmls[id] = document.getElementById(id).innerHTML;
        document.getElementById(id).innerHTML = '';
    }
    
    for(var i = 0; i < array.length; i++) {
        if(array[i] == '') {
            continue;
        }
        
        var meta = array[i].split(',');
        var newid = meta[0];
        var id = meta[1];
        document.getElementById(newid).innerHTML = htmls[id];
        
        // Re-ranked results. Now, we attach the visit information.
        if(meta.length > 4) {
            var query = meta[3];
            var time = meta[4];
            var device = meta[5];
            // Alter content in HTML
            var ele = document.createElement("div");
            ele.className = 'ranker-meta-serp';
            ele.innerHTML = "<img src='images/indicator-visit.png' width='15px'></img>&nbsp;<b>" + time + "</b> on <img src='images/logo-" + device.toLowerCase() + ".png' width='15px'></img> with query <b>" + query + "</b>";
            document.getElementById(newid).appendChild(ele);
        }
        
    }
}