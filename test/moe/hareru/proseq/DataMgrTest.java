package moe.hareru.proseq;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataMgrTest {

    private static final Logger logger = LoggerFactory.getLogger(DataMgrTest.class);

    @Test
    void getSeq() {
        logger.debug("Testing DataMgr.getSeq");
        DataMgr d = new DataMgr(Paths.get("./data/testData"));
        assertEquals("KKEKSPKGKSSISPQARAFLEEVFRRKQSLNSKEKEEVAKKCGITPLQVRVWFINKRMRSK", d.getSeq("1AKHA"));
        assertEquals(10, d.size());
        assertArrayEquals(new String[]{"MELPIAPIGRIIKDAGAERVSDDARITLAKILEEMGRDIASEAIKLARHAGRKTIKAEDIELAVRRFKK", "MEFDYVICEECGKEFMDSYLMDHFDLPTCDDCRDADDKHKLITKTEAKQEYLLKDCDLEKREPPLKFIVKKNPHHSQWGDMKLYLKLQIVKRSLEVWGSQEALEEAKEVRQ"}, Arrays.copyOfRange(d.getAll(), 8, 10));
    }
}