package Model;

import java.util.*;

public class Recipe {
    public List<Product> products;
    public Map<Product, Double> productAmount;

    public Recipe(){
        products = new ArrayList<Product>();
        productAmount = new HashMap<Product, Double>();
    }

    public Recipe(Iterable<Product> products, Iterable<Double> amounts){
        Iterator<Product> p = products.iterator();
        Iterator<Double> a = amounts.iterator();

        this.products = new ArrayList<Product>();
        this.productAmount = new HashMap<Product, Double>();

        while(p.hasNext() && a.hasNext()){
            Product curProd = p.next();
            this.products.add(curProd);
            this.productAmount.put(curProd, a.next());
        }
    }

    public int productCount(){
        return products.size();
    }

    public void addProduct(Product p, Double amount){
        if (!products.contains(p)){
            products.add(p);
            productAmount.put(p, amount);
        }else{
            productAmount.replace(p, amount);
        }
    }

    public double getCost(){
        double cost = 0.0;
        for (Map.Entry m : productAmount.entrySet()) {
            cost += ((Product) m.getKey()).cost * ((Double) m.getValue());
        }
        return cost;
    }

    public boolean isAvailable(){
        Storage s = new Storage();
        for (Product p : products){
            if (productAmount.get(p) > (double)s.getProductField(p.name, "amount")){
                return false;
            }
        }
        return true;
    }

    public void getRecipe() throws Exception {
        Storage s = new Storage();
        for (Product p : products){
            s.get(p.id, productAmount.get(p));
        }
    }
}
