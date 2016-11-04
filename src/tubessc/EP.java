/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessc;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author fauhan
 */
public class EP {

    public static Kromosom Mutation(Kromosom kromosom) {
        Random r = new Random();
        double a = r.nextGaussian();
        double[] newMutationStep = new double[kromosom.getVariableLenght()];
        for (int i = 0; i < kromosom.getVariableLenght(); i++) {
            newMutationStep[i] = kromosom.getMutationStep(i) * (1 + (TubesSC.alpha * a));
//            double b = r.nextGaussian();
//            newMutationStep[i] = kromosom.getMutationStep(i) * Math.exp((a*1/Math.sqrt(2*kromosom.getVariableLenght())) + (1/Math.sqrt(2*Math.sqrt(kromosom.getVariableLenght())) * b));
        }
        double[] newVariable = new double[kromosom.getVariableLenght()];
        for (int i = 0; i < kromosom.getVariableLenght(); i++) {
            double b = r.nextGaussian();
            newVariable[i] = kromosom.getVariable(i) + newMutationStep[i] * b;
        }
        Kromosom newKromosom = new Kromosom(newVariable, newMutationStep, kromosom.getDataset());
        return newKromosom;
    }

    public static Populasi SurvivorSelection(Populasi pop) {
        pop.clearWin();
        for (int i = 0; i < pop.getNumbOfPop(); i++) {
            Kromosom y = pop.getKromosom(i);
            Random r = new Random();
            Set<Integer> generated = new LinkedHashSet<Integer>();
            while (generated.size() < TubesSC.selectivePressure) {
                Integer next = r.nextInt(pop.getNumbOfPop());
                if (next != i) {
                    generated.add(next);
                }
            }
            Iterator<Integer> iterator = generated.iterator();
            for (int j = 0; j < TubesSC.selectivePressure; j++) {
                Kromosom x = pop.getKromosom(iterator.next());
                if (y.getFitness() > x.getFitness()) {
                    y.setWin(y.getWin() + 1);
                }
            }
        }
        pop.sortByWin();
        Populasi newPopulation = new Populasi(pop.getNumbOfPop() / 2, false, TubesSC.ds);
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++) {
            newPopulation.setKromosom(i, pop.getKromosom(i));
        }
        return newPopulation;
    }

    public static Populasi Evolution(Populasi pop) {
        Populasi newPopulation = new Populasi(pop.getNumbOfPop() * 2, false, TubesSC.ds);
        int j = 0;
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++) {
            Kromosom x;
            if (i < pop.getNumbOfPop()) {
                x = pop.getKromosom(i);
                newPopulation.setKromosom(i, x);
            } else {
                x = Mutation(pop.getKromosom(j));
                newPopulation.setKromosom(i, x);
                j++;
            }
        }
        newPopulation = SurvivorSelection(newPopulation);
        return newPopulation;
    }
}
