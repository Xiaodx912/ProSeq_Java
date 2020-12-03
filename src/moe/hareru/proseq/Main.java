package moe.hareru.proseq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        DataMgr d = new DataMgr(Paths.get("./data/testData"));
        logger.debug(d.getSeq("1AKHA"));
    }
}
