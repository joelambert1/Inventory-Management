/* Joe Lambert Alert.java -- for alerting the user of illegal action
 */
package inventorysystem;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert {
    
    public static void show(String message) {
        
        Stage stage = new Stage();
        stage.setTitle("Exception");
       
        String style = "-fx-font-size: 15px; " + "-fx-font-weight: bold;";
        stage.setMinWidth(175);
        stage.setMinHeight(150);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label();
        label.setText(message);
        label.setStyle(style);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> stage.close());

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
