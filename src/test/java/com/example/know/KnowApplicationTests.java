package com.example.know;

import com.example.know.dao.UserDao;
import com.example.know.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class KnowApplicationTests {
    @Autowired
    private UserDao userDao;

    @Test
    public void testUserMapper() {
        List<User> users = userDao.selectList(null);
        System.out.println(users);
    }

}
