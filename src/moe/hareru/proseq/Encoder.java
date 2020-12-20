package moe.hareru.proseq;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import org.ahocorasick.trie.PayloadEmit;
import org.ahocorasick.trie.PayloadTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Encoder {
    String Encode(String seq);

    String[] Encode(String[] seq);

    Feature[][] fEncode(String[] seq);
}

class OneHot_Encoder implements Encoder {
    private static final Logger logger = LoggerFactory.getLogger(OneHot_Encoder.class);
    private static final String default_a_acid = "ACDEFGHIKLMNPQRSTVWY";
    private int[] acc_map = null;
    private int vecDim_count = 0;
    private WordBag_Encoder helper = null;
    private boolean multi_mode = false;

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

    public OneHot_Encoder(String[] seq_list) {
        logger.debug("Multi mode");
        helper = new WordBag_Encoder(seq_list);
        multi_mode = true;
    }

    public boolean[] bEncode(String seq) {
        boolean[] result = new boolean[vecDim_count];
        for (char ch : seq.toCharArray()) {
            if (acc_map[ch] != 0) {
                result[acc_map[ch] - 1] = true;
            }
        }
        return result;
    }

    public String Encode(String seq) {
        if (multi_mode) return helper.Encode(seq, 1);
        boolean[] result = bEncode(seq);
        StringBuilder tmp = new StringBuilder();
        for (boolean b : result) {
            tmp.append(b ? "1" : "0");
        }
        return tmp.toString();
    }

    public String[] Encode(String[] seq) {
        if (multi_mode) return helper.Encode(seq, 1);
        String[] ans = new String[seq.length];
        for (int i = 0; i < seq.length; ++i) ans[i] = Encode(seq[i]);
        return ans;
    }

    public Feature[][] fEncode(String[] seq) {
        if (multi_mode) return helper.fEncode(seq, true);
        List<Feature[]> ans = new ArrayList<>();
        for (String s : seq) {
            List<Feature> x = new ArrayList<>();
            boolean[] result = bEncode(s);
            for (int i = 1; i <= result.length; ++i) x.add(new FeatureNode(i, result[i - 1] ? 1 : 0));
            ans.add((Feature[]) x.toArray());
        }
        return ans.toArray(new Feature[0][]);
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

    public int[] iEncode(String seq) {
        int[] result = new int[this.WB_dic.length];
        Collection<PayloadEmit<Integer>> emits = AC_Automaton.parseText(seq);
        for (PayloadEmit<Integer> emit : emits) ++result[emit.getPayload()];
        return result;
    }

    public String Encode(String seq) {
        return Encode(seq, 9);
    }

    public String Encode(String seq, int Count_lim) {
        int[] result = iEncode(seq);
        StringBuilder tmp = new StringBuilder();
        for (Integer b : result) {
            b = Integer.min(b, Count_lim);
            tmp.append(b.toString());
        }
        return tmp.toString();
    }

    public String[] Encode(String[] seq) {
        return Encode(seq, 9);
    }

    public String[] Encode(String[] seq, int Count_lim) {
        String[] ans = new String[seq.length];
        for (int i = 0; i < seq.length; ++i) ans[i] = Encode(seq[i], Count_lim);
        return ans;
    }

    public Feature[][] fEncode(String[] seq) {
        return fEncode(seq, false);
    }

    public Feature[][] fEncode(String[] seq, boolean bin_flag) {
        List<Feature[]> ans = new ArrayList<>();
        for (String s : seq) {
            List<Feature> x = new ArrayList<>();
            int[] result = iEncode(s);
            for (int i = 1; i <= result.length; ++i) x.add(new FeatureNode(i, result[i - 1]));
            ans.add(x.toArray(new Feature[0]));
        }
        return ans.toArray(new Feature[0][]);
    }
}
