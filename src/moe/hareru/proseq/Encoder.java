package moe.hareru.proseq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encoder {
}
class OneHot_Encoder extends Encoder{
    private static final Logger logger=LoggerFactory.getLogger(OneHot_Encoder.class);
    private static final String a_acid="ACDEFGHIKLMNPQRSTVWY";
    private final int[] acc_map;
    public OneHot_Encoder(){
        acc_map=new int[256];
        for(int i=0;i<a_acid.length();i++) acc_map[a_acid.charAt(i)] = i+1;
        logger.debug("OH_enc init with:"+a_acid);
    }
    public String[] Encode(String[] seq){
        String[] ans=new String[seq.length];
        for(int i = 0; i<seq.length; ++i){
            boolean[] result = new boolean[a_acid.length()];
            for(char ch:seq[i].toCharArray()){
                if (acc_map[ch]!=0) {
                    result[acc_map[ch]-1]=true;
                }
            }
            StringBuilder tmp = new StringBuilder();
            for(boolean b:result){
                tmp.append(b ? "1" : "0");
            }
            ans[i]=tmp.toString();
        }
        return ans;
    }
}
