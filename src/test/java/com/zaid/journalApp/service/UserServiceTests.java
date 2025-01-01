package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

enum Color {
    A, B, C
}
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;
    @Disabled
    @Test
    public void testGetUser() {
        User user = userRepository.findByUsername("zaid");
        assertTrue(user.getJournalIds().isEmpty());
//        assertNotNull(userRepository.findByUsername("zaid"));
//        assertEquals(4,2+2);
    }
    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,2,3",
            "2,3,4"
    })
    public void test(int a,int b,int expected)  {
        assertEquals(expected,a+b,"not equal for "+a+b);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1,
            2,
            3
    })
    public void test2(int a) {
        assertEquals(4,a+2,"failed for int value : "+a);
    }

    @ParameterizedTest
    @EnumSource(Color.class)
    public void test3(Color color) {
        assertEquals(Color.A, color, "failed for enum value: " + color);
    }

}
