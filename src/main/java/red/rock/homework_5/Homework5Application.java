package red.rock.homework_5;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@ServletComponentScan
@MapperScan("red.rock.homework_5.Mapper")
@EnableScheduling
@SpringBootApplication
public class Homework5Application {

    public static void main(String[] args) {
        SpringApplication.run(Homework5Application.class, args);
    }

}
