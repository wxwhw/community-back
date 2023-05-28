package com.majiang.community;

import com.majiang.community.mapper.UserMapper;
import com.majiang.community.pojo.User;
import com.majiang.community.pojo.UserToken;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MajiangApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void testMappper() {
        List<User> userList = userMapper.selectPage("a", "17", "", "", 0, 5);
        System.out.println(userList);
    }

}
