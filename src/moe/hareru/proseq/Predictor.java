package moe.hareru.proseq;

import de.bwaldvogel.liblinear.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Predictor {
    private static final Logger logger = LoggerFactory.getLogger(Predictor.class);
    private Problem prob;
    Model model;
    private Feature[][] dataset = null;

    public Predictor(int l, int n) {
        prob = new Problem();
        prob.l = l;
        prob.n = n;
    }

    public void addData(String[] pos_seqs, String[] neg_seqs, Encoder enc) {
        Feature[][] pos = enc.fEncode(pos_seqs);
        Feature[][] neg = enc.fEncode(neg_seqs);
        double[] y = new double[pos.length + neg.length];
        Arrays.fill(y, 0, pos.length, 1);
        Feature[][] x = new Feature[pos.length + neg.length][];
        System.arraycopy(pos, 0, x, 0, pos.length);
        System.arraycopy(neg, 0, x, pos.length, neg.length);
        logger.debug(x.length + " instances");
        prob.x = x;
        prob.y = y;
    }

    public void train() {
        Parameter p = new Parameter(SolverType.L2R_LR, 0.9, 0.01);
        logger.debug(prob.x.length + " instances");
        logger.debug("train start");
        model = Linear.train(prob, p);
        try {
            model.save(Paths.get("model"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("model ready");
    }

    double predict(Feature[] instance) {
        return Linear.predict(model, instance);
    }

    Double[] predict(Feature[][] instances) {
        ArrayList<Double> l = new ArrayList<>();
        for (Feature[] i : instances) l.add(predict(i));
        return l.toArray(new Double[0]);
    }
}
