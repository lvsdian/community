package cn.andios.majiangcommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "cn.andios.majiangcommunity.mapper")
public class MajiangCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajiangCommunityApplication.class, args);
    }

}
