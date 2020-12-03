package moe.hareru.proseq;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DataMgrTest {

    private static final Logger logger = LoggerFactory.getLogger(DataMgrTest.class);

    @Test
    void getSeq() {
        logger.debug("Testing DataMgr.getSeq");
        DataMgr d=new DataMgr(Paths.get("./data/testData"));
        assertEquals("KKEKSPKGKSSISPQARAFLEEVFRRKQSLNSKEKEEVAKKCGITPLQVRVWFINKRMRSK",d.getSeq("1AKHA"));
    }
}