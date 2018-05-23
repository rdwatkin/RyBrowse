import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.stage.Stage;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Stack;
import java.util.EmptyStackException;

public class browser extends Application{
	/* Objects and State Variables */
	String current_Url = "https://www.google.com";

	HBox utilBar;
	BorderPane root;

	Button go;
	Button back;
	Button forward;
	TextField searchBar;
	WebView wv;
	WebEngine we;

	Stack<String> back_Stack = new Stack<String>();
	Stack<String> forward_Stack = new Stack<String>();

	/* Loads webpage
	 * Also does small text manipulation to ensure proper url */
	public void goToPage(){
		String tmpUrl = searchBar.getText();
		if(!tmpUrl.substring(0,4).equals("http"))
			{ searchBar.setText("https://"+tmpUrl);
			}else{ tmpUrl = tmpUrl.substring(8); }
		if(!searchBar.getText().substring(8,11).equals("www"))
			{ searchBar.setText("https://www."+tmpUrl); }
		we.load(searchBar.getText());
		back_Stack.push(current_Url);
		current_Url = searchBar.getText();
	}

	/* Loads previous page
	 * pops back_Stack and pushed front_Stack */
	public void goBack(){
		try{
			String tmpUrl = back_Stack.pop();
			searchBar.setText(tmpUrl);
			we.load(tmpUrl);
			forward_Stack.push(current_Url);
			current_Url = tmpUrl;
		}catch(EmptyStackException e){
			System.err.println("goBack(): back_Stack is empty");
		}
	}

	@Override
	public void start(Stage stage){

		/* Create WebView object and load HomePage */
		wv = new WebView();
		we = wv.getEngine();
		we.load("http://www.google.com/");

		/* instantiate ui elements */
		go = new Button("Go");
		back = new Button("<-");
		forward = new Button("->");
		searchBar = new TextField("https://www.google.com/");
		searchBar.setMaxHeight(go.getHeight());

		/* ----- Lambda Notation used for Event Handling ------ */

		/* load new page on enter */
		searchBar.setOnKeyPressed( (event) ->
			{ if(event.getCode() == KeyCode.ENTER){goToPage();}});
		/* load new page on click */
		go.setOnAction( (event) -> { goToPage(); });

		/* load previous page */
		back.setOnAction( (event) -> { goBack(); });

		/* load page preceding goBack() */
		forward.setOnAction( (event) -> {
			try{
				searchBar.setText(forward_Stack.pop());
			}catch(EmptyStackException e){
				System.err.println("forward event: Empty forward_Stack");
			}
			goToPage();
		});


		/* Format/Container Objects */
		utilBar = new HBox();
		utilBar.getChildren().addAll(back, forward, go, searchBar);
		utilBar.setHgrow(searchBar, Priority.ALWAYS);
		root = new BorderPane();
		root.setTop(utilBar);
		root.setCenter(wv);

		/* Create & Show GUI */
		stage.setTitle("RyBrowse");
		stage.setScene(new Scene(root, 800, 600));
		stage.show();
	}


	public static void main (String[] args){
		launch(args);
	}



}
