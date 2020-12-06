package moe.hareru.proseq;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class OneHot_EncoderTest {
    private static final Logger logger = LoggerFactory.getLogger(OneHot_EncoderTest.class);

    @Test
    void encode() {
        logger.debug("Testing OneHot_Encoder.encode");
        OneHot_Encoder enc=new OneHot_Encoder();
        assertArrayEquals(new String[]{"00010000010000011001", "00100000010111000000", "10010000100000000100"},enc.Encode(new String[]{"STLYE", "QLDNP", "AEVKK"}));
    }
}

class WordBag_EncoderTest {
    private static final Logger logger = LoggerFactory.getLogger(WordBag_EncoderTest.class);

    @Test
    void encode() {
        logger.debug("Testing WordBag_Encoder.encode");
        WordBag_Encoder enc=new WordBag_Encoder(new String[]{"KKE","AKK","BCC"});
        assertArrayEquals(new String[]{"121", "110"},enc.Encode(new String[]{"AKKAEKKAEEBCCAKKE","AKKE"}));
    }
}