/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessc;

import java.util.ArrayList;

/**
 *
 * @author Ersa
 */
public class JST {

    final private boolean bias = true;
    private int JINeuron;
    private int JHNeuron;
    private int JONeuron;
    private double[][] w1;
    private double[] w2;
    private double[] b1;
    private double b2;
    private Dataset DS = null;

    public JST(double[][] w1, double[] w2, double[] b1, double b2, Dataset DS) {
        this.w1 = w1;
        this.w2 = w2;
        this.b1 = b1;
        this.b2 = b2;
        this.JINeuron = w1.length;
        this.JHNeuron = w1[0].length;
        this.JONeuron = 1;
        this.DS = DS;
    }

    public double getMSE() {
        double ErrorTotal = 0;
        for (int i = 0; i < DS.getDatasetLenghtTraining(); i++) {
            GoldPrice gp = DS.getDataTraining(i);
            double input[] = gp.getPrice();
            double target = gp.getTarget();
            double u1[] = new double[JHNeuron];
            double z[] = new double[JHNeuron];
            for (int k = 0; k < JHNeuron; k++) {
                for (int l = 0; l < JINeuron; l++) {
                    u1[k] += (input[l] * w1[l][k]);
                }
                if (bias) {
                    u1[k] += b1[k];
                }
                z[k] = 1 / (1 + Math.exp(-u1[k]));
            }
            double u2 = 0;
            double y = 0;
            for (int l = 0; l < JHNeuron; l++) {
                u2 += (z[l] * w2[l]);
            }
            if (bias) {
                u2 += b2;
            }
            y = 1 / (1 + Math.exp(-u2));
            double error = 100;
            error = Math.pow(target - y, 2);
            ErrorTotal += error;
        }
        double MSE = ErrorTotal / DS.getDatasetLenghtTraining() * 100;
//        System.out.println("MAPE : "+MAPE);
        return MSE;
    }

    public double getMape(boolean test) {
        double ErrorTotal = 0;
        for (int i = 0; i < DS.getDatasetLenghtTesting(); i++) {
            GoldPrice gp = DS.getDataTesting(i);
            double input[] = gp.getPrice();
            double target = gp.getTarget();
            double u1[] = new double[JHNeuron];
            double z[] = new double[JHNeuron];
            for (int k = 0; k < JHNeuron; k++) {
                for (int l = 0; l < JINeuron; l++) {
                    u1[k] += (input[l] * w1[l][k]);
                }
                if (bias) {
                    u1[k] += b1[k];
                }
                z[k] = 1 / (1 + Math.exp(-u1[k]));
            }
            double u2 = 0;
            double y = 0;
            for (int l = 0; l < JHNeuron; l++) {
                u2 += (z[l] * w2[l]);
            }
            if (bias) {
                u2 += b2;
            }
            y = 1 / (1 + Math.exp(-u2));

//            if (test) {
//                System.out.println("OUTPUT: " + y + " TARGET:" + target);
//            }
            double error = 100;
            error = (Math.abs(target - y))/(Math.abs(target));
            ErrorTotal += error;
       //     System.out.println(ErrorTotal);
        }
        double MAPE = ErrorTotal / DS.getDatasetLenghtTesting() * 100;
//        System.out.println("MAPE : "+MAPE);
        return MAPE;
    }
}
