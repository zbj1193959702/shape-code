package com.biji.puppeteer;

import com.biji.puppeteer.util.ApolloUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * create by biji.zhao on 2020/11/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShapeCodeApplication.class})
public class ApolloTest {

    @Test
    public void test_readApollo() {
        Integer limitCount = ApolloUtil.getLimitCount();
        System.out.println(limitCount);
        assert limitCount != 100;
    }
}
