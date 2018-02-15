package application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import rs.gui.functionality.CancelButtonAction;
import rs.gui.functionality.Options;
import rs.gui.functionality.RunButtonAction;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	VBox vb;
	@Override
	public void start(Stage primaryStage) {
		try {
			
			primaryStage.setTitle("RC Welcome");
			
			Group root = new Group();
			
			ScrollBar sc = new ScrollBar();
			vb = new VBox();
			BorderPane border = new BorderPane();
			Scene scene = new Scene(root);

			border.setTop(getTitle());
			border.setCenter(getOptions());
			border.prefWidthProperty().bind(scene.widthProperty());
//			border.prefHeightProperty().bind(scene.heightProperty());
			
			vb.getChildren().add(border);
			
			root.getChildren().addAll(vb,sc);

			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	        //set Stage boundaries to visible bounds of the main screen
	        primaryStage.setX(primaryScreenBounds.getMinX());
	        primaryStage.setY(primaryScreenBounds.getMinY());
	        primaryStage.setWidth(primaryScreenBounds.getWidth());
	        primaryStage.setHeight(primaryScreenBounds.getHeight());
	        primaryStage.setMinWidth(800);
	        primaryStage.setMinHeight(600);
	 
	        sc.setLayoutX(primaryScreenBounds.getWidth()-sc.getWidth()+2);
	        sc.setMin(0);
	        sc.setOrientation(Orientation.VERTICAL);
	        sc.setPrefHeight(primaryScreenBounds.getHeight());
	        sc.setMax(360);
	        sc.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    vb.setLayoutY(-new_val.doubleValue());
	            }
	        });

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private GridPane getOptionsGrid()
	{
		
		GridPane grid = new GridPane();
		
		grid.setHgap(30);
		grid.setVgap(5);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		grid.setId("optionsGrid");
		
		Text txt = new Text("		Database Options   ");
		txt.setId("optionsTitle");
        txt.setTextAlignment(TextAlignment.CENTER);
		grid.add(txt,0,0,2,1);
		
		
		txt = new Text("Database URL: ");
		txt.setId("optionsTxt");
        txt.setTextAlignment(TextAlignment.CENTER);
        grid.add(txt,0,1);
        Options.database_url.setPrefColumnCount(25);
        grid.add(Options.database_url,1,1);
        
        txt = new Text("Username: ");
        txt.setId("optionsTxt");
        txt.setTextAlignment(TextAlignment.LEFT);
        grid.add(txt,0,2);
        grid.add(Options.database_username,1,2);
        
        txt = new Text("Password: ");
        txt.setId("optionsTxt");
        txt.setTextAlignment(TextAlignment.LEFT);
        grid.add(txt,0,3);
        grid.add(Options.database_password,1,3);
        
        // Combination options
        
        txt = new Text("			Combinations Options    ");
        txt.setId("optionsTitle");
        txt.setTextAlignment(TextAlignment.RIGHT);
        grid.add(txt,3,0,2,1);
        
        txt = new Text("	Combinations of number of items: ");
        txt.setId("optionsTxt");
        txt.setTextAlignment(TextAlignment.LEFT);
        grid.add(txt,3,1);
        Options.combinations_of_n.setPrefColumnCount(5);
        grid.add(Options.combinations_of_n,4,1);
        
        txt = new Text("	Maximum combinations: ");
        txt.setId("optionsTxt");
        txt.setTextAlignment(TextAlignment.LEFT);
        grid.add(txt,3,2);
        Options.max_combinations.setPrefColumnCount(5);
        grid.add(Options.max_combinations,4,2);
        
        return grid;
		
	}
	
	private GridPane getMiddleCenterGrid()
	{
		GridPane grid2 = new GridPane();
//      grid2.setGridLinesVisible(true);
      grid2.setMaxWidth(2000);
      
      
      grid2.setVgap(20);
      grid2.setHgap(20);
      
      Text text = new Text("Multi-step Multi-Criteria Recommender System Options");
      text.setTextAlignment(TextAlignment.CENTER);
      text.getStyleClass().add("title-1");
      text.setUnderline(true);
      grid2.add(text, 0, 0,4,1);
      
      //Step 1
      text = new Text("Step 1 :");                  
      text.setTextAlignment(TextAlignment.CENTER);
      text.getStyleClass().add("title-2");    //css
      text.setUnderline(true);                //underline
      grid2.add(text, 0, 1);
      
      text = new Text("Aggregation function:");
      text.getStyleClass().add("title-3");
      text.setUnderline(true);
      grid2.add(text, 1, 1);
      
      Options.cb_linear_aggregation.setSelected(true);
      grid2.add(Options.cb_linear_aggregation, 2, 1);
      
      //Step 2
      text = new Text("Step 2:");
      text.setTextAlignment(TextAlignment.LEFT);
      text.getStyleClass().add("title-2");
      text.setUnderline(true);
      grid2.add(text, 0, 2);
      
      text = new Text("Similarity using only weights function:");
      text.getStyleClass().add("title-3");
      text.setUnderline(true);
      grid2.add(text, 1, 2);
      
      Options.cb_weighted_cosine_similarity.setSelected(true);
      grid2.add(Options.cb_weighted_cosine_similarity, 2, 2);
      grid2.add(Options.cb_weighted_pearson_similarity,3,2);
      
      //Step 3
      text = new Text("Step 3:");
      text.setTextAlignment(TextAlignment.LEFT);
      text.getStyleClass().add("title-2");
      text.setUnderline(true);
      grid2.add(text, 0, 3);

      text = new Text("Similarity using only overall ratings function:");
      text.getStyleClass().add("title-3");
      text.setUnderline(true);
      grid2.add(text, 1, 3);

      Options.cb_overall_cosine_similarity.setSelected(true);
      grid2.add(Options.cb_overall_cosine_similarity, 2, 3);
      grid2.add(Options.cb_pearson_similarity, 3, 3);
      
      //Step 4
      text = new Text("Step 4:");
      text.setTextAlignment(TextAlignment.LEFT);
      text.getStyleClass().add("title-2");
      text.setUnderline(true);
      grid2.add(text, 0, 4);

      text = new Text("Aggregation similarity:");
      text.getStyleClass().add("title-3");
      text.setUnderline(true);
      grid2.add(text, 1, 4);

      Options.cb_multiplication_similarity.setSelected(true);
      grid2.add(Options.cb_multiplication_similarity, 2, 4);
      grid2.add(Options.cb_intersection_connectives_infinity, 2, 5);
      grid2.add(Options.cb_intersection_connectives_1, 3, 4);
      grid2.add(Options.cb_intersection_connectives_2, 3, 5);
      
      //Step 5
      text = new Text("Step 5:");
      text.setTextAlignment(TextAlignment.LEFT);
      text.getStyleClass().add("title-2");
      text.setUnderline(true);
      grid2.add(text, 0, 6);

      text = new Text("Choose the prediction function to be used:");
      text.getStyleClass().add("title-3");
      text.setUnderline(true);
      grid2.add(text, 1, 6);

      Options.cb_weighted_sum_approach.setSelected(true);
      grid2.add(Options.cb_weighted_sum_approach, 2, 6);
      grid2.add(Options.cb_adjusted_weighted_sum_approach, 3, 6);
      
      return grid2;
	}
	
	private GridPane getOptions()
	{
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		
		GridPane generalGrid = new GridPane();
		generalGrid.setVgap(10);
		generalGrid.setMaxWidth(2000);
		
		GridPane grid = getOptionsGrid();
        
        //add options grid to general grid
        generalGrid.add(grid, 0, 0);
        
        GridPane grid2 = getMiddleCenterGrid();
        grid2.setMaxWidth(2000);
        
        generalGrid.add(grid2, 0, 1);
        
        GridPane gridBottomCenter = new GridPane();
        
        Text text = new Text("Results");
        text.setTextAlignment(TextAlignment.CENTER);
        text.getStyleClass().add("resultsTextTitle");
        text.setUnderline(true);
        gridBottomCenter.add(text, 0, 0);
        
        gridBottomCenter.setAlignment(Pos.TOP_CENTER);
        gridBottomCenter.setHgap(10);
        gridBottomCenter.setVgap(10);
        gridBottomCenter.setPadding(new Insets(0, 15, 10, 15));
        gridBottomCenter.setVgap(5);
        Options.actiontarget.setFill(Color.BLACK);
        Options.actiontarget.setFont(Font.font("Verdana", 15));
        Options.actiontarget.setText("Results will go here");
        gridBottomCenter.add(Options.actiontarget, 0, 1);
        
        ScrollPane sp = new ScrollPane();
        sp.setContent(Options.actiontarget);
        sp.setPrefSize((primaryScreenBounds.getWidth() ) * 0.7, (primaryScreenBounds.getHeight() /1.5));
        Options.actiontarget.setWrappingWidth((primaryScreenBounds.getWidth() / 2) * 0.8);
        gridBottomCenter.add(sp, 0, 2);
        
        gridBottomCenter.add(getFooter(), 1, 2);
        
        generalGrid.add(gridBottomCenter, 0, 2);
		
        generalGrid.getStyleClass().add("generalGrid");
        
		return generalGrid;
	}
	
	private HBox getFooter()
    {
        HBox hbox = new HBox();
        hbox.setId("footer");
        hbox.setPadding(new Insets(5, 20, 5, 20));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.TOP_LEFT);

        //Submit button
        Button btn = new Button("Run");
        RunButtonAction rba = new RunButtonAction();
        btn.setOnAction(rba);
        hbox.getChildren().add(btn);

        //Cancel button
        btn = new Button("Cancel");
        CancelButtonAction cba = new CancelButtonAction();
        btn.setOnAction(cba);
        hbox.getChildren().add(btn);

        //Progress bar
        Text text = new Text("Progress: ");
        text.setTextAlignment(TextAlignment.CENTER);
        VBox vb = new VBox();
        vb.getChildren().add(text);
        Options.pb.setProgress(0.0);
        vb.getChildren().add(Options.pb);
        hbox.getChildren().add(vb);

        //Progress counter
        Options.pbt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(Options.pbt);
        
        //Progress Indicator
        Options.pi.setProgress(-1);
        Options.pi.prefHeight(2);
        Options.pi.setMaxWidth(1);
        Options.pi.setMaxHeight(1);
        Options.pi.setVisible(false);
        hbox.getChildren().add(Options.pi);

        return hbox;
    }

	private VBox getTitle()
	{
		//Set title
		Text scenetitle = new Text("Multi-Criteria Recommender Systems");
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		scenetitle.getStyleClass().add("title");
		VBox vbox = new VBox();
		vbox.setId("top-field");
		vbox.setPadding(new Insets(0, 20, 0, 20));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(scenetitle);
		vbox.setSpacing(10);

		return vbox;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
