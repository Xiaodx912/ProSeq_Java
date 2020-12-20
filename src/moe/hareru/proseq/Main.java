package moe.hareru.proseq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Arrays;

import static moe.hareru.proseq.utils.WB_builder;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static int mode;//-1:err cmd 0:help 1:enc from f 2:interactive enc 3:?
    public static void main(String[] args) {
        if(args.length==0||args[0].equals("-h")) mode=0;
        else if(args[0].equals("-WB")||args[0].equals("-OH")) mode=args.length==1?2:1;
        else mode=-1;


        if(mode==-1){System.out.println("command error");mode=0;}
        if(mode==0){print_help();return;}
        if(mode==1){
            assert args[1].equals("-i")&&args[3].equals("-o")&&args.length==5;
            DataMgr input=new DataMgr(Paths.get(args[1]));
            Encoder enc=args[0].equals("-OH")?new OneHot_Encoder(WB_builder(3)):new WordBag_Encoder(WB_builder(3));
            input.encAll(enc,args[4]);
            return;
        }
        if(mode==2){
            Encoder enc=args[0].equals("-OH")?new OneHot_Encoder(WB_builder(3)):new WordBag_Encoder(WB_builder(3));
            System.out.println("interactive enc\nplease input sequence, type q to exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            while (true){
                try {
                    str=br.readLine();
                } catch (IOException e) {
                    logger.error("IO error");
                    e.printStackTrace();
                }
                if(str.equals("q"))return;
                System.out.println(enc.Encode(str));
            }
        }
    }
    public static void demo(){
        DataMgr d = new DataMgr(Paths.get("./data/testData"));
        logger.debug(d.getSeq("1AKHA"));

        WordBag_Encoder enc = new WordBag_Encoder(new String[]{"KKE", "AKK", "BCC"});
        logger.debug(Arrays.toString(enc.Encode(new String[]{"AKKAEKKAEEBCCAKKE", "AKKE"})));

        logger.debug(Arrays.toString(WB_builder(3)));
        WordBag_Encoder full_enc = new WordBag_Encoder(WB_builder(3));
        logger.debug(Arrays.toString(full_enc.Encode(d.getAll())));
    }
    public static void print_help(){
        String help= """
                main [-v|-WB|-OH] [-i input -o output]
                -h help
                -WB Word Bag Encoder
                -OH One Hot Encoder
                """;
        System.out.println(help);
    }
}
