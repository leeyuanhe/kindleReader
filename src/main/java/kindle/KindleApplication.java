package kindle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @description 启动类
 * @author hely
 * @date 2017-08-22
 * @param
 */

@SpringBootApplication()
@MapperScan(basePackages = "kindle.mapper")
public class KindleApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(KindleApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(KindleApplication.class,args);
    }
}
