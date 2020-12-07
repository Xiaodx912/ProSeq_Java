package moe.hareru.proseq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class utilsTest {

    @Test
    void WB_builder() {
        assertArrayEquals(new String[]{"A", "B", "AA", "BA", "AB", "BB"}, utils.WB_builder(2, "AB"));
    }
}