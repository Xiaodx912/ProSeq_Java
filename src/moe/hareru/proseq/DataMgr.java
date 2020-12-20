package moe.hareru.proseq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class DataMgr {
    private static final Logger logger = LoggerFactory.getLogger(DataMgr.class);
    private static final Pattern Protein_re = Pattern.compile(">(\\w+)[\\n\\r]+(\\w+)");
    private Map<String, String> seqDic;

    public DataMgr(Path data_path) {
        logger.debug("DMgr init with file:" + data_path);
        String data;
        try {
            data = new String(Files.readAllBytes(data_path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        logger.debug("File OK.");

        this.seqDic = new HashMap<>();
        Scanner Fasta_scanner = new Scanner(data).useDelimiter("(?=>\\w+[\\n\\r]+\\w+)");
        while (Fasta_scanner.hasNext()) {
            Matcher m = Protein_re.matcher(Fasta_scanner.next());
            if (m.find()) seqDic.put(m.group(1).trim(), m.group(2).trim());
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

    public void encAll(Encoder enc,String output){
        OutputStream out;
        try {
            out=new FileOutputStream(output);
        } catch (FileNotFoundException e) {
            logger.error("output path illegal");
            e.printStackTrace();
            return;
        }
        OutputStreamWriter ow=new OutputStreamWriter(out);
        for (Map.Entry<String, String> entry : seqDic.entrySet()) {
            try {
                ow.write(">" + entry.getKey() + "\n" + enc.Encode(entry.getKey())+"\n");
            } catch (IOException e) {
                logger.error("write error");
                e.printStackTrace();
            }
        }
    }
}
