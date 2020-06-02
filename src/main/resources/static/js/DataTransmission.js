window.onload = init();
var name;
var x = 10;var k=1;
var lordListId;

var robot0=17;var robot2=17;//初始时人机的牌数
var user=17;//初始时用户的牌数

function init() {//界面初始化，将后端用户牌组显示出来
	var str;
	name=Transmission();
	var url = '/initGames';
	$.getJSON(url).done(function(json) {
			playerListId = json.playerListId;
			lordListId = json.lordListId;
			
			for(var p in playerListId) { 
				var parent = document.getElementById("bottom");
				var img = document.createElement("img");
				img.setAttribute("id", playerListId[p].name);
				img.src = "img/" + playerListId[p].name + ".gif";
				img.style.marginLeft = "-50px";
				img.style.display = "inline-block";
				img.onclick = up;
				parent.appendChild(img);
				
				var parent1 = document.getElementById("right");
				var parent2 = document.getElementById("left");
				var imga = document.createElement("img");
				imga.setAttribute("id", "r"+k);
				var imgb = document.createElement("img");
				imgb.setAttribute("id", "l"+k);
				imga.src = "img/rear.gif";
				imgb.src = "img/rear.gif";
				imga.classList.add("robotPocker");
				imgb.classList.add("robotPocker");
				imga.style.top = x + "%";
				imgb.style.top = x + "%";
				parent1.appendChild(imga);
				parent2.appendChild(imgb);
				x += 3;
				k++;
			}
			for(var e in lordListId) {
				var parent = document.getElementById("top");
				var img = document.createElement("img");
				img.src = "img/rear.gif";
				img.classList.add("desktopPocker");
				parent.appendChild(img);
			}

	})
}

function pushpocker(i) {//将出牌和不出情况通告后台
	if(i==0){
		var json = [];
		 $.ajax({
				type: 'POST',
				url: '/cardJudge',
				traditional : true ,
				contentType:'application/x-www-form-urlencoded',
				data: {"list": json},
				success: function(result) {
					if(result == 0){
						alert("本轮必须出牌");
						flag=result;
						return;
					}
					var div = document.getElementById("center-buttom");
					 div.innerHTML = "不出";
					clearInterval(timer);
					gameButtonsInvisible();
					var div = document.getElementById("desktop-center");
					div.style.display = "none";
					robotPlaying();
				},
			});
	}
	else if(i==1){
		var arr = [];
		var json = [];
		arr = document.getElementsByClassName('select', 'bottom');
		if(arr.length==0){
			alert("不能为空");
			return;
		}
		for(var j = 0; j < arr.length; j++) {
			json[j] = arr[j].id;
		}
		$.ajax({
			type: 'POST',
			url: '/cardJudge',
			traditional : true ,
			contentType:'application/x-www-form-urlencoded',
			data: {"list" : json},
			success: function(result) {
				if(result==1){
					var div = document.getElementById("center-buttom");
					div.innerHTML = "";
					var parent = document.getElementById("center-buttom");
					for(var i=0;i<json.length;i++){
						var obj = document.getElementById(json[i]);
						obj.classList.remove("select");
						obj.style.display = 'none';
						var img = document.createElement("img");
						img.setAttribute("id", json[i]);
						img.src = "img/" + json[i] + ".gif";
						img.style.marginLeft = "-50px"
						img.style.display = "inline-block";
						img.classList.add("desktopPocker");
						parent.appendChild(img);
						
						user--;
					}
					if(user==0) show(1);
						
					clearInterval(timer);
					var div = document.getElementById("desktop-center");
					div.style.display = "none";
					gameButtonsInvisible();
					robotPlaying();
				}
				
				console.log(result);
			},
			beforeSend: function() {
				console.log("请求发送之前")
			},
			error: function() {
				console.log("失败时调用")
			}
		});
	}
	
}


function robotPlaying(){//人机出牌
	var count = 4;
	time = setInterval(function() {
		if(count > 0) {
			count = count - 1;
		} 
		if(count==2) {
			$.getJSON('/robotPlaying', { "Judgement": 2 }, function(json){
				 a2=json;
				 var div = document.getElementById("center-right");
				 div.innerHTML = "";
				 if(a2.length == 0){//不出牌
					 div.innerHTML = "不出";
				 }else{//出牌
					 for(var e in a2) {
							var parent = document.getElementById("center-right");
							var img = document.createElement("img");
							img.setAttribute("id", a2[e].name);
							img.src = "img/" + a2[e].name + ".gif";
							img.style.marginLeft = "-50px"
							img.style.display = "inline-block";
							img.classList.add("desktopPocker");
							parent.appendChild(img);
							
							document.getElementById("r"+robot2).style.display = "none";
							robot2 --;
						}
					 if(robot2==0) show(2);
				 }
				 
			});
		}else if(count==1){
			$.getJSON('/robotPlaying', { Judgement: 0 }, function(json){
				 a0=json;
				 var div = document.getElementById("center-left");
				 div.innerHTML = "";
				 if(a0.length == 0){
					 div.innerHTML = "不出";
				 }else{
					 for(var e in a0) {
							var parent = document.getElementById("center-left");
							var img = document.createElement("img");
							img.setAttribute("id", a0[e].name);
							img.src = "img/" +a0[e].name + ".gif";
							img.style.marginLeft = "-50px";
							img.style.display = "inline-block";
							img.classList.add("desktopPocker");
							parent.appendChild(img);
							document.getElementById("l"+robot0).style.display = "none";
							robot0--;
						}
					 if(robot0==0) show(0);
				 }
				 
			});
		}else if(count==0){
			var div = document.getElementById("desktop-center");
			div.style.display = "block";
			gameButtonsVisualization();
			clearInterval(time);
			countDown();
		}
		
	}, 1000);
}

