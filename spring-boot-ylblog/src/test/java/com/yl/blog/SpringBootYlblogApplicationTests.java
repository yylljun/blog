package com.yl.blog;


import com.yl.blog.repository.es.EsBlogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootYlblogApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private EsBlogRepository blogRepository;



    @Test
    public void testEs(){

    }
}
