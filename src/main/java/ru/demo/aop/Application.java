package ru.demo.aop;

import org.springframework.aop.framework.ProxyFactory;
import ru.demo.aop.dto.CarDto;
import ru.demo.aop.dto.Drivable;

public class Application {

    public static void main(String[] args) {

        ProxyFactory proxyFactory = new ProxyFactory(CarDto.builder().build());

        proxyFactory.addInterface(Drivable.class);
        //proxyFactory.addAdvice(new RetryAdvice());

        Drivable drivable = (Drivable) proxyFactory.getProxy();

        drivable.drive();

    }

}
