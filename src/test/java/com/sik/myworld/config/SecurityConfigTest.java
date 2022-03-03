package com.sik.myworld.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testEncode(){
        String password= "0710";
        String enPw = passwordEncoder.encode(password);

        System.out.println("enPw = " + enPw);

        boolean matches = passwordEncoder.matches(password, enPw);
        System.out.println("matches = " + matches);
    }
}