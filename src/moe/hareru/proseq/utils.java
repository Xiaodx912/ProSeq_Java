package moe.hareru.proseq;

import java.util.ArrayList;
import java.util.Arrays;

public class utils {
    private static final String dAA = "ACDEFGHIKLMNPQRSTVWY";

    public static String[] WB_builder(int len) {
        return WB_builder(len, dAA);
    }

    public static String[] WB_builder(int len, String AA) {
        if (len < 1) return new String[]{""};
        String[] base = WB_builder(len - 1, AA);
        ArrayList<String> ans = new ArrayList<>();
        for (char c : AA.toCharArray()) {
            String[] tmp = base.clone();
            for (int i = 0; i < tmp.length; ++i) {
                tmp[i] = tmp[i] + c;
            }
            ans.addAll(Arrays.asList(tmp));
        }
        return ans.toArray(new String[0]);
    }

}

