package dic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


/**
 * @author aiyk
 */
@SpringBootApplication//(scanBasePackages = {"dic.entity"})
@MapperScan("dic.mapper")
public class SpringbootApplication {


//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(SpringbootApplication.class);
//    }


    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
