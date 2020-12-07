package moe.hareru.proseq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.*;

public class DataMgr {
    private static final Logger logger = LoggerFactory.getLogger(DataMgr.class);
    private Map<String, String> seqDic;

    public DataMgr(Path data_path) {
        logger.debug("DMgr init with file:" + data_path);
        String data;
        try {
            data = new String(Files.readAllBytes(data_path));
        } catch (Exception e) {
            logger.error("Except:" + e);
            e.printStackTrace();
            return;
        }
        logger.debug("File OK.");
        this.seqDic = new HashMap<>();
        String[] buffer = data.split("\n");
        for (int i = 0; i < buffer.length; i += 2) {
            while (!buffer[i].startsWith(">")) ++i;
            seqDic.put(buffer[i].substring(1).trim(), buffer[i + 1].trim());
        }
        logger.debug("sepDic init OK");
    }

    public String getSeq(String ident) {
        return seqDic.get(ident);
    }

    public int size() {
        return seqDic.size();
    }

    public String[] getAll() {
        return seqDic.values().toArray(new String[this.size()]);
    }
}
