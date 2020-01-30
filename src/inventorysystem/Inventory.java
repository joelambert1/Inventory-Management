/* Joe Lambert Inventory.java
 */
package inventorysystem;

import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;
    private ObservableList<Part> internalPartHistory;
    private ObservableList<Product> internalProductHistory;
    
public Inventory() {
    allParts = FXCollections.observableArrayList();
    allProducts = FXCollections.observableArrayList();
    internalPartHistory = FXCollections.observableArrayList();
    internalProductHistory = FXCollections.observableArrayList();
}

public ObservableList<Part> partSearch (String text) { // probably could use template
 
    ObservableList<Part> newParts = FXCollections.observableArrayList();
    
    // return old list or keep same list if empty text 
    if (text.isEmpty() || text.charAt(0) == ' ' && text.length() == 1) { 
        if (!internalPartHistory.isEmpty()) {
            return internalPartHistory;
        }
        else return allParts;
    }
    // search for substring and add all matching parts to new partlist
    for (Part i : internalPartHistory) { 
        if (i.getName().contains(text))
            newParts.add(i);
    }
    return newParts;
}

public ObservableList<Product> productSearch (String text) { // probably could use template
    
    ObservableList<Product> newProducts = FXCollections.observableArrayList();
    
    // return old list or keep same list if empty text 
    if (text.isEmpty() || text.charAt(0) == ' ' && text.length() == 1) { 
        if (!internalProductHistory.isEmpty())
            return internalProductHistory;
        else 
            return allProducts;
    }
    
    // search for substring and add all matching parts to new partlist
    for (Product i : internalProductHistory) {
        if (i.getName().contains(text))
            newProducts.add(i);
    }
    return newProducts;
}

// used for add (new) product table search
public ObservableList<Part> nonAssociatedPartSearch (String text, ObservableList<Part> associatedParts) {
    ObservableList<Part> newParts = FXCollections.observableArrayList();
    
    // return old list or keep same list if empty text 
    if (text.isEmpty() || text.charAt(0) == ' ' && text.length() == 1) {
        if (!internalPartHistory.isEmpty())
            for (Part i : internalPartHistory)
                newParts.add(i);
        else
            for (Part i : allParts)
                newParts.add(i);
    }
    else // search for substring and add all matching parts to new partlist
        for (Part i : internalPartHistory) {
            if (i.getName().contains(text))
                newParts.add(i);
        }
    if (associatedParts != null && associatedParts.size() >= 1)
        for (Part i : associatedParts)
            newParts.remove(i);
    

    return newParts;
}

public void addPart(Part newPart) {
    allParts.add(newPart);
    internalPartHistory.add(newPart);
}

public void addProduct(Product newProduct) {
    allProducts.add(newProduct);
    internalProductHistory.add(newProduct);
}

// return part if found, else null
public Part lookupPart(int partId) {
    for (Part i : allParts)
        if (i.getId() == partId)
            return i;
    return null;
}

// return product if found, else null
public Product lookupProduct(int productId) {
    for (Product i: allProducts)
        if (i.getId() == productId)
            return i;
    return null;
}

// Searches for substring and returns all matching parts
public ObservableList<Part> lookupPart(String partName) {
    ObservableList<Part> temp = FXCollections.emptyObservableList();
    for (Part i : allParts)
        if (i.getName().contains(partName))
            temp.add(i);
    return null;
}

// Searches for substring and returns all matching parts
public ObservableList<Product> lookupProduct(String productName) {
    ObservableList<Product> temp = FXCollections.emptyObservableList();
    
    for (Product i : allProducts)
        if (i.getName().contains(productName))
            temp.add(i);
    return temp;
}

public void updatePart(int index, Part selectedPart) {
    Collections.replaceAll(allParts, allParts.get(index), selectedPart);
    Collections.replaceAll(internalPartHistory, internalPartHistory.get(index), selectedPart);
}

public void updateProduct(int index, Product newProduct) {
    Collections.replaceAll(allProducts, allProducts.get(index), newProduct);
    Collections.replaceAll(internalProductHistory, internalProductHistory.get(index), newProduct);
}

public boolean deletePart(Part selectedPart) {
    allParts.remove(selectedPart);
    internalPartHistory.remove(selectedPart);
    return true;
}

public boolean deletePartById(String part) {
    for (Part i: allParts)
        if (i.getName().contains(part)) {
            deletePart(i);
            return true;
        }
    return true;
}

public boolean deleteProductById(String product) {
    for (Product i: allProducts)
        if (i.getName().contains(product)) {
            deleteProduct(i);
            return true;
        }
    return true;
}

public boolean deleteProduct(Product selectedProduct) {
    allProducts.remove(selectedProduct);
    internalProductHistory.remove(selectedProduct);
    return true;
}

public ObservableList<Part> getAllParts() {
    return allParts;
}

public ObservableList<Product> getAllProducts() {
    return allProducts;
}
}
