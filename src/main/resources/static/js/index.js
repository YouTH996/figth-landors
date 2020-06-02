
function up() {
	if(this.className == "select") {
		this.classList.remove("select");
	} else {
		this.classList.add("select");
	}
	//console.log(this.id);
}

function gameButtonsVisualization(){
	var obj = document.getElementsByName("game");
	for(var i=0;i<obj.length;i++){
        obj[i].style.display = 'inline-block';
    }
}
function gameButtonsInvisible(){
	var obj = document.getElementsByName("game");
	for(var i=0;i<obj.length;i++){
        obj[i].style.display = 'none';
    }
}
function visualization(){
	var obj1 = document.getElementById("yes");
	var obj2 = document.getElementById("no");
	obj1.style.display = 'none';
	obj2.style.display = 'none';
	gameButtonsVisualization();
}

function countDown(){
	var odiv = document.getElementById("desktop-center");
	var count = 30;
	odiv.innerHTML = count;
	
	timer = setInterval(function() {
		if(count > 0) {
			count = count - 1;
			odiv.innerHTML = count;
		} else {
			clearInterval(timer);
			pushpocker('0');
		}
	}, 1000);
}
function addRobotCard(i){
	var x=61;
	var k=18;
	if(i==2){
		for(var p=1;p<=3;p++){
			var parent1 = document.getElementById("right");
			var imga = document.createElement("img");
			imga.setAttribute("id", "r"+k);
			imga.src = "img/rear.gif";
			imga.classList.add("robotPocker");
			imga.style.top = x + "%";
			parent1.appendChild(imga);
			x += 3;k++;	
		}
	}else if(i==0){
		for(var p=1;p<=3;p++){
			var parent2 = document.getElementById("left");
			var imgb = document.createElement("img");
			imgb.setAttribute("id", "l"+k);
			imgb.src = "img/rear.gif";
			imgb.classList.add("robotPocker");
			imgb.style.top = x + "%";
			parent2.appendChild(imgb);
			x += 3;k++;
		}
	}
	
}





function hide() //去除隐藏层和弹出层
{
	document.getElementById("hidebg").style.display = "none";
	document.getElementById("hidebox").style.display = "none";
}