function robLandlord(i){//抢地主的判断并显示地主牌
	$.getJSON('/landorJudge', { Judgement: i }, function(json){
		 if(json.result==0){//人机0抢到地主
			 addRobotCard(0);
			 robot0=robot0+3;
			 var div = document.getElementById("center-right");
			 div.innerHTML = "不抢";
			 a0=json.a0;
			 var Landlord0 = document.getElementById("Landlord0");
			 var img = document.createElement("img");
			 img.src = "img/dizhu.gif";
			 Landlord0.appendChild(img);
			 
			 for(var e in a0) {//显示人机0出的牌
					var parent = document.getElementById("center-left");
					var img = document.createElement("img");
					img.setAttribute("id", a0[e].name);
					img.src = "img/" +a0[e].name + ".gif";
					img.style.marginLeft = "-50px";
					img.style.display = "inline-block";
					img.classList.add("desktopPocker");
					parent.appendChild(img);
					
					document.getElementById("l"+robot0).style.display = "none";
					robot0=robot0-1;
				}
		 }
		 else if(json.result==2){//人机2抢到地主
			 addRobotCard(2);
			 robot2=robot2+3;
			 a2=json.a2;
			 var Landlord2 = document.getElementById("Landlord2");
			 var img = document.createElement("img");
			 img.src = "img/dizhu.gif";
			 Landlord2.appendChild(img);
			 for(var e in a2) {//显示人机2的出牌
					var parent = document.getElementById("center-right");
					var img = document.createElement("img");
					img.setAttribute("id", a2[e].name);
					img.src = "img/" + a2[e].name + ".gif";
					img.style.marginLeft = "-50px"
					img.style.display = "inline-block";
					img.classList.add("desktopPocker");
					parent.appendChild(img);
					
					document.getElementById("r"+robot2).style.display = "none";
					robot2=robot2-1;
				}
			 a0=json.a0;
			 for(var e in a0) {//显示人机1的出牌
					var parent = document.getElementById("center-left");
					var img = document.createElement("img");
					img.setAttribute("id", a0[e].name);
					img.src = "img/" +a0[e].name + ".gif";
					img.style.marginLeft = "-50px"
					img.style.display = "inline-block";
					img.classList.add("desktopPocker");
					parent.appendChild(img);
					
					document.getElementById("l"+robot0).style.display = "none";
					robot1=robot1-1;
				}
		 }
		 else if(json.result==1){//用户抢地主
			 user=user+3;
			 var Landlord1 = document.getElementById("Landlord1");
			 var img = document.createElement("img");
			 img.src = "img/dizhu.gif";
			 Landlord1.appendChild(img);
			 a1 = json.playerList1;
			 var div = document.getElementById("bottom");
			 div.innerHTML = "";
			 for(var e in a1) {//刷新用户手中的牌
					var parent = document.getElementById("bottom");
					var img = document.createElement("img");
					img.setAttribute("id", a1[e].name);
					img.src = "img/" +a1[e].name + ".gif";
					img.style.marginLeft = "-50px"
					img.style.display = "inline-block";
					img.onclick = up;
					parent.appendChild(img);
				}
		 }
		});
	var objs=document.getElementsByClassName('desktopPocker', 'top');
	for(var j=0;j<objs.length;j++){
		objs[j].style.display = "none";
	}
	for(var e in lordListId) {//显示地主牌
		var parent = document.getElementById("top");
		var img = document.createElement("img");
		img.src = "img/" + lordListId[e].name + ".gif";
		parent.appendChild(img);
	}
	visualization();
	countDown();
}


//游戏结束时调用
function show(i) //显示隐藏层和弹出层
{
	//alert("2:"+name);
	var hideobj = document.getElementById("hidebg");
	hidebg.style.display = "block"; //显示隐藏层
	hidebg.style.height = document.body.clientHeight + "px"; //设置隐藏层的高度为当前页面高度
	var obj = document.getElementById("hidebox");
	obj.style.display = "block"; //显示弹出层
	if(i==1){
		obj.innerHTML = "恭喜你获得胜利</br><a onclick='change()'>点击返回</a>";
	}
	else if(i==2){
		obj.innerHTML = "人机2取胜</br><a onclick='change()'>点击返回</a>";
	}else if(i==0){
		obj.innerHTML = "人机1取胜</br><a onclick='change()'>点击返回</a>";
	}
	alert(name);
	$.getJSON('/gameOver', { Judgement: i , Account : name}, function(json){
		 
	});
	
}
