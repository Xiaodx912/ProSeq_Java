package moe.hareru.proseq;

import org.ahocorasick.trie.PayloadEmit;
import org.ahocorasick.trie.PayloadTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static java.lang.Integer.min;

public interface Encoder {
    String[] Encode(String[] seq);
}

class OneHot_Encoder implements Encoder {
    private static final Logger logger = LoggerFactory.getLogger(OneHot_Encoder.class);
    private static final String default_a_acid = "ACDEFGHIKLMNPQRSTVWY";
    private final int[] acc_map;
    private final int vecDim_count;

    public OneHot_Encoder() {
        this(default_a_acid);
    }

    public OneHot_Encoder(String a_acid) {
        logger.debug("OH_enc init with AminoAcid list include {} items.", a_acid.length());
        this.vecDim_count = a_acid.length();
        acc_map = new int[256];
        for (int i = 0; i < a_acid.length(); i++) acc_map[a_acid.charAt(i)] = i + 1;
        logger.debug("OH_enc accelerate map build fin.");
    }

    public String[] Encode(String[] seq) {
        String[] ans = new String[seq.length];
        for (int i = 0; i < seq.length; ++i) {
            boolean[] result = new boolean[vecDim_count];
            for (char ch : seq[i].toCharArray()) {
                if (acc_map[ch] != 0) {
                    result[acc_map[ch] - 1] = true;
                }
            }
            StringBuilder tmp = new StringBuilder();
            for (boolean b : result) {
                tmp.append(b ? "1" : "0");
            }
            ans[i] = tmp.toString();
        }
        return ans;
    }
}

class WordBag_Encoder implements Encoder {
    private static final Logger logger = LoggerFactory.getLogger(WordBag_Encoder.class);
    private String[] WB_dic;
    private PayloadTrie<Integer> AC_Automaton;

    public WordBag_Encoder() {
        this(new String[]{"KKE", "LEE"});
    }

    public WordBag_Encoder(String[] dic) {
        logger.debug("WB_enc init with {} words.", dic.length);
        this.WB_dic = dic.clone();
        PayloadTrie.PayloadTrieBuilder<Integer> T_Builder = PayloadTrie.builder();
        logger.debug("building Trie tree for AC_automaton...");
        for (int i = 0; i < WB_dic.length; ++i) T_Builder.addKeyword(WB_dic[i], i);
        AC_Automaton = T_Builder.build();
        logger.debug("Trie tree ready.");
    }

    public String[] Encode(String[] seq) {
        String[] ans = new String[seq.length];
        for (int i = 0; i < seq.length; ++i) {
            int[] result = new int[this.WB_dic.length];
            Collection<PayloadEmit<Integer>> emits = AC_Automaton.parseText(seq[i]);
            for (PayloadEmit<Integer> emit : emits) ++result[emit.getPayload()];
            StringBuilder tmp = new StringBuilder();
            for (Integer b : result) {
                b = min(b, 9);
                tmp.append(b.toString());
            }
            ans[i] = tmp.toString();
        }
        return ans;
    }
}
