import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.stage.Stage;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Stack;
import java.util.EmptyStackException;

public class browser extends Application{


	Button go;
	Button back;
	Button forward;
	TextField searchBar;
	WebView wv;
	WebEngine we;

	Stack<String> back_Stack = new Stack<String>();
	Stack<String> forward_Stack = new Stack<String>();



	@Override
	public void start(Stage stage){

			BorderPane root = new BorderPane();
			TabPane tab_bar = new TabPane();
			root.setTop(tab_bar);
			wv = new WebView();
			root.setCenter(wv);


			Tab tab1 = new Tab("tab1");
			Tab tab2 = new Tab("tab2");
			WebView we1 = new WebView();
			WebView we2 = new WebView();
			tab_bar.getTabs().addAll(tab1, tab2);
			we1.getEngine().load("https://www.google.com/");
			we2.getEngine().load("https://www.wikipedia.org/");
			tab1.setContent(we1);
			tab2.setContent(we2);

			/* instantiate ui elements */
			go = new Button("Go");
			back = new Button("<-");
			forward = new Button("->");
			searchBar = new TextField("https://www.google.com/");
			searchBar.setMaxHeight(go.getHeight());

			/* ----- Lambda Notation used for Event Handling ------ */

			/* load new page on enter */
			searchBar.setOnKeyPressed( (event) ->
				{ if(event.getCode() == KeyCode.ENTER){
					Tab n = tab_bar.getSelectionModel().getSelectedItem();
					goToPage(n.getContent());}
				});

			/* load new page on click */
			go.setOnAction( (event) -> {
				Tab n = tab_bar.getSelectionModel().getSelectedItem();
				goToPage(n.getContent());
			});

			/* load previous page */
			back.setOnAction( (event) -> {
				Tab n = tab_bar.getSelectionModel().getSelectedItem();
				goBack(n.getContent());
			});

			/* load page preceding goBack() */
			forward.setOnAction( (event) -> {
				try{
					searchBar.setText(forward_Stack.pop());
				}catch(EmptyStackException e){
					System.err.println("forward event: Empty forward_Stack");
				}
				Tab n = tab_bar.getSelectionModel().getSelectedItem();
				goToPage(n.getContent());
			});

			tab1.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
				if(isNowSelected){
					wv = tab_bar.getSelectionModel().getSelectedItem().getContent();
				}
			});

			tab2.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
				if(isNowSelected){
					wv = tab_bar.getSelectionModel().getSelectedItem().getContent();
				}
			});


			stage.setTitle("RyBrowse");
			stage.setScene(new Scene(root, 800, 600));
			stage.show();

	}


	public static void main(String[] args){
		launch(args);
	}


	/* ---------- Helper Functions ---------*/



	/* Loads webpage
	 * Also does small text manipulation to ensure proper url */
	public void goToPage(WebView webview){
		String tmpUrl = searchBar.getText();
		if(!tmpUrl.substring(0,4).equals("http"))
			{ searchBar.setText("https://"+tmpUrl);
			}else{ tmpUrl = tmpUrl.substring(8); }
		if(!searchBar.getText().substring(8,11).equals("www"))
			{ searchBar.setText("https://www."+tmpUrl); }
		webview.getEngine().load(searchBar.getText());
		back_Stack.push(current_Url);
		current_Url = webview.locationProperty();
		wb.load(current_Url);
	}


	/* Loads previous page
	 * pops back_Stack and pushed front_Stack */
	public void goBack(WebView webview){
		try{
			String tmpUrl = back_Stack.pop();
			searchBar.setText(tmpUrl);
			webview.load(tmpUrl);
			wv.load(tmpUrl);
			forward_Stack.push(current_Url);
			current_Url = tmpUrl;
		}catch(EmptyStackException e){
			System.err.println("goBack(): back_Stack is empty");
		}
	}


}
