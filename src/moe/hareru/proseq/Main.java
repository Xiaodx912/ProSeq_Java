package moe.hareru.proseq;

import org.ahocorasick.trie.PayloadEmit;
import org.ahocorasick.trie.PayloadTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        DataMgr d = new DataMgr(Paths.get("./data/testData"));
        logger.debug(d.getSeq("1AKHA"));

        WordBag_Encoder enc=new WordBag_Encoder(new String[]{"KKE","AKK","BCC"});
        //WordBag_Encoder enc=new WordBag_Encoder();
        logger.debug(Arrays.toString(enc.Encode(new String[]{"AKKAEKKAEEBCCAKKE","AKKE"})));
    }
}
