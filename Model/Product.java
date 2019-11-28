package Model;

public class Product {
    Integer id;
    String name;
    AmountUnit amountUnit;
    Double amount;
    Double cost;
    String iconPath;

    public Product(Integer id, String name, AmountUnit au, double amount, double cost){
        this.id = id;
        this.name = name;
        this.amountUnit = au;
        this.amount = amount;
        this.cost = cost;
        this.iconPath = "";
    }

    public Product(Integer id, String name, AmountUnit au, double amount, double cost, String iconPath){
        this.id = id;
        this.name = name;
        this.amountUnit = au;
        this.amount = amount;
        this.cost = cost;
        this.iconPath = iconPath;
    }

    public Product(Integer id, String name, String au, double amount, double cost, String iconPath){
        this.id = id;
        this.name = name;
        this.amountUnit = defineUnit(au);
        this.amount = amount;
        this.cost = cost;
        this.iconPath = iconPath;
    }

    public static AmountUnit defineUnit(String s){
        switch (s){
            case "kg" : return AmountUnit.kg;
            case "g" : return AmountUnit.g;
            case "mg" : return AmountUnit.mg;
            case "l" : return AmountUnit.l;
            case "ml" : return AmountUnit.ml;
            default: return AmountUnit.piece;
        }
    }

    public boolean equals(String name){
        return this.name.equalsIgnoreCase(name);
    }

    public boolean equals(Product p){
        return id.equals(p.id);
    }
}
