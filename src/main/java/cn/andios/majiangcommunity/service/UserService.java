package cn.andios.majiangcommunity.service;

import cn.andios.majiangcommunity.mapper.UserMapper;
import cn.andios.majiangcommunity.model.User;
import cn.andios.majiangcommunity.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/20/15:22
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setId(System.currentTimeMillis());
            userMapper.insert(user);
        }else{
            //更新
            User dbUser = users.get(0);

            User updateUser = new User();
            updateUser.setGmtModified(user.getGmtCreate());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());

            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());

            userMapper.updateByExampleSelective(updateUser,example);
        }
    }
}
