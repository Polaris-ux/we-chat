package com.mjy.chatroom.service.impl;

import com.mjy.chatroom.entity.ChatMsg;
import com.mjy.chatroom.entity.FriendsRequest;
import com.mjy.chatroom.entity.MyFriends;
import com.mjy.chatroom.entity.Users;
import com.mjy.chatroom.entity.vo.FriendRequestVo;
import com.mjy.chatroom.entity.vo.MyFriendsVo;
import com.mjy.chatroom.entity.vo.UsersVo;
import com.mjy.chatroom.enums.MsgActionEnum;
import com.mjy.chatroom.exception.GlobalException;
import com.mjy.chatroom.idworker.Sid;
import com.mjy.chatroom.mapper.ChatMsgMapper;
import com.mjy.chatroom.mapper.FriendsRequestMapper;
import com.mjy.chatroom.mapper.MyFriendsMapper;
import com.mjy.chatroom.mapper.UsersMapper;
import com.mjy.chatroom.netty.interact.DataContext;
import com.mjy.chatroom.netty.interact.UserAndChannel;
import com.mjy.chatroom.redis.RedisService;
import com.mjy.chatroom.redis.key.FriendsKey;
import com.mjy.chatroom.service.UserService;
import com.mjy.chatroom.util.*;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @description
 * @create 2021-11-02 23:49
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qRCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public Users getUserById(String id) {

        return usersMapper.selectByPrimaryKey(id);
    }

    @Override
    public Users isUserExist(String username) {

        return usersMapper.getUserByName(username);
    }

    @Override
    public Users saveUser(Users user) {
        user.setId(sid.nextShort());

        // 生成用户唯一的二维码
        String filePath= ConstantsUtil.QRCODE_PATH+"qrcode_"+user.getId()+".png";
        String content="qrcode_"+user.getUsername()+"_"+user.getId();
        qRCodeUtils.createQRCode(filePath,content);
        MultipartFile multipartFile = FileUtils.fileToMultipart(filePath);
        String qrCode = "";
        try {
            qrCode=fastDFSClient.uploadQRCode(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if("".equals(qrCode)){
            throw new GlobalException(CodeMsg.CREATE_QRCODE_ERROR);
        }
        user.setQrcode(qrCode);

        int flag = usersMapper.insertSelective(user);

        if(flag>0){
            return user;
        }else{
            throw new GlobalException(CodeMsg.INSERT_USER_ERROR);
        }
    }

    @Override
    public boolean editNickname(UsersVo usersVo) {

        int res=usersMapper.updateNickname(usersVo);
        return res>0;
    }

    @Override
    public boolean updateImg(Users user) {
        int flags = usersMapper.updateByPrimaryKeySelective(user);
        if(flags>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ReturnResult searchFriends(String userId,String username) {
        Users userByName = usersMapper.getUserByName(username);

        // 搜索好友不存在时
        if(userByName==null){
            return ReturnResult.error().message(ConstantsUtil.SEARCH_FRIENDS_NOT_EXIST);
        }

        // 搜索到的是自己时
        if(userId.equals(userByName.getId())){
            return ReturnResult.error().message(ConstantsUtil.CAN_NOT_ADD_SELF);
        }

        // 当搜索的用户已经是自己的好友时
        MyFriends friend=myFriendsMapper.getFriendRecord(userId,userByName.getId());
        if(friend!=null){
            return ReturnResult.error().message(ConstantsUtil.ALREADY_FRIENDS);
        }

        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(userByName,usersVo);

        return ReturnResult.success().data(usersVo);
    }

    @Override
    public boolean sendAddRequest(FriendsRequest friendRequest) {
        // 设置id
        friendRequest.setId(sid.nextShort());

        // 设置发送请求的时间
        friendRequest.setRequestDateTime(new Date());
        int insert = friendsRequestMapper.insert(friendRequest);
        return insert>0;
    }

    @Override
    public List<FriendRequestVo> getSendReqUsersInfo(String userId) {

        return usersMapper.getUsersByAcceptId(userId);
    }

    @Transactional
    @Override
    public List<MyFriendsVo> addFriend(MyFriends mFriends) {

        // 在好友表中加上两条记录，两边都需要加
        if(saveFriend(mFriends.getMyUserId(),mFriends.getMyFriendUserId())<=0||saveFriend(mFriends.getMyFriendUserId(),mFriends.getMyUserId())<=0){
            throw new GlobalException(CodeMsg.ADD_FRIEND_ERROR);
        }


        // 将好友请求中的记录删除掉
        int delete=friendsRequestMapper.removeRecord(mFriends.getMyUserId(),mFriends.getMyFriendUserId());
        if(delete<=0){
            throw new GlobalException(CodeMsg.REMOVE_REQUEST_ERROR);
        }

        // 获取用户的好友
        List<MyFriendsVo> myFriendsVos = queryFriendsById(mFriends.getMyUserId());

        // 拉取好友
        // 使用websocket主动推送消息到请求发起者
        DataContext dataContext = new DataContext();
        dataContext.setAction(MsgActionEnum.PULL_FRIEND.type);

        Channel channel = UserAndChannel.getChannel(mFriends.getMyFriendUserId());
        if(channel!=null){
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.ObjectToJSON(dataContext)));
        }
        return myFriendsVos;
    }

    @Override
    public void ignoreFriend(MyFriends mFriends) {
        int delete=friendsRequestMapper.removeRecord(mFriends.getMyUserId(),mFriends.getMyFriendUserId());
        if(delete<=0){
            throw new GlobalException(CodeMsg.REMOVE_REQUEST_ERROR);
        }

    }

    @Override
    public int saveFriend(String userId, String friendId) {
        MyFriends myFriends = new MyFriends();
        myFriends.setId(sid.nextShort());
        myFriends.setMyUserId(userId);
        myFriends.setMyFriendUserId(friendId);

        return myFriendsMapper.insert(myFriends);
    }

    @Override
    public List<MyFriendsVo> queryFriendsById(String userId) {
        return myFriendsMapper.selectFriendsById(userId);
    }

    @Override
    public int getRequestNum(String userId) {
        return friendsRequestMapper.selectRequests(userId);
    }

    @Transactional
    @Override
    public List<MyFriendsVo> getFriends(String userId) {
        List<MyFriendsVo> myFriendsVos = myFriendsMapper.selectFriendsById(userId);
        if(myFriendsVos!=null){
            redisService.set(FriendsKey.MY_FRIENDS,userId,myFriendsVos);
        }
        return myFriendsVos;
    }

    @Override
    public String saveChatMsg(ChatMsg chatMsg) {
        chatMsg.setId(sid.nextShort());
        chatMsgMapper.insert(chatMsg);
        return chatMsg.getId();
    }

    @Override
    public void doMsgSign(List<String> msgIds) {
        chatMsgMapper.updateSignFlag(msgIds);
    }

    @Override
    public List<ChatMsg> queryUnReadMsg(String accepterId) {

        return chatMsgMapper.selectMsgByAccepterId(accepterId);
    }


}
