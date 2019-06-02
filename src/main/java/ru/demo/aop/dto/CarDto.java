package ru.demo.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDto implements Drivable {

    private String number;

    @Override
    public void drive() {
        System.out.println("Drive");
    }
}
