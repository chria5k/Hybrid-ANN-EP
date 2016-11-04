/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ersa
 */
public class TubesSC {

    /**
     * @param args the command line arguments
     */
    private static final int[] varInput = {2};
    private static final int[] varHidden = {6};
    private static final int[] varPop = {1000};
    private static final int[] varGen = {50};
    final public static int times = 1;

    public static int input;
    public static int neuron;
    public static int nhidden;
    public static int noutput;
    public static int total;
    public static int batas;
    public static int numOfPop;
    public static int numOfGen;
    final public static int bias = 1;
    final public static double alpha = 0.2;
    final public static int selectivePressure = 10;
    static Dataset ds = null;

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            int x = 0;
            XSSFWorkbook output = new XSSFWorkbook();
            XSSFSheet sheetOutput = output.createSheet("new sheet");
            FileOutputStream fileOut = new FileOutputStream("F:\\JST\\dataset\\goldPrice\\HasilFINAL.xlsx");
            ds = new Dataset();
            ds.calculateFluctuation("F:\\JST\\dataset\\goldPrice\\training.xlsx", "F:\\JST\\dataset\\goldPrice\\trainingFluctuation.xlsx");
            ds.normalization("F:\\JST\\dataset\\goldPrice\\trainingFluctuation.xlsx", "F:\\JST\\dataset\\goldPrice\\trainingNormalization.xlsx", 0.1, 0.9);
            ds.calculateFluctuation("F:\\JST\\dataset\\goldPrice\\testing.xlsx", "F:\\JST\\dataset\\goldPrice\\testingFluctuation.xlsx");
            ds.normalization("F:\\JST\\dataset\\goldPrice\\testingFluctuation.xlsx", "F:\\JST\\dataset\\goldPrice\\testingNormalization.xlsx", 0.1, 0.9);
            int totalCombination = varInput.length * varHidden.length * varPop.length * times;
            int progress = 0;
            for (int i = 0; i < varInput.length; i++) {
                input = varInput[i];
                ds.clearDatasetTraining();
                ds.clearDatasetTesting();
                ds.addDataSetTrainingExcel("F:\\JST\\dataset\\goldPrice\\trainingNormalization.xlsx", TubesSC.input);
                ds.addDataSetTestingExcel("F:\\JST\\dataset\\goldPrice\\testingNormalization.xlsx", TubesSC.input);
                for (int j = 0; j < varHidden.length; j++) {
                    neuron = varHidden[j];
                    nhidden = ((input + bias) * neuron);
                    noutput = (neuron + bias);
                    total = nhidden + noutput;
                    batas = total - noutput;
                    for (int k = 0; k < varPop.length; k++) {
                        numOfGen = varGen[k];
                        numOfPop = varPop[k];
                        for (int l = 0; l < times; l++) {
                            Row r = sheetOutput.createRow(progress);
                            Kromosom fittest = null;
                            double fittestBefore = 0;
                            Populasi pop = new Populasi(numOfPop, true, ds);
                            for (int y = 0; y < numOfGen; y++) {
                                pop = EP.Evolution(pop);
                                fittest = pop.getFittest();
                                System.out.println(fittest.getFitness());
                            }
                            JST jst = new JST(fittest.getW1(), fittest.getW2(), fittest.getB1(), fittest.getB2(), ds);
                            double MAPE = jst.getMape(true);
                            System.out.println(MAPE);
                            //System.out.println("final MAPE = " + MAPE);
                            for (int z = 0; z < 6; z++) {
                                Cell c = r.createCell(z);
                                switch (z) {
                                    case 0:
                                        c.setCellValue(input);
                                        break;
                                    case 1:
                                        c.setCellValue(neuron);
                                        break;
                                    case 2:
                                        c.setCellValue(numOfGen);
                                        break;
                                    case 3:
                                        c.setCellValue(numOfPop);
                                        break;
                                    case 4:
                                        c.setCellValue(fittest.getFitness());
                                        break;
                                    case 5:
                                        c.setCellValue(MAPE);
                                        break;

                                }
                            }
                            x++;
                            progress++;
                            System.out.println("completed: " + progress + "/" + totalCombination);
                        }
                    }
                }
            }
            output.write(fileOut);
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TubesSC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
