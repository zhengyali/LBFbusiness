<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/js/jquery-1.6.4.js"></script>
<script type="text/javascript" >
	//去判断用户是否登录
	$.ajax({
		url: "http://localhost:8090/isLogin.aspx",
		type:"post",
		dataType : "jsonp",
		success : function(data){
			//判断   0  1
			//alert(data);
			if(data){
				$("#login").hide();
				$("#regist").hide();
			}else{
				$("#logout").hide();
				$("#myOrder").hide();
			}
		}
	});
	//去登陆页面
	function login(){
		/*原页面刷新 ，用 href */
		//window.location.href   当前全路径  windows.location.XXX --可以获取到域名、参数等
		alert(window.location.href);
		window.location.href="http://localhost:8090/login.aspx?returnUrl="+encodeURIComponent(window.location.href);
	}

</script>
<div id="shortcut-2013">
	<div class="w">
		<ul class="fl lh">
			<li class="fore1 ld" clstag="homepage|keycount|home2013|01a"><b></b>
				<a href="javascript:addToFavorite()" >收藏LB</a>
			</li>
		</ul>
		<ul class="fr lh">
			<li class="fore1" id="loginbar" clstag="homepage|keycount|home2013|01b">
				您好！欢迎来到LB商城！
				<a href="javascript:;" onclick="login()" id="login">[登录]</a>&nbsp;
				<a href="javascript:;" onclick="regist()" id="regist">[免费注册]</a>
				<a href="javascript:;" onclick="logout()" id="logout">[退出]</a>
				<a href="javascript:;" onclick="myOrder()" id="myorder">我的订单</a>
			</li>
			<li class="fore2-1 ld" id="jd-vip">
				<s></s>
				<a target="_blank" rel="nofollow" href="http://vip.jd.com">会员俱乐部</a>
			</li>
			<li class="fore3 ld menu" id="app-jd" data-widget="dropdown" clstag="homepage|keycount|home2013|01d">
				<s></s><i></i><span class="outline"></span><span class="blank"></span>
				<a href="http://app.jd.com/" target="_blank">手机LB</a><b></b>
			</li>
			<li class="fore4 ld menu" id="biz-service" data-widget="dropdown" clstag="homepage|keycount|home2013|01e">
				<s></s>
				<span class="outline"></span>
				<span class="blank"></span>
				客户服务
				<b></b>
				<div class="dd">
					<div><a href="http://help.jd.com/index.html" target="_blank">帮助中心</a></div>
					<div><a href="http://myjd.jd.com/repair/orderlist.action" target="_blank" rel="nofollow">售后服务</a></div>
					<div><a href="http://chat.jd.com/jdchat/custom.action" target="_blank" rel="nofollow">在线客服</a></div>
					<div><a href="http://myjd.jd.com/opinion/list.action" target="_blank" rel="nofollow">投诉中心</a></div>
					<div><a href="http://www.jd.com/contact/service.html" target="_blank">客服邮箱</a></div>
				</div>
			</li>
			<li class="fore5 ld menu" id="site-nav" data-widget="dropdown" clstag="homepage|keycount|home2013|01f">
				<s></s>
				<span class="outline"></span>
				<span class="blank"></span>
				网站导航
				<b></b>
				<div class="dd lh">
					<dl class="item fore1">
						<dt>特色栏目</dt>
						<dd>
							<div><a target="_blank" href="http://mobile.jd.com/index.do">LB通信</a></div>
							<div><a target="_blank" href="http://jdstar.jd.com/">校园之星</a></div>
							<div><a target="_blank" href="http://my.jd.com/personal/guess.html">为我推荐</a></div>
							<div><a target="_blank" href="http://shipingou.jd.com/">视频购物</a></div>
							<div><a target="_blank" href="http://club.jd.com/">LB社区</a></div>
							<div><a target="_blank" href="http://read.jd.com/">在线读书</a></div>
							<div><a target="_blank" href="http://diy.jd.com/">装机大师</a></div>
							<div><a target="_blank" href="http://giftcard.jd.com/market/index.action">LB-E卡</a></div>
							<div><a target="_blank" href="http://channel.jd.com/jiazhuang.html">家装城</a></div>
							<div><a target="_blank" href="http://dapeigou.jd.com/">搭配购</a></div>
							<div><a target="_blank" href="http://xihuan.jd.com/">我喜欢</a></div>
						</dd>
					</dl>
					<dl class="item fore2">
						<dt>企业服务</dt>
						<dd>
							<div><a target="_blank" href="http://giftcard.jd.com/company/index">企业客户</a></div>
							<div><a target="_blank" href="http://sale.jd.com/p10997.html">办公直通车</a></div>
						</dd>
					</dl>
					<dl class="item fore3">
						<dt>旗下网站</dt>
						<dd>
							<div><a target="_blank" href="http://en.jd.com/">English Site</a></div>
						</dd>
					</dl>
				</div>
			</li>
		</ul>
		<span class="clr"></span>
	</div>
</div>