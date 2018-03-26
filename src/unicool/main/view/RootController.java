package unicool.main.view;

import unicool.main.MainApp;

import java.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RootController {
	
	@FXML
	private MainApp mainApp;
	@FXML
	private Label daytimeLabel;
	
	@FXML
	private void initialize() {
		handleBtnStart();
		try {
			new Serial().connect("COM1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void handleBtnStart() {
        Thread thread = new Thread() {
            @Override
            public void run() {
    			while(true){
    				Calendar cal = Calendar.getInstance();
        			String now = cal.get(Calendar.YEAR)+"년"+
        			(cal.get(Calendar.MONTH)+1)+"월"+
        			cal.get(Calendar.DATE)+"일"+
        			cal.get(Calendar.HOUR)+"시"+
        			cal.get(Calendar.MINUTE)+"분"+
        			cal.get(Calendar.SECOND)+"초";
	                Platform.runLater(() -> {
	                	daytimeLabel.setText(now);
	                	});
	                try {
	                	Thread.sleep(100);
	                } catch (InterruptedException e) {}
	            }
            }
        };
       	thread.setDaemon(true);
       	thread.start();
       	
       	
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	@FXML
	private void handleCancelClicked() {
	    System.exit(0);
	}
	
}
