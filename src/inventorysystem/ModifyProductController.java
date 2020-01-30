/* Joe Lambert
 */
package inventorysystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class ModifyProductController implements Initializable {
    
    private Inventory storage;
    private Product selectedProduct, oldProduct;
    private ObservableList<Part> list = FXCollections.observableArrayList();
    private boolean checker = true;
    @FXML private TextField idField, nameField, invField, priceField, maxField, minField;
    
    @FXML private TextField partSearchField;
    @FXML private Button partSearchButton, cancelButton;
    
    @FXML private TableView<Part> topTable; // all parts
    @FXML private TableColumn<Part, Integer> partIdColumn, partInventoryLevelColumn;
    @FXML private TableColumn<Part, String> partNameColumn, partPriceColumn;
    @FXML private Button addPartButton;
    
    @FXML private TableView<Part> bottomTable; // associated parts
    @FXML private TableColumn<Part, Integer> idColumn, inventoryLevelColumn;
    @FXML private TableColumn<Part, String> nameColumn, priceColumn;
    @FXML private Button removeButton;

    
    public void transferProductData(Inventory inv, Product passedProduct) {
        storage = inv;
        oldProduct = passedProduct;
        selectedProduct = new Product();
        for (Part i: storage.getAllParts())
            list.add(i);
        for (Part j: passedProduct.getAllAssociatedParts()) {
            list.remove(j);
            selectedProduct.addAssociatedPart(j);
        }
        
        // Top Table
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("displayPrice"));
        topTable.setItems(list);
        topTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // choose multiple row functionality
        
        // Bottom Table
        bottomTable.setItems(passedProduct.getAllAssociatedParts());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("displayPrice"));
        bottomTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            
        // Side panel
        idField.setText(Integer.toString(passedProduct.getId()));
        nameField.setText(passedProduct.getName());
        invField.setText(Integer.toString(passedProduct.getStock()));
        priceField.setText(Double.toString(passedProduct.getPrice()));
        maxField.setText(Integer.toString(passedProduct.getMax()));
        minField.setText(Integer.toString(passedProduct.getMin()));
        
        // addPartButton functionality
        // add parts to bottom table then in saveProduct() add parts to product's arraylist
        addPartButton.setOnAction(e -> {
            if (topTable.getSelectionModel().getSelectedItem() != null) {
                selectedProduct.addAssociatedPart(topTable.getSelectionModel().getSelectedItem());
                bottomTable.setItems(selectedProduct.getAllAssociatedParts());
                list.removeAll(selectedProduct.getAllAssociatedParts()); // remove the item that is associated from the top table to avoid dups
                topTable.setItems(list);
            }
        });
        
        removeButton.setOnAction(e -> {
                Part part1 = bottomTable.getSelectionModel().getSelectedItem();
                if (part1 != null) {
                selectedProduct.deleteAssociatedPart(part1);
                bottomTable.setItems(selectedProduct.getAllAssociatedParts());
                list.add(part1);
                topTable.setItems(list);
                }
        });
        
        partSearchField.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)
            partSearchButtonPressed(storage); });
        
        partSearchButton.setOnAction(e -> {
            partSearchButtonPressed(storage);
        });
    }
    
    private void partSearchButtonPressed(Inventory storage) {
        String text = partSearchField.getText(); 
        ObservableList<Part> newParts = FXCollections.observableArrayList();
    
        // search for substring and add all matching parts to new partlist
        for (Part i : list)
        if (i.getName().contains(text))
            newParts.add(i);
        
        topTable.setItems(newParts);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

/*************************************************************************
 * 
 * ************  Change scene and save changes ***************************
 * 
 ************************************************************************/
    
    // update product array in storage variable
    private void saveProduct() {
        
        String id = idField.getText(),
                stock = invField.getText(),
                min = minField.getText(),
                max = maxField.getText(),
                name = nameField.getText(),
                price = priceField.getText();
        
        if (min.length() < 1)
            min = "0";
        if (max.length() < 1)
            max = "0";
        try {
            if (name.length() < 1 || stock.length() < 1 || price.length() < 1)
                throw new InValidProduct(" name or stock or price are invalid");
        } 
        catch (InValidProduct e) {
            Alert.show(e.sendMsg());
            cancelButton.fire();
            checker = false;
        }
        if (checker) {
        selectedProduct.setId(Integer.parseInt(id));
        selectedProduct.setStock(Integer.parseInt(stock));
        selectedProduct.setMin(Integer.parseInt(min));
        selectedProduct.setMax(Integer.parseInt(max));
        selectedProduct.setName(nameField.getText());
        selectedProduct.setPrice(Double.parseDouble(price));
        }
        
        try {
            if (selectedProduct.min > selectedProduct.max)
                throw new MinMaxException(" Min field is greater than max field..");
        } catch (MinMaxException e) {
            Alert.show(e.sendMsg());
            cancelButton.fire();
            checker = false;
        }
        if (checker) {
            storage.addProduct(selectedProduct);
            storage.deleteProduct(oldProduct);
        }
    }
    
    public void saveProductButtonPushed(ActionEvent event) throws IOException {

        if (idField.getLength() > 0)
            saveProduct(); // adds product to Inventory
        if (checker) {
            // Load document
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            Parent savePartParent = loader.load();

            // get main controller and transfer updated data
            FXMLDocumentController mainController = loader.getController();
            mainController.transferProductData(storage);

            // change scene back
            Scene savePartScene = new Scene(savePartParent);
            Stage savePartStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            savePartStage.setScene(savePartScene);
            savePartStage.show();
        }
    }
    
    public void cancelProductButtonPushed(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
        Parent cancelPartParent = loader.load();
        
        FXMLDocumentController mainController = loader.getController();
        mainController.transferData(storage);
        
        Scene mainScene = new Scene(cancelPartParent);
        Stage mainStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        mainStage.setScene(mainScene);
        mainStage.show();
    }
}