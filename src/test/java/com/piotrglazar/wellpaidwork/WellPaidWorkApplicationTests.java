package com.piotrglazar.wellpaidwork;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WellPaidWorkApplicationTests {

    @Value("${sense.of.the.universe}")
    public int magicValue = 0;

    @Test
    public void contextLoads() {
        // given context loaded

        // expect
        assertThat(magicValue).isEqualTo(42);
    }

}
