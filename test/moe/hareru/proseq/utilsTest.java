package moe.hareru.proseq;

import de.bwaldvogel.liblinear.FeatureNode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class utilsTest {
    private static final Logger logger = LoggerFactory.getLogger(utilsTest.class);

    @Test
    void WB_builder() {
        assertArrayEquals(new String[]{"A", "B", "AA", "BA", "AB", "BB"}, utils.WB_builder(2, "AB"));
    }

}