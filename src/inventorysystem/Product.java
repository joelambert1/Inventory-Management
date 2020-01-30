/* Joe Lambert -- Product.java
 */
package inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private ObservableList<Part> associatedParts;
    int id, stock, min, max; // inventory level = stock
    String name, displayPrice;
    double price;
    

    public Product () {
        associatedParts = FXCollections.observableArrayList();
    }
    public Product(int id, int stock, int min, int max, String name, double price) {
        associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.name = name;
        this.price = price;
    }
    public String getDisplayPrice() {
        String temp = "$";
        temp += String.valueOf(this.price);
        if (temp.charAt(temp.length()-1) == '.' || temp.charAt(temp.length()-2) == '.') // if one of last 2 digits are decimal, append 0
            temp += '0';
        return temp;
    }
    //         add part to the list
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    //         delete part return success status
    public boolean deleteAssociatedPart(Part selectedAspart) {
        return associatedParts.remove(selectedAspart);
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}