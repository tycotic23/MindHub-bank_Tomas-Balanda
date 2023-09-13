package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CardUtilsTest {

    @Test
    void generateCvvTest() {
        short cvv=CardUtils.generateCvv();

        assertTrue(cvv<=999 && cvv>0);
    }

    @Test
    void generateCardNumberTest() {
        String cardNumber = CardUtils.generateCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
        assertEquals(cardNumber.length(),19);
    }
}