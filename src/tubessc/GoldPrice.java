/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tubessc;

/**
 *
 * @author Ersa
 */
public class GoldPrice {
    
    private double[] price;
    private double target;

    
    public double[] getPrice() {
        return price;
    }

    public void setPrice(double[] price) {
        this.price = price;
    }
    
    public double getTarget() {
        return target;
    }

    public void setTarget(double error) {
        this.target = target;
    }
    

    public GoldPrice(double[] price, double target) {
        this.price = price;
        this.target = target;
    }

}
