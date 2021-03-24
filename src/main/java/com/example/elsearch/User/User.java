package com.example.elsearch.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @ClassName: User
 * @Description: TODO
 * @author: 大佬
 * @Date: 2021/3/24 20:25
 * @Version: 1.0
 **/
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private int age;
}
