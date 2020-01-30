/* Joe Lambert ModifyPartController.java
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

public class ModifyPartController implements Initializable {
    
    // copy of main inventory
    private Inventory storage;
    private Part passedPart;
    
    @FXML private RadioButton inHouseButton, outSourcedButton;
    private ToggleGroup locationSelectionToggleGroup; // groups radio buttons
    
    // label and textfield for changeable field
    @FXML private Label changeableLabel;
    @FXML private TextField changeableText;
    
    // textfields for entered information
    @FXML private TextField idText, nameText, invText, priceText, maxText, minText;
    private boolean checker = true;
    @FXML private Button cancelButton;
    
    // configure buttons for in house or outsourced
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
    
    // get data from main screen (inventory and modifiable part)
    public void transferData (Inventory inventory, Part part) {
        storage = inventory;
        passedPart = part;
        if (part != null) {
            idText.setText(Integer.toString(part.getId()));
            nameText.setText(part.getName());
            invText.setText(Integer.toString(part.getStock()));
            priceText.setText(Double.toString(part.getPrice()));
            maxText.setText(Integer.toString(part.getMax()));
            minText.setText(Integer.toString(part.getMin()));
        
            if (part instanceof InHouse) {
                inHouseButton.fire();
                changeableText.setText(Integer.toString(((InHouse) part).getMachineId()));
            }
            else if (part instanceof Outsourced) {
                outSourcedButton.fire();
                changeableText.setText(((Outsourced) part).getCompanyName());
            }
        }
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
    
    // Save changes
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
        
            if (this.locationSelectionToggleGroup.getSelectedToggle().equals(this.inHouseButton)) {//inhouse
                String machineId = changeableText.getText();
                if (machineId.length() < 1)
                    machineId = "0";
                int changed = Integer.parseInt(machineId);
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

    // change scene to main scene and save
    public void savePartButtonPushed(ActionEvent event) throws IOException {
        if (idText.getLength() > 0) { // turn this into try / catch
            savePart(); // adds part to Inventory
            if (checker)
                storage.deletePart(passedPart);  
        }
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

    // Change scene to main scene without re-initializing
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