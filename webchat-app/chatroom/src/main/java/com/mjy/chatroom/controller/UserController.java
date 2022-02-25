package com.mjy.chatroom.controller;


import com.mjy.chatroom.entity.ChatMsg;
import com.mjy.chatroom.entity.FriendsRequest;
import com.mjy.chatroom.entity.MyFriends;
import com.mjy.chatroom.entity.Users;
import com.mjy.chatroom.entity.bo.UsersBo;
import com.mjy.chatroom.entity.vo.FriendRequestVo;
import com.mjy.chatroom.entity.vo.MyFriendsVo;
import com.mjy.chatroom.entity.vo.UsersVo;
import com.mjy.chatroom.exception.GlobalException;
import com.mjy.chatroom.redis.RedisService;
import com.mjy.chatroom.redis.key.FriendsKey;
import com.mjy.chatroom.service.UserService;
import com.mjy.chatroom.util.*;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author
 * @description
 * @create 2021-11-03 13:07
 */
@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private RedisService redisService;

    @PostMapping("/register")
    @ResponseBody
    public ReturnResult register(Users user){
        if(StringUtils.isEmpty(user.getUsername())||StringUtils.isEmpty(user.getPassword())){
            throw new GlobalException(CodeMsg.LOGIN_PARAM_NULL);
        }

        Users userExist = userService.isUserExist(user.getUsername());
        if(userExist!=null){
            return ReturnResult.error().message(ConstantsUtil.USER_ALREADY_EXIST);
        }

        user.setPassword(MD5Utils.encodeByMd5(user.getPassword()));
        if(StringUtils.isEmpty(user.getNickname())){
           user.setNickname("");
        }
        user.setQrcode("");
        user.setFaceImageBig("");
        user.setFaceImage("");
        userService.saveUser(user);
        return ReturnResult.success();
    }


    @RequestMapping("/login")
    @ResponseBody
    public ReturnResult loginOrRegister(Users user){
        String username = user.getUsername();
        String password = user.getPassword();
        if(username==null||username.length()==0||password==null||password.length()==0){
            throw new GlobalException(CodeMsg.LOGIN_PARAM_NULL) ;
        }

        // 通过用户名判断该用户是否存在
        Users userExist=userService.isUserExist(username);

        // 该用户存在
        if(userExist!=null){
            if(!userExist.getPassword().equals(MD5Utils.encodeByMd5(password))){
                throw new GlobalException(CodeMsg.Login_PARAM_ERROR);
            }
            UsersVo usersVo = new UsersVo();
            BeanUtils.copyProperties(userExist,usersVo);

            return ReturnResult.success().data(usersVo);
        }else{
            return ReturnResult.error().message(ConstantsUtil.USER_NOT_EXIST);
        }



    }
    @PostMapping("/editNickname")
    @ResponseBody
    public ReturnResult editNickname(UsersVo usersVo){
        if(!StringUtils.isEmpty(usersVo)){
            boolean flag=userService.editNickname(usersVo);

            if(flag){
                return ReturnResult.success();
            }
        }

        return ReturnResult.error().message("修改失败");
    }

    @PostMapping("/uploadFaceBase64")
    @ResponseBody
    public ReturnResult uploadPhoto(UsersBo usersBo){

        try {
            if(usersBo==null||usersBo.getUserId()==null||usersBo.getFaceData()==null){
                throw new GlobalException(CodeMsg.PHOTO_PARAM_NULL);
            }
            // 获取前端传过来的base64c字符串，然后转为文件对象进行上传
            String base64Data = usersBo.getFaceData();

            String[] split = base64Data.split("base64,");
            if(split==null&&split.length!=2){
                throw new GlobalException(CodeMsg.ILLEGAL_PARAM_TYPE);
            }
            // 获取图片的类型
            String contentType=split[0].substring(5,split[0].length()-1);
            String imageType=contentType.substring(contentType.lastIndexOf("/")+1);
            System.out.println(imageType);
            String userFacePath=ConstantsUtil.IMAGE_PATH+usersBo.getUserId()+"userFaceBase64."+imageType;

            // 调用FileUtils类中的方法将base64的字符串转化为对象
            FileUtils.base64ToFile(userFacePath,split[1]);
            MultipartFile multipartFile = FileUtils.fileToMultipart(userFacePath,imageType,contentType);

            // 获取fastDFS上传图片的路径
            String path = fastDFSClient.uploadBase64(multipartFile);
            System.out.println(path);
            String thump="_150x150.";
            String[] arr = path.split("\\.");

            String thumpUrl=arr[0]+thump+arr[1];

            // 更新用户头像
            Users users = new Users();
            users.setId(usersBo.getUserId());
            users.setFaceImage(thumpUrl);
            users.setFaceImageBig(path);

            boolean isSuccess=userService.updateImg(users);

            if(isSuccess){
                return ReturnResult.success().data(users);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ReturnResult.error().message("修改头像失败");
    }

    @PostMapping("/toAddFriendsPage")
    @ResponseBody
    public ReturnResult toAddFriendPage(@RequestParam("userId") String userId,@RequestParam("username") String username){
        if(userId==null||userId.length()==0||username==null||username.length()==0){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        ReturnResult res=userService.searchFriends(userId,username);

        return res;
    }

    @PostMapping("/addFriendReq")
    @ResponseBody
    public ReturnResult addFriend(FriendsRequest friendRequest){
        if(friendRequest.getSendUserId()==null||friendRequest.getAcceptUserId()==null||friendRequest.getSendUserId().length()==0||friendRequest.getAcceptUserId().length()==0){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        boolean flag=userService.sendAddRequest(friendRequest);

        if(flag){
            return ReturnResult.success();
        }else{
            return ReturnResult.error().message("添加请求失败");
        }
    }

    @GetMapping("/getRequestUser")
    @ResponseBody
    public ReturnResult getRequestUsers(String userId){
        if(userId==null||userId.length()==0){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        List<FriendRequestVo> senders=userService.getSendReqUsersInfo(userId);

        System.out.println(senders);
        return ReturnResult.success().data(senders);
    }

    @PostMapping("/addFriend")
    @ResponseBody
    public ReturnResult addFriend(MyFriends mFriends){
        if(mFriends.getMyUserId()==null||mFriends.getMyUserId().length()==0||mFriends.getMyFriendUserId()==null
        ||mFriends.getMyFriendUserId().length()==0){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        List<MyFriendsVo> myFriendsVos = userService.addFriend(mFriends);

        return ReturnResult.success().data(myFriendsVos);
    }

    @PostMapping("/ignoreFriend")
    @ResponseBody
    public ReturnResult ignoreFriend(MyFriends mFriends){
        if(mFriends.getMyUserId()==null||mFriends.getMyUserId().length()==0||mFriends.getMyFriendUserId()==null
                ||mFriends.getMyFriendUserId().length()==0){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        userService.ignoreFriend(mFriends);

        return ReturnResult.success();
    }

    @GetMapping("/getRequestNum")
    @ResponseBody
    public ReturnResult getRequestNum(@RequestParam("userId") String userId){
        if(userId==null||userId.length()==0){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        Integer nums=userService.getRequestNum(userId);

        return ReturnResult.success().data(nums);
    }

    @GetMapping("/getMyFriends")
    @ResponseBody
    public ReturnResult getMyFriends(@RequestParam("userId") String userId){
        if(userId==null||userId.length()==0){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        // 从缓存中获取数据
        String str = redisService.get(FriendsKey.MY_FRIENDS, userId, String.class);
        if(str==null||str.length()==0){
            List<MyFriendsVo> myFriendsVos = JSON.parseArray(str, MyFriendsVo.class);
            System.out.println(myFriendsVos);
            if(myFriendsVos!=null){
                return ReturnResult.success().data(myFriendsVos);
            }
        }

        // 若缓存中未命中，就查数据库
        List<MyFriendsVo> friends=userService.getFriends(userId);

        System.out.println(friends);

        return ReturnResult.success().data(friends);
    }

    @GetMapping("/getUnReadMsg")
    @ResponseBody
    public ReturnResult getUnReadMsg(@RequestParam("accepterId") String accepterId){
        if(StringUtils.isEmpty(accepterId)){
            return ReturnResult.error().message(ConstantsUtil.PARAM_NULL_ERROR);
        }

        List<ChatMsg> chatMsgs= userService.queryUnReadMsg(accepterId);

        return ReturnResult.success().data(chatMsgs);
    }

}
