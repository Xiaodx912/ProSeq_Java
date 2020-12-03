package moe.hareru.proseq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encoder {
}
class OneHot_Encoder extends Encoder{
    private static final Logger logger=LoggerFactory.getLogger(OneHot_Encoder.class);
    private static final String a_acid="ACDEFGHIKLMNPQRSTVWY";
    private final int[] accl_map;
    public OneHot_Encoder(){
        accl_map=new int[256];
        for(int i=0;i<a_acid.length();i++) accl_map[a_acid.charAt(i)] = i+1;
    }
    public String[] Encode(String[] seqs){
        String[] ans=new String[seqs.length];
        for(int i = 0; i<seqs.length; ++i){
            boolean[] result = new boolean[a_acid.length()];
            for(char ch:seqs[i].toCharArray()){
                if (accl_map[ch]!=0) {
                    result[accl_map[ch]-1]=true;
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
