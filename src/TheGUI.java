import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TheGUI extends Application {
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
	  
	  GridPane MainPane = new GridPane();								// Our main holder(pane)
	  MainPane.setAlignment(Pos.CENTER);								// Set aligment
	  MainPane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));			// Set padding
	  MainPane.setHgap(5.5);											// Set horizontal gap
	  MainPane.setVgap(5.5);											// Set vertical gap
	  MainPane.add(new Label("Enter file or directory path:"), 0, 0);	// Set label for dir/file and position
	  TextField directory = new TextField();							// Make a new textfield for the dir/file name
	  directory.setPrefWidth(450);										// Set width of textfield
	  MainPane.add(directory, 1, 0);									// Set position for the dir/file name and add it to main holder(pane)
	  MainPane.add(new Label("Enter path to hashfile:"), 0, 1);			// make label for the hashfile
	  TextField hashFile = new TextField();								// make textfield for the hashfile
	  hashFile.setPrefWidth(450);										// Set width of the hashfile
	  MainPane.add(hashFile, 1, 1);										// Set position for the hashfile and add it to main holder(pane
	  
	  
	  FlowPane btpane = new FlowPane();									// Make a pane to hold buttons
	  Button btHash = new Button("Hash files");							// Make button for hashing files
	  MainPane.add(btHash, 0, 2);										// At it to the main holder(pane)
	  GridPane.setHalignment(btHash, HPos.RIGHT);						// Set horizontal alignment and add it to the main holder(pane)
	  Button btVerify = new Button("Verify files");						// Make a button for verifying 
	  MainPane.add(btVerify, 0, 2);										// Add it to the main holder(pane)
	  GridPane.setHalignment(btVerify, HPos.LEFT);						// Set horizontal alignment and add it to the main holder(pane)
	  MainPane.getChildren().addAll(btpane);							// Add the btpane to the main holder(pane)
	  
	  
	  btVerify.setOnAction(new EventHandler<ActionEvent>() {			// Action handler for verify button
	      @Override 
	      public void handle(ActionEvent e) {							// On click
	    	  Stage stage = new Stage();								// Make a new stage
	    	  File fileToWorkWith = new File(directory.getText());		// Get text from textfield and set it to be the file to work with
	    	  String hash = hashFile.getText();							// Get text from textfield and set it to be hashlist to work with
	          TextArea result = new TextArea();							// Make a new textarea to show results in
	          
	          result.setWrapText(true);									// Set wrap for text
	          result.setStyle("-fx-text-fill: black");					// Set style
	          result.setFont(Font.font("Times", 14));					// Set font style and size
	          result.setPrefWidth(590);									// Set width
	          result.setPrefHeight(249);								// Set height								
	          result.setEditable(false);								// Disable useraccess to textfield
	          ArrayList<String> toPrint = WhereAllTheMagicHappens.getAnswerForGUIVer(fileToWorkWith, hash);	// Array for result - and get the result
	          if(toPrint.isEmpty()){									// If we did not get any results
	        	  result.appendText("Ingen resultat mottat \n");		// Print message
	          }
	          else {													// Else, for every result we got
	        	  for(int i = 0; i < toPrint.size();i++){				
	        	  result.appendText((String) toPrint.get(i) + "\n");  	// Print it!
	        	  }
	          }
	          WhereAllTheMagicHappens.clearGUIResponse();				// Clear the array in WhereAllTheMagicHappens.java in case we want to run the program again
	          ScrollPane scrollPane = new ScrollPane(result);			// Add the result to a scrollpane
	          
	          Scene scene = new Scene(scrollPane, 600, 252);			// Set scrollpane size (width, height)
	          stage.setTitle("Results Verifying Files");				// Set stage title
	          stage.setScene(scene);									// Set the scene in the stage
	          stage.show();												// And show the stage
	      }
	  });
	  
	  btHash.setOnAction(new EventHandler<ActionEvent>() {				// This part does exactly the same as the snippet above, just for the hash function
	      @Override public void handle(ActionEvent e) {					// No more comments for this one, look at the snippet above for exp.
	    	  Stage stage = new Stage();
	    	  File fileToWorkWith = new File(directory.getText());
	    	  String hash = hashFile.getText();
	          TextArea result = new TextArea();
	          
	          result.setWrapText(true);
	          result.setStyle("-fx-text-fill: black");
	          result.setFont(Font.font("Times", 14));
	          result.setPrefWidth(599);
	          result.setPrefHeight(249);
	          result.setEditable(false);
	          ArrayList<String> toPrint = WhereAllTheMagicHappens.getAnswerForGUIhash(fileToWorkWith, hash);
	          if(toPrint.isEmpty()){
	        	  result.appendText("Ingen resultat mottat \n");
	          }
	          else {
	        	  for(int i = 0; i < toPrint.size();i++){
	          
	        	  
	        	  result.appendText((String) toPrint.get(i) + "\n");
	        	  }
	          }
	          WhereAllTheMagicHappens.clearGUIResponse();
	          ScrollPane scrollPane = new ScrollPane(result);
	          
	          Scene scene = new Scene(scrollPane, 600, 250);
	          stage.setTitle("Results Hashing Files");
	          stage.setScene(scene);
	          stage.show();
	      }
	  });
	  
	  
	  
	  
	  													// Create a scene and place it in the stage
    Scene scene = new Scene(MainPane, 550, 150);
    primaryStage.setTitle("Generate or Verify Hash"); 	// Set the stage title
    primaryStage.setScene(scene); 						// Place the scene in the stage
    primaryStage.show(); 								// Display the stage
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
