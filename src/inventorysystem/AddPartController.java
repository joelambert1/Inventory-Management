/* Joe Lambert AddPartController.java
 */
package inventorysystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;


public class AddPartController implements Initializable {
    // main inventory
    private Inventory storage;
    private boolean checker = true;
    
    // radio buttons
    @FXML private RadioButton inHouseButton, outSourcedButton;
    private ToggleGroup locationSelectionToggleGroup; // groups buttons
    
    // label and textfield for changeable field
    @FXML private Label changeableLabel;
    @FXML private TextField changeableText;
    
    // textfields for information
    @FXML private TextField idText, nameText, invText, priceText, maxText,minText;
    @FXML private Button cancelButton, saveButton;

    // buttons for in house or outsourced
    public void radioButtonChanged() { 
        if (this.locationSelectionToggleGroup.getSelectedToggle().equals(this.inHouseButton)) {
            changeableLabel.setText("Machine ID");
            changeableText.setPromptText("Mach ID");
        }
        else if (this.locationSelectionToggleGroup.getSelectedToggle().equals(this.outSourcedButton)) {
            changeableLabel.setText("Company Name");
            changeableText.setPromptText("Comp Nm");
        }
    }
    
    public void transferData (Inventory inventory) {
        storage = inventory;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // radiobutton configurations
        locationSelectionToggleGroup = new ToggleGroup();
        this.inHouseButton.setToggleGroup(locationSelectionToggleGroup);
        this.outSourcedButton.setToggleGroup(locationSelectionToggleGroup);
    } 
    
/*************************************************************************
 * 
 * ************  Change scene and save changes ***************************
 * 
 ************************************************************************/

    private void savePart() {
        int id, stock, min, max;
        String name;
        double price;
        String id1 = idText.getText(),
                stock1 = invText.getText(),
                min1 = minText.getText(),
                max1 = maxText.getText(),
                name1 = nameText.getText(),
                price1 = priceText.getText();
        if (id1.length() < 1 || stock1.length() < 1 || name1.length() < 1) {
            checker = false;
            cancelButton.fire();
        }
        if (min1.length() < 1 || max1.length() < 1) {
            min1 = "0";
            max1 = "0";
        }
        
        if (price1.length() < 1)
            price1 = "0";
        if (checker) {
            if (Integer.parseInt(stock1) < Integer.parseInt(min1))
                stock1 = min1;
            if (Integer.parseInt(stock1) > Integer.parseInt(max1))
                stock1 = max1;
        
            id = Integer.parseInt(id1);
            stock = Integer.parseInt(stock1);
            min = Integer.parseInt(min1);
            max = Integer.parseInt(max1);
            name = name1;
            price = Double.parseDouble(price1);
        
        try {
            if (min > max)
                throw new MinMaxException(" Min field is greater than max field..");
        } catch (MinMaxException e) {
            Alert.show(e.sendMsg());
            cancelButton.fire();
            checker = false;
        }
        
        if (changeableText.getText().contains("Machine")) {//inhouse
           int changed = Integer.parseInt(changeableText.getText());
           InHouse part = new InHouse(id, name, price, stock, min, max, changed);
           storage.addPart(part);
        }
        else {
            String changed = changeableText.getText();
            Outsourced part = new Outsourced(id, name, price, stock, min, max, changed);
            storage.addPart(part);
        }
        }
        
    }
 
    // change scene from add part to main screen and pass info
    public void savePartButtonPushed(ActionEvent event) throws IOException {

            savePart(); // adds part to Inventory
        if (checker) {
        
        // Load document
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
        Parent savePartParent = loader.load();
        
        // get main controller and transfer updated data
        FXMLDocumentController mainController = loader.getController();
        mainController.transferData(storage);

        // change scene back
        Scene savePartScene = new Scene(savePartParent);
        Stage savePartStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        savePartStage.setScene(savePartScene);
        savePartStage.show();
        }
    }
    
    // Change scene without re-initializing
    public void cancelPartButtonPushed(ActionEvent event) throws IOException {
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
