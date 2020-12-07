package moe.hareru.proseq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Arrays;

import static moe.hareru.proseq.utils.WB_builder;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        DataMgr d = new DataMgr(Paths.get("./data/testData"));
        logger.debug(d.getSeq("1AKHA"));

        WordBag_Encoder enc = new WordBag_Encoder(new String[]{"KKE", "AKK", "BCC"});
        logger.debug(Arrays.toString(enc.Encode(new String[]{"AKKAEKKAEEBCCAKKE", "AKKE"})));

        logger.debug(Arrays.toString(WB_builder(2, "AB")));
        //WordBag_Encoder full_enc=new WordBag_Encoder(WB_builder(3));
        //logger.debug(Arrays.toString(full_enc.Encode(d.getAll())));
    }
}
