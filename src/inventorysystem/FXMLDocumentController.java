/* Joe Lambert -- FXMLDocumentController.java
 */
package inventorysystem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class FXMLDocumentController implements Initializable {
    
    private Inventory storage;
    @FXML private HBox partHbox, productHbox; // main hbox border
    String cssLayout = "-fx-border-color: grey;\n";

    // Part Table
    @FXML public TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIdColumn, partInventoryLevelColumn;
    @FXML private TableColumn<Part, String> partNameColumn, partPriceColumn;
    @FXML private TextField partSearchField;
    @FXML private Button partSearchButton, partDeleteButton, productDeleteButton;
    
    
    // Product Table
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryLevelColumn;
    @FXML private TableColumn<Product, String> productPriceColumn;
    @FXML private TextField productSearchField;
    @FXML private Button productSearchButton;
    @FXML private Button exitButton;
    
    
    public void transferData(Inventory newStorage) {
        storage = newStorage;
        partTable.setItems(storage.getAllParts());
        productTable.setItems(storage.getAllProducts());
    }
    
    public void transferProductData(Inventory newStorage) {
        storage = newStorage;
        productTable.setItems(storage.getAllProducts());
        partTable.setItems(storage.getAllParts());
    }
    
    private void partDeleteButtonPressed(Inventory storage) {
        ObservableList<Part> selectedList;
        List<String> checkList = new ArrayList<>();
        
        selectedList = partTable.getSelectionModel().getSelectedItems();
        for (Part i: selectedList) {
            checkList.add(i.getName());
        }
       
        int temp = checkList.size();
        for (int i = 0; i < temp; ++i) {
                storage.deletePartById(checkList.get(i));
            }
    } 
    
     private void productDeleteButtonPressed(Inventory storage) {
        ObservableList<Product> selectedList;
        List<String> checkList = new ArrayList<>();
        
        selectedList = productTable.getSelectionModel().getSelectedItems();
        for (Product i: selectedList) {
            checkList.add(i.getName());
        }
       
        int temp = checkList.size();
        for (int i = 0; i < temp; ++i) {
                storage.deleteProductById(checkList.get(i));
            }
     }
     
    private void partSearchButtonPressed(Inventory storage) {
            String userText = partSearchField.getText(); // might need get selected text
            partTable.setItems(storage.partSearch(userText));
    }
    
    private void productSearchButtonPressed(Inventory storage) {
            String userText = productSearchField.getText(); // might need get selected text
            productTable.setItems(storage.productSearch(userText));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        storage = new Inventory();
        
        // Part Table
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("displayPrice"));
        partTable.setItems(initializePartTable(storage));
        partTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // choose multiple row functionality
        
        // Product Table
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("displayPrice"));
        productTable.setItems(initializeProductTable(storage));
        productTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // choose multiple row functionality
        
        partHbox.setStyle(cssLayout);
        productHbox.setStyle(cssLayout);
        
        // Search for part
        partSearchField.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)
            partSearchButtonPressed(storage); });
        
        partSearchButton.setOnAction(e -> {
            partSearchButtonPressed(storage);
        });
        // Search for product
        productSearchField.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)
            productSearchButtonPressed(storage); });
        
        productSearchButton.setOnAction(e -> {
            productSearchButtonPressed(storage);
        });
        
        partDeleteButton.setOnAction(e -> partDeleteButtonPressed(storage));
        productDeleteButton.setOnAction(e -> productDeleteButtonPressed(storage));
        
        exitButton.setOnAction(e -> {
        Platform.exit();
            });
    }

    private ObservableList initializePartTable(Inventory table) {
        table.addPart(new InHouse(1, "Part 1", 5.00, 5, 1, 9, 10));
        table.addPart(new InHouse(2, "Part 2", 10.00, 10, 1, 7, 11));
        table.addPart(new InHouse(3, "Part 3", 15.00, 12, 3, 5, 4));
        return table.getAllParts();
    }

    private ObservableList initializeProductTable(Inventory table) {
//         int id, int stock, int min, int max, String name, double price
        table.addProduct(new Product(1, 5, 1, 5, "Product 1", 5.0));
        table.addProduct(new Product(2, 10, 1, 10, "Product 2", 10.0));
        table.addProduct(new Product(3, 12, 1, 12, "Product 3", 15.0));
        return table.getAllProducts();
    }
/*************************************************************************
 * 
 * ************  Change scene and save changes ***************************
 * 
 ************************************************************************/
    
    // change scene from main screen to add part screen
    public void addPartButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Add Part.fxml"));
        Parent addPartParent = loader.load();
        // get class
        AddPartController addController = loader.getController();
        addController.transferData(storage);
        
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }
    
     // change scene from main screen to modify part screen 
    public void modifyPartButtonPushed(ActionEvent event) throws IOException {
        Part info = partTable.getSelectionModel().getSelectedItem();
        if (info != null) {
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("Modify Part.fxml"));
            Parent modifyPartParent = loader2.load();

            ModifyPartController modifyController = loader2.getController();
            modifyController.transferData(storage, info);

            Scene modifyPartScene = new Scene(modifyPartParent);
            Stage modifyPartStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            modifyPartStage.setScene(modifyPartScene);
            modifyPartStage.show();
        }
    }
    
    // change scene from main screen to add product screen
    public void addProductButtonPushed(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Add Product.fxml"));
        Parent addPartParent = loader.load();
        // get class
        AddProductController addProductController = loader.getController();
        addProductController.transferProductData(storage);
        
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }
    
    // change scene from main screen to modify product screen
    public void modifyProductButtonPushed(ActionEvent event) throws IOException {
        Product info = productTable.getSelectionModel().getSelectedItem();
        if (info != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Modify Product.fxml"));
            Parent addPartParent = loader.load();
            // get class
            ModifyProductController modifyProductController = loader.getController();
            modifyProductController.transferProductData(storage, info);

            Scene addPartScene = new Scene(addPartParent);
            Stage addPartStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            addPartStage.setScene(addPartScene);
            addPartStage.show();
        }
    }
}