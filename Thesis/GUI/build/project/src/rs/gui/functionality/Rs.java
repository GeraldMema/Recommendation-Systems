package rs.gui.functionality;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Is the main class that draws the JavaFX applicaation GUI
 *
 * @author georgia
 */
public class Rs extends Application
{
    /**
     *
     * @param primaryStage Needed from the specification of the JavaFX
     */
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("RC Welcome");

        BorderPane border = new BorderPane();

        border.setTop(getTitle());
        border.setCenter(getOptions());
        border.setBottom(getFooter());

        Scene scene = new Scene(border);
        primaryStage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        scene.getStylesheets().add(Rs.class.getResource("/rs/gui/others/Rs.css").toExternalForm());
        primaryStage.show();
    }

    private VBox getTitle()
    {
        //Set title
        Text scenetitle = new Text("Multi-Criteria\nRecommender Systems");
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

    private GridPane getOptions()
    {
        int line_counter = 0;

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        GridPane grid = new GridPane();
        
//        grid.setGridLinesVisible(true);

        //Set options line
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0, 30, 0, 30));
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10);
        hbox.getStyleClass().add("options-bar");
        Text txt = new Text("Database Options    |   ");
        txt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(txt);
        txt = new Text("Database URL: ");
        txt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(txt);
        Options.database_url.setPrefColumnCount(25);
        hbox.getChildren().add(Options.database_url);
        txt = new Text("Username: ");
        txt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(txt);
        hbox.getChildren().add(Options.database_username);
        txt = new Text("Password: ");
        txt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(txt);
        hbox.getChildren().add(Options.database_password);

        //Set options line to main grid
        ColumnConstraints cc = new ColumnConstraints();
        RowConstraints rc = new RowConstraints();
        //cc.setPercentWidth(100);
        rc.setPercentHeight(5);
        grid.getRowConstraints().add(rc);
        //grid.getColumnConstraints().add(cc);
        grid.add(hbox, 0, 0, 2, 1);

        hbox = new HBox();
        hbox.setPadding(new Insets(0, 30, 0, 30));
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10);
        hbox.getStyleClass().add("options-bar");
        txt = new Text("Combinations Options    |   ");
        txt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(txt);
        txt = new Text("Combinations of number of items: ");
        hbox.setSpacing(10);
        txt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(txt);
        Options.combinations_of_n.setPrefColumnCount(5);
        hbox.getChildren().add(Options.combinations_of_n);
        txt = new Text("Maximum combinations: ");
        hbox.setSpacing(10);
        txt.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(txt);
        Options.max_combinations.setPrefColumnCount(5);
        hbox.getChildren().add(Options.max_combinations);

        //Set options line to main grid
        rc = new RowConstraints();
        //cc.setPercentWidth(100);
        rc.setPercentHeight(5);
        grid.getRowConstraints().add(rc);
        //grid.getColumnConstraints().add(cc);
        grid.add(hbox, 0, 1, 2, 1);

        //Start of left grid
        GridPane grid_left = new GridPane();
        grid_left.setHgap(10);
        grid_left.setVgap(10);
        grid_left.setPadding(new Insets(15, 15, 15, 15));
        grid_left.setAlignment(Pos.TOP_LEFT);
        Text text = new Text("Multi-step Multi-Criteria Recommender System Options");
        text.setTextAlignment(TextAlignment.CENTER);
        text.getStyleClass().add("title-1");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        text = new Text("Choose any options you want to run the algorithm");
        text.setTextAlignment(TextAlignment.LEFT);
        text.getStyleClass().add("title-3");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        //Step 1
        text = new Text("Step 1");                  
        text.setTextAlignment(TextAlignment.LEFT);
        text.getStyleClass().add("title-2");    //css
        text.setUnderline(true);                //underline
        grid_left.add(text, 0, line_counter++);  //emfanish Step1

        text = new Text("Determine the aggregation function:");
        text.getStyleClass().add("title-3");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++); //εμφανιση της επιλογης

        Options.cb_linear_aggregation.setSelected(true);
        grid_left.add(Options.cb_linear_aggregation, 0, line_counter++);

        //Step 2
        text = new Text("Step 2");
        text.setTextAlignment(TextAlignment.LEFT);
        text.getStyleClass().add("title-2");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        text = new Text("Determine the similarity using only weights function:");
        text.getStyleClass().add("title-3");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        Options.cb_weighted_cosine_similarity.setSelected(true);
        grid_left.add(Options.cb_weighted_cosine_similarity, 0, line_counter++);
        grid_left.add(Options.cb_weighted_pearson_similarity,0,line_counter++);
        
        
        //Step 3
        text = new Text("Step 3");
        text.setTextAlignment(TextAlignment.LEFT);
        text.getStyleClass().add("title-2");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        text = new Text("Determine the similarity using only overall ratings function:");
        text.getStyleClass().add("title-3");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        Options.cb_overall_cosine_similarity.setSelected(true);
        grid_left.add(Options.cb_overall_cosine_similarity, 0, line_counter++);
        grid_left.add(Options.cb_pearson_similarity, 0, line_counter++);

        //Step 4
        text = new Text("Step 4");
        text.setTextAlignment(TextAlignment.LEFT);
        text.getStyleClass().add("title-2");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        text = new Text("Determine the aggregation similarity using similarities form Step2 and Step3 function:");
        text.getStyleClass().add("title-3");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        Options.cb_multiplication_similarity.setSelected(true);
        grid_left.add(Options.cb_multiplication_similarity, 0, line_counter++);
        grid_left.add(Options.cb_intersection_connectives_infinity, 0, line_counter++);
        grid_left.add(Options.cb_intersection_connectives_1, 0, line_counter++);
        grid_left.add(Options.cb_intersection_connectives_2, 0, line_counter++);

        //Step 5
        text = new Text("Step 5");
        text.setTextAlignment(TextAlignment.LEFT);
        text.getStyleClass().add("title-2");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        text = new Text("Choose the prediction function to be used:");
        text.getStyleClass().add("title-3");
        text.setUnderline(true);
        grid_left.add(text, 0, line_counter++);

        Options.cb_weighted_sum_approach.setSelected(true);
        grid_left.add(Options.cb_weighted_sum_approach, 0, line_counter++);
        grid_left.add(Options.cb_adjusted_weighted_sum_approach, 0, line_counter++);

        ScrollPane sp = new ScrollPane();
        sp.setMinWidth(primaryScreenBounds.getWidth() / 4);
        sp.setContent(grid_left);
        sp.getStyleClass().add("transparent-background");
        //End of left grid

        //Set left grid to main grid
        cc = new ColumnConstraints();
        rc = new RowConstraints();
        cc.setPrefWidth(primaryScreenBounds.getWidth() / 2);
        rc.setPercentHeight(90);
        grid.getColumnConstraints().add(cc);
        grid.getRowConstraints().add(rc);
        grid.add(sp, 0, 2);

        //Start of right grid
        GridPane grid_right = new GridPane();

        text = new Text("Results");
        text.setTextAlignment(TextAlignment.CENTER);
        text.getStyleClass().add("title-1");
        text.setUnderline(true);
        grid_right.add(text, 0, 0);

        grid_right.setAlignment(Pos.TOP_CENTER);
        grid_right.setHgap(10);
        grid_right.setVgap(10);
        grid_right.setPadding(new Insets(15, 15, 15, 15));
        grid_right.setVgap(5);
        Options.actiontarget.setFill(Color.BLACK);
        Options.actiontarget.setFont(Font.font("Verdana", 15));
        Options.actiontarget.setText("Results will go here");
        grid_right.add(Options.actiontarget, 0, 1);

        sp = new ScrollPane();
        sp.setContent(Options.actiontarget);
        sp.setPrefSize((primaryScreenBounds.getWidth() / 2) * 0.8, (primaryScreenBounds.getHeight() / 4) * 3 * 0.8);
        Options.actiontarget.setWrappingWidth((primaryScreenBounds.getWidth() / 2) * 0.8);
        grid_right.add(sp, 0, 2);

        cc = new ColumnConstraints();
        cc.setPrefWidth(primaryScreenBounds.getWidth() / 2);
        grid.getColumnConstraints().add(cc);
        grid.add(grid_right, 1, 2);
        grid.setAlignment(Pos.CENTER_LEFT);
        //End of right grid

        return grid;
    }

    private HBox getFooter()
    {
        HBox hbox = new HBox();
        hbox.setId("footer");
        hbox.setPadding(new Insets(5, 20, 5, 20));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.BOTTOM_LEFT);

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

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args The command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
