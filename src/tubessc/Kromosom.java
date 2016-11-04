 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessc;

import java.util.Random;

/**
 *
 * @author fauhan
 */
public class Kromosom {

    // Kromosom EP input 2 bias 1 total neuron 10 = 41(X) + 41(step size)
    // Kromosom EP input 4 bias 1 total neuron 10 = 50(X) + 50(step size)
    // Kromosom EP input 6 bias 1 total neuron 10 = 70(X) + 70(step size)
//    private int x = 41;
//    private int thau = 41;
    
    private double[] variable;
    private double[] stepSize = new double[TubesSC.total];
    private double fitness;
    private int win;
    JST jst;
    Dataset ds;
    double[][] w1;
    double[] w2;
    double[] b1;
    double b2;

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public Kromosom(double[] x1, double[] y1, Dataset ds) {
        this.variable = new double[TubesSC.total];
        this.stepSize = new double[TubesSC.total];
        for (int i = 0; i < variable.length; i++) {
            this.variable[i] = x1[i];
        }
        for (int i = 0; i < stepSize.length; i++) {
            this.stepSize[i] = y1[i];
        }
        this.ds = ds;
        setFitness();
        this.win = 0;
    }

    public Kromosom(Dataset ds) {
        this.variable = new double[TubesSC.total];
        for (int i = 0; i < variable.length; i++) {
            this.variable[i] = randomizeWeight();
        }
        this.ds = ds;
        setFitness();
        this.win = 0;
    }

    public double[][] getW1() {
        return w1;
    }

    public void setW1(double[][] w1) {
        this.w1 = w1;
    }

    public double[] getW2() {
        return w2;
    }

    public void setW2(double[] w2) {
        this.w2 = w2;
    }

    public double[] getB1() {
        return b1;
    }

    public void setB1(double[] b1) {
        this.b1 = b1;
    }

    public double getB2() {
        return b2;
    }

    public void setB2(double b2) {
        this.b2 = b2;
    }

    public void setDataset(Dataset ds) {
        this.ds = ds;
    }

    public Dataset getDataset() {
        return this.ds;
    }

    private double randomizeWeight() {
        Random r = new Random();
        return ((r.nextDouble() * 50) - 100);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness() {
        getW(variable);
        jst = new JST(getW1(), getW2(), getB1(), getB2(), ds);
        this.fitness = 1 / (jst.getMSE());
    }

    public void getW(double[] kromosom) {
        w1 = new double[TubesSC.input][TubesSC.neuron];
        w2 = new double[TubesSC.neuron];
        b1 = new double[TubesSC.neuron];
        b2 = 0;
        int a = 0, b = 0, c = 0, d = 0, e = 0;
        for (int i = 0; i < TubesSC.total; i++) {
            if (i < TubesSC.total - TubesSC.bias) {
                if (i < TubesSC.batas) {
                    if (a < TubesSC.input) {
//                    if (b < neuron) {
//                        w1[c][b] = kromosom[i];
//                        b = b + 1;
//                    } else if (b >= neuron) {
//                        c = c + 1;
//                        b = 0;
//                        w1[c][b] = kromosom[i];
//                    }                         
//                    if (b >= neuron) {
//                        c = c + 1;
//                        b = 0;
//                    }
//                    w1[c][b] = kromosom[i];
//                    b = b + 1;
//                    a = a + 1;
                        if (c > TubesSC.input - 1) {
                            b = b + 1;
                            c = 0;
                        }
                        w1[c][b] = kromosom[i];
                        c = c + 1;
                        a = a + 1;
                    } else {
                        a = 0;
                        b1[e] = kromosom[i];
                        e = e + 1;
                    }
                } else {
                    w2[d] = kromosom[i];
                    d = d + 1;
                }
            } else {
                b2 = kromosom[i];
            }
        }
    }

    public double getVariable(int index) {
        return variable[index];
    }

    public void setVariable(double x, int index) {
        this.variable[index] = x;
    }

    public double getMutationStep(int index) {
        return variable[index];
    }

    public void setMutationStep(double x, int index) {
        this.variable[index] = x;
    }

    public int getVariableLenght() {
        return variable.length;
    }
}
