/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessc;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author fauhan
 */
public class Populasi {

    public Kromosom[] populasi;
    
    public Populasi(int populationSize, boolean create, Dataset ds) {
        populasi = new Kromosom[populationSize];
        if (create) {
            for (int i = 0; i < populationSize; i++) {
                this.populasi[i] = new Kromosom(ds);
            }
        }
    }

    public int getNumbOfPop() {
        return populasi.length;
    }

    public Kromosom getKromosom(int index) {
        return populasi[index];
    }

    public Kromosom getFittest() {
        Kromosom fittest = populasi[0];
        for (int i = 1; i < this.populasi.length; i++) {
            if (populasi[i].getFitness() > fittest.getFitness()) {
                fittest = this.populasi[i];
            }
        }
        return fittest;
    }

    public void setKromosom(int index, Kromosom k) {
        this.populasi[index] = k;
    }

    public void clearWin() {
        for (int i = 0; i < this.populasi.length; i++) {
            populasi[i].setWin(0);
        }
    }

    public void sortByFitness() {
        Arrays.sort(populasi, new Comparator<Kromosom>() {
            @Override
            public int compare(Kromosom o1, Kromosom o2) {
                if (o1.getFitness() == o2.getFitness()) {
                    return 0;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public void sortByWin() {
        Arrays.sort(populasi, new Comparator<Kromosom>() {
            @Override
            public int compare(Kromosom o1, Kromosom o2) {
                if (o1.getWin() == o2.getWin()) {
                    return 0;
                } else if (o1.getWin() < o2.getWin()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }
}
