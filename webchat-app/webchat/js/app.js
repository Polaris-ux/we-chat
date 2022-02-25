
window.app={
	
	// 后端服务器地址
	// 192.168.43.120 10.17.131.174
	serverUrl:"http://10.17.131.174:9001/",
	
	// 图片服务器地址
	imageServerUrl:"http://116.62.122.89:8888/spring/",
	
	// webSocket地址
	websocketUrl:"ws://10.17.131.174:9000/chat",
	
	// 第一次(或重连)初始化连接
	CONNECT:1,
	// 聊天消息
	CHAT:2,
	// 消息签收
	SIGNED:3,
	// 客户端保持长连接
	KEEPALIVE:4,
	// 拉取好友
	PULL_FRIEND:5,
	
	/**
	 * 判断字符串是否为空
	 * @param {Object} str
	 */
	isNotNUll:function(str){
		if(str!=null&&str.length!=0&&str!=undefined){
			return true;
		}
		return false;
	},
	
	/**
	 * 封装消息提示框
	 * @param {Object} msg：消息内容
	 * @param {Object} type:消息的类型
	 */
	showToAst:function(msg,type){
		plus.nativeUI.toast(msg,{
			icon:"../images/"+type+".png",verticalAlign:"center"
		});
	},
	
	/**
	 * 保存用户的全局对象
	 * @param {Object} userInfo
	 */
	setUserGlobalInfo:function(userInfo){
		var userInfoStr=JSON.stringify(userInfo);
		plus.storage.setItem("userInfo",userInfoStr);
	},
	
	/**
	 * 获取用户的全局对象
	 */
	getUserInfo:function(){
		var userInfo=plus.storage.getItem("userInfo");
		
		return JSON.parse(userInfo);
	},
	/**
	 * 用户退出
	 */
	userLogout:function(){
		// 将用户信息移除
		plus.storage.removeItem("userInfo");
		// // 关闭id为index的WebView，防止退出再次登录时，index.html不刷新
		plus.webview.getWebviewById("index").close();
		var hbuilderMv=plus.webview.getWebviewById("HBuilder");
		// var loginMv=plus.webview.getWebviewById("login");
		if(hbuilderMv!=null){
			hbuilderMv.hide();
		}
	},
	/**
	 * 保存好友列表
	 */
	setContactList:function(friendsList){
		var friends=JSON.stringify(friendsList);
		plus.storage.setItem("contactList",friends);
	},
	/**
	 * 获取好友列表
	 */
	getContactList:function(){
		var me=this;
		var friends=plus.storage.getItem("contactList");
		if(!me.isNotNUll(friends)){
			return [];
		}
		
		return JSON.parse(friends);
	},
	getFriendById:function(friendId){
		var contactList=this.getContactList();
		for (var i = 0; i < contactList.length; i++) {
			if(contactList[i].friendId==friendId){
				return contactList[i];
			}
		}
		return null;
	},
	/**
	 * 保存请求列表
	 */
	setRequestList:function(requestList){
		var requests=JSON.stringify(requestList);
		plus.storage.setItem("requestList",requests);
	},
	/**
	 * 获取请求列表
	 */
	getContatcList:function(){
		var requests=plus.storage.getItem("requestList");
		return requests;
	},
	
	/**
	 * 与后端的ChatContext对应
	 * @param {Object} senderId
	 * @param {Object} receiverId
	 * @param {Object} msg
	 * @param {Object} msgId
	 */
	ChatContext:function(senderId,receiverId,msg,msgId){
		this.senderId=senderId;
		this.receiverId=receiverId;
		this.msg=msg;
		this.msgId=msgId;
	},
	
	/**
	 * 与后端DataContext对应
	 * @param {Object} action
	 * @param {Object} chatContext
	 * @param {Object} extend
	 */
	DataContext:function(action,chatContext,extend){
		this.action=action;
		this.chatContext=chatContext;
		this.extend=extend;
	},
	/**
	 * 存储历史聊天记录的对象
	 * @param {Object} userId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} flag
	 */
	HistoryMsg:function(userId,friendId,msg,flag){
		this.userId=userId;
		this.friendId=friendId;
		this.msg=msg;
		this.flag=flag;
	},
	/**
	 * 保存历史聊天记录
	 * @param {Object} userId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} flag:1代表自己，2代表好友
	 */
	saveChatHistoryMsg:function(userId,friendId,msg,flag){
		var me=this;
		var chatKey="chat-"+userId+"-"+friendId;
		// 从本地缓存中获取聊天记录是否存在
		var chatHistoryMsgListStr=plus.storage.getItem(chatKey);
		
		// 用于存储聊天记录的变量
		var chatHistoryMsgList;
		if(me.isNotNUll(chatHistoryMsgListStr)){
			chatHistoryMsgList=JSON.parse(chatHistoryMsgListStr);
		}else{
			chatHistoryMsgList=[];
		}
		var newChatHistoryMsgList;
		if(chatHistoryMsgList.length>100){
			newChatHistoryMsgList=[];
			var len=chatHistoryMsgList.length;
			for (var i = len-100; i <100; i++) {
				newChatHistoryMsgList.push(chatHistoryMsgList[i]);
			}
		}else{
			newChatHistoryMsgList=chatHistoryMsgList;
		}
		console.log(JSON.stringify(newChatHistoryMsgList));
		// 构建聊天记录对象
		var historyMsg=new me.HistoryMsg(userId,friendId,msg,flag);
		
		newChatHistoryMsgList.push(historyMsg);
		
		// 存储聊天记录
		plus.storage.setItem(chatKey,JSON.stringify(newChatHistoryMsgList));
		
	},
	/**
	 * 获取历史聊天记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	getHistoryChatMsgList:function(myId,friendId){
		var me=this;
		var chatKey="chat-"+myId+"-"+friendId;
		var msgStr=plus.storage.getItem(chatKey);
		return JSON.parse(msgStr);
	},
	/**
	 * 快照对象
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} isRead
	 * @param {Object} num
	 */
	SnapShot:function(myId,friendId,msg,isRead,num){
		this.myId=myId;
		this.friendId=friendId;
		this.msg=msg;
		this.isRead=isRead;
		this.num=num;
		
	},
	saveChatSnapshot:function(myId,friendId,msg,isRead){
		var shotKey="snapshot-"+myId;
		var me=this;
		var snapshotList;
		// 获取历史快照
		var snapshotListStr=plus.storage.getItem(shotKey);
		console.log(snapshotListStr);
		if(me.isNotNUll(snapshotListStr)&&snapshotListStr!="[null]"){
			snapshotList=JSON.parse(snapshotListStr);
		}else{
			snapshotList=[];
		}
		// 未读消息的条数
		var num=0;
		for (var i = 0; i < snapshotList.length; i++) {
			if(snapshotList[i].friendId==friendId){
				// 删除之前的快照，更新为新的快照
				num=snapshotList[i].num;
				snapshotList.splice(i,1);
				break;
			}
		}
		if(isRead){
			num=0;
		}else{
			num=num+1;
		}
		
		// 创建新的快照对象
		var snapshot=new me.SnapShot(myId,friendId,msg,isRead,num);
		// 将新的快照对象放到最前面
		snapshotList.unshift(snapshot);
		
		plus.storage.setItem(shotKey,JSON.stringify(snapshotList));
	},
	/**
	 * 获取当前的消息快照
	 * @param {Object} myId
	 */
	getSnapshotList:function(myId){
		var me=this;
		var shotKey="snapshot-"+myId;
		var snapshotList;
		var snapShotListStr=plus.storage.getItem(shotKey);
		if(me.isNotNUll(snapShotListStr)){
			snapshotList=JSON.parse(snapShotListStr);
		}else{
			snapshotList=[];
		}
		return snapshotList;
	},
	/**
	 * 将快照的状态改为已读
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	readSnapshot:function(myId,friendId){
		var shotKey="snapshot-"+myId;
		var snapshotList=this.getSnapshotList(myId);
		for (var i = 0; i < snapshotList.length; i++) {
			if(snapshotList[i].friendId==friendId){
				snapshotList[i].isRead=true;
				break;
			}
		}
		plus.storage.setItem(shotKey,JSON.stringify(snapshotList));
	},
	/**
	 * 删除快照
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	deleteSnapshot:function(myId,friendId){
		var shotKey="snapshot-"+myId;
		var snapshotList=this.getSnapshotList(shotKey);
		for (var i = 0; i < snapshotList.length; i++) {
			if(snapshotList[i].friendId==friendId){
				// 删除快照
				snapshotList.splice(i,1);
				break;
			}
		}
		plus.storage.setItem(shotKey,JSON.stringify(snapshotList));
	},
	/**
	 * 将需要的内容放在最上面
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	snapshotToTop:function(myId,friendId){
		var msg;
		var isRead;
		var num;
		var shotKey="snapshot-"+myId;
		var snapshotList=this.getSnapshotList(myId);
		for (var i = 0; i < snapshotList.length; i++) {
			if(snapshotList[i].friendId==friendId){
				msg=snapshotList[i].msg;
				isRead=snapshotList[i].isRead;
				num=snapshotList[i].num;
				snapshotList.splice(i,1);
				break;
			}
		}
		
		// 创建新的快照对象
		var snapshot=new this.SnapShot(myId,friendId,msg,isRead,num);
		// 将新的快照对象放到最前面
		snapshotList.unshift(snapshot);
		
		plus.storage.setItem(shotKey,JSON.stringify(snapshotList));
	}
	
	
}