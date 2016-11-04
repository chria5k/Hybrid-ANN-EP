/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ersa
 */
public class Dataset {

    private ArrayList<GoldPrice> dataSetTesting = new ArrayList<>();
    private ArrayList<GoldPrice> dataSetTraining = new ArrayList<>();

    private double max;
    private double min;
    private double minValue;
    private double maxValue;

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public GoldPrice getDataTesting(int index) {
        return dataSetTesting.get(index);
    }

    public GoldPrice getDataTraining(int index) {
        return dataSetTraining.get(index);
    }

    public Dataset() {
    }

    public int getDatasetLenghtTraining() {
        return dataSetTraining.size();
    }

    public int getDatasetLenghtTesting() {
        return dataSetTesting.size();
    }

    public void addDataSetTrainingExcel(String InputFile, int numOfInput) throws IOException {
        FileInputStream file = new FileInputStream(new File(InputFile));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        for (int i = rowStart; i <= rowEnd; i++) {
            double price[] = new double[numOfInput];
            double target = 0;
            if ((i + numOfInput) <= rowEnd) {
                for (int j = 0; j <= numOfInput; j++) {
                    Row row = sheet.getRow(i + j);
                    if (j != numOfInput) {
                        Cell cell = row.getCell(0);
                        price[j] = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
                    } else {
                        Cell cell = row.getCell(0);
                        target = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
                    }
                }
                GoldPrice gp = new GoldPrice(price, target);
                dataSetTraining.add(gp);
            }
        }
        file.close();

    }

    public void addDataSetTestingExcel(String InputFile, int numOfInput) throws IOException {
        FileInputStream file = new FileInputStream(new File(InputFile));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        for (int i = rowStart; i < rowEnd; i++) {
            double price[] = new double[numOfInput];
            double target = 0;
            if ((i + numOfInput) <= rowEnd) {
                for (int j = 0; j <= numOfInput; j++) {
                    Row row = sheet.getRow(i + j);
                    if (j != numOfInput) {
                        Cell cell = row.getCell(0);
                        price[j] = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
                    } else {
                        Cell cell = row.getCell(0);
                        target = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
                    }
                }
                GoldPrice gp = new GoldPrice(price, target);
                dataSetTesting.add(gp);
            }
        }
        file.close();

    }

    public void calculateFluctuation(String InputFile, String OutputFile) throws FileNotFoundException, IOException {
        FileInputStream file = new FileInputStream(new File(InputFile));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFWorkbook output = new XSSFWorkbook();
        XSSFSheet sheetOutput = output.createSheet("new sheet");
        FileOutputStream fileOut = new FileOutputStream(OutputFile);
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        for (int i = rowStart; i <= rowEnd - 1; i++) {
            Row rowIn1 = sheet.getRow(i);
            Cell cellIn1 = rowIn1.getCell(0);
            Row rowIn2 = sheet.getRow(i + 1);
            Cell cellIn2 = rowIn2.getCell(0);
            double value1 = Double.parseDouble(String.valueOf(cellIn1.getNumericCellValue()));
            double value2 = Double.parseDouble(String.valueOf(cellIn2.getNumericCellValue()));
            Row rowOut = sheetOutput.createRow(i);
            Cell cellOut = rowOut.createCell(0);
            cellOut.setCellValue(value2 - value1);
        }
        output.write(fileOut);
        fileOut.close();
    }

    public void normalization(String InputFile, String outputFile, double minValue, double maxValue) throws FileNotFoundException, IOException {
        this.minValue = minValue;
        this.maxValue = maxValue;
        FileInputStream file = new FileInputStream(new File(InputFile));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFWorkbook output = new XSSFWorkbook();
        XSSFSheet sheetOutput = output.createSheet("new sheet");
        FileOutputStream fileOut = new FileOutputStream(outputFile);
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        Row row = sheet.getRow(rowStart);
        Cell cell = row.getCell(0);
        max = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
        min = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
        for (int i = rowStart + 1; i <= rowEnd; i++) {
            row = sheet.getRow(i);
            cell = row.getCell(0);
            double value = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
        }
        for (int i = rowStart; i <= rowEnd; i++) {
            row = sheet.getRow(i);
            cell = row.getCell(0);
            double value = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
            double newValue = minValue + ((value - min) * (maxValue - minValue) / (max - min));
            Row rowOut = sheetOutput.createRow(i);
            Cell cellOut = rowOut.createCell(0);
            cellOut.setCellValue(newValue);
        }
        output.write(fileOut);
        fileOut.close();
    }

    public void clearDatasetTraining() {
        this.dataSetTraining = new ArrayList<>();
    }

    public void clearDatasetTesting() {
        this.dataSetTesting = new ArrayList<>();
    }

    public double denormalizationValue(double input) {
        double newValue = min + ((input - minValue) * (max - min) / (maxValue - minValue));
        return newValue;
    }
}
