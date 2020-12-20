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

    private static int mode;//-1:err cmd 0:help 1:enc from f 2:interactive enc 3:predict

    public static void main(String[] args) {

        //for(String s:args)logger.debug(s);
        //demo();

        if (args.length == 0 || args[0].equals("-h")) mode = 0;
        else if (args[0].equals("-WB") || args[0].equals("-OH")) mode = args.length == 1 ? 2 : 1;
        else if (args[0].equals("-P")) mode = 3;
        else mode = -1;


        if (mode == -1) {
            System.out.println("command error");
            mode = 0;
        }
        if (mode == 0) {
            print_help();
            return;
        }
        if (mode == 1) {
            assert args[1].equals("-i") && args[3].equals("-o") && args.length == 5;
            DataMgr input = new DataMgr(Paths.get(args[2]));
            Encoder enc = args[0].equals("-OH") ? new OneHot_Encoder() : new WordBag_Encoder(WB_builder(2));
            input.encAll(enc, args[4]);
            return;
        }
        if (mode == 2) {
            Encoder enc = args[0].equals("-OH") ? new OneHot_Encoder() : new WordBag_Encoder(WB_builder(2));
            System.out.println("interactive enc\nplease input sequence, type q to exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            while (true) {
                try {
                    str = br.readLine();
                } catch (IOException e) {
                    logger.error("IO error");
                    e.printStackTrace();
                }
                if (str.equals("q")) return;
                System.out.println(enc.Encode(str));
            }
        }
        if (mode == 3) {
            DataMgr bench_p = new DataMgr(Paths.get(args[1]));
            DataMgr bench_n = new DataMgr(Paths.get(args[2]));
            DataMgr test = new DataMgr(Paths.get(args[3]));
            int exp = args[4].equals("1") ? 1 : 0;
            Predictor p = new Predictor(bench_p.size() + bench_n.size(), 8000);
            Encoder enc = new WordBag_Encoder(WB_builder(3));
            p.addData(bench_p.getAll(), bench_n.getAll(), enc);
            p.train();
            Double[] ans = p.predict(enc.fEncode(test.getAll()));
            logger.debug(Arrays.toString(ans));
            int ACC = 0;
            for (Double c : ans) if (c == exp) ACC++;
            Double r = (double) ACC / test.size();
            logger.info(String.valueOf(r));
        }
    }

    public static void demo() {
        DataMgr d = new DataMgr(Paths.get("./data/testData"));
        logger.debug(Arrays.toString(d.getAll()));
        DataMgr bench_p = new DataMgr(Paths.get("./data/pos"));
        DataMgr bench_n = new DataMgr(Paths.get("./data/neg"));
        DataMgr test = new DataMgr(Paths.get("./data/test_neg.txt"));
        logger.debug(Arrays.toString(bench_p.getAll()));
        Predictor p = new Predictor(bench_p.size() + bench_n.size(), 8000);
        Encoder enc = new WordBag_Encoder(WB_builder(3));
        p.addData(bench_p.getAll(), bench_n.getAll(), enc);
        p.train();
        Double[] ans = p.predict(enc.fEncode(test.getAll()));
        int c_count = 0;
        int exp = 0;
        for (Double c : ans) if (c == exp) c_count++;
        Double r = (double) c_count / test.size();
        logger.info(String.valueOf(r));
    }

    public static void print_help() {
        String help = """
                *.jar [-h|-WB|-OH|-P] [-i input -o output]
                -h help
                -WB Word Bag Encoder
                -OH One Hot Encoder
                -i input path
                -o output path
                -P predict mode
                """;
        System.out.println(help);
    }
}
