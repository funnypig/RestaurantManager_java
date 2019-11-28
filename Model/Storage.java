package Model;

import java.sql.*;
import java.util.*;

class InsufficientAmount extends Exception{
    public InsufficientAmount(String msg){
        super(msg);
    }
}

public class Storage {
    public static final String[] Fields = new String[]{
            "id", "name", "amountUnit", "amount", "cost", "icon"
    };
    private List<String> products;

    public Connection conn;

    public void Connect() throws ClassNotFoundException, SQLException {
        conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:storage.db");
            createStorageTable();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void createStorageTable(){
        try(Statement statement = conn.createStatement()){
            statement.execute("CREATE TABLE IF NOT EXISTS Storage ("+
                    "id integer,"+
                    "name text,"+
                    "amountUnit text,"+
                    "amount real,"+
                    "cost real,"+
                    "icon text);");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Storage(){
        try{
            Connect();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        products = new ArrayList<String>();
        updateProductsList();
    }

    public void updateProductsList(){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT name FROM Storage");

            while(rs.next()){
                String name = rs.getString("name");
                if (! products.contains(name))
                    products.add(name);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<String> getProductList(){
        return products;
    }

    public int getProductCount(){
        return products.size();
    }

    public String getProductAt(int i){
        return products.get(i);
    }

    public String[] getFields(){
        return Fields;
    }

    public boolean isProduct(String name){
        return products.contains(name);
    }

    public Product getProduct(String name){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT * from Storage WHERE name='%s'",
                    name
            ));
            return new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("amountUnit"),
                    rs.getDouble("amount"),
                    rs.getDouble("cost"),
                    rs.getString("icon"));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Product getProduct(Integer id){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT * from Storage WHERE name=%d",
                    id
            ));
            return new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("amountUnit"),
                    rs.getDouble("amount"),
                    rs.getDouble("cost"),
                    rs.getString("icon"));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Product addProduct(String name, String amountUnit, Double amount, Double cost, String icon){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT MAX(id) FROM Storage;");
            int id = rs.getInt("MAX(id)")+1;

            Product p = new Product(id, name, amountUnit, amount, cost, icon);

            statement.execute(String.format(
                    "INSERT INTO Storage (id, name, amountUnit, amount, cost, icon)"+
                            "VALUES (%d, '%s', '%s', %f, %f, '%s');",
                    id, name, amountUnit, amount, cost, icon
            ));

            if (!products.contains(name))
                products.add(name);

            return p;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public Object getProductField(String name, String field){
        try(Statement statement = conn.createStatement()){
            return statement.executeQuery(String.format(
                    "SELECT %s FROM Storage WHERE name='%s'",
                    field, name
            )).getObject(field);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return "";
    }

    public void add(String name, Double quantity){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT id, amount from Storage WHERE name='%s'",
                    name
            ));
            int id = rs.getInt("id");
            double curAmount = rs.getDouble("amount");
            statement.execute(String.format(
                    "UPDATE Storage SET amount = %f WHERE id = %d",
                    curAmount+quantity, id
            ));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void add(Integer id, Double quantity){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT amount from Storage WHERE id=%d",
                    id
            ));
            double curAmount = rs.getDouble("amount");
            statement.execute(String.format(
                    "UPDATE Storage SET amount = %f WHERE id = %d",
                    curAmount+quantity, id
            ));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public double getAmount(String name){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT amount from Storage WHERE name='%s'",
                    name
            ));

            return rs.getDouble("amount");
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public double getAmount(Integer id){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT amount from Storage WHERE id=%d",
                    id
            ));

            return rs.getDouble("amount");
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public void get(String name, Double quantity) throws Exception {
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT id, amount from Storage WHERE name='%s'",
                    name
            ));
            int id = rs.getInt("id");
            double curAmount = rs.getDouble("amount");
            if (curAmount < quantity)
                throw new InsufficientAmount(String.format("get product(name=%s): No required amount", name));
            statement.execute(String.format(
                    "UPDATE Storage SET amount = %f WHERE id = %d",
                    curAmount-quantity, id
            ));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void get(Integer id, Double quantity) throws Exception {
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(String.format(
                    "SELECT amount from Storage WHERE id=%d",
                    id
            ));
            double curAmount = rs.getDouble("amount");
            if (curAmount < quantity)
                throw new InsufficientAmount(String.format("get product(id=%d): No required amount", id));
            statement.execute(String.format(
                    "UPDATE Storage SET amount = %f WHERE id = %d",
                    curAmount-quantity, id
            ));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void showAll(){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT * FROM Storage");

            while (rs.next()){
                System.out.println(String.format("{%d}\t{%s}\t{%s}\t{%f}\t{%f}\t{%s}\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getShort("amountUnit"),
                        rs.getDouble("amount"),
                        rs.getDouble("cost"),
                        rs.getString("icon")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
