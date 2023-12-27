package org.hsgt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"org.hsgt.pricing.mapper", "org.hsgt.order.mapper", "org.hsgt.core.mapper"})
public class MainHsgtApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainHsgtApplication.class, args);
    }
}
