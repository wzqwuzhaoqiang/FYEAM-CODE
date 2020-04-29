function XXX() {
	displayMessage();
}
 
function displayMessage() {
	if (navigator.userAgent.indexOf("Firefox") == -1) {
		var obj = document.getElementsByTagName('SELECT');
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].type.indexOf("select") != -1)
				obj[i].style.visibility = 'hidden';
		}
		mask.style.visibility = 'visible';
		massage_box.style.visibility = 'visible';
	}
}
function hiddenMessage() {
	mask.style.visibility = 'hidden';
	massage_box.style.visibility = 'hidden';
}
var Obj = '';
document.onmouseup = MUp;
document.onmousemove = MMove;
function MDown(Object) {
	Obj = Object.id;
	document.all(Obj).setCapture();
	pX = event.x - document.all(Obj).style.pixelLeft;
	pY = event.y - document.all(Obj).style.pixelTop;
}
 
function MMove() {
	if (Obj != '') {
		document.all(Obj).style.left = event.x - pX;
		document.all(Obj).style.top = event.y - pY;
	}
}
 
function MUp() {
	if (Obj != '') {
		document.all(Obj).releaseCapture();
		Obj = '';
	}
}

