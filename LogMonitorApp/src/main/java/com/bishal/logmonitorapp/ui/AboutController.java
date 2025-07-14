package com.bishal.logmonitorapp.ui;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AboutController {

	@FXML private VBox aboutContainer;
	@FXML private Label titleLabel;
	@FXML private Label devLabel;
	@FXML private Label companyLabel;
	@FXML private Label versionLabel;
	@FXML private Label contactLabel;
	@FXML private Label websiteLabel;

	@FXML
	public void initialize() {
		animateLabels();
	}

	private void animateLabels() {
		Label[] labels = {
				titleLabel, devLabel, companyLabel, versionLabel, contactLabel, websiteLabel
		};

		SequentialTransition sequence = new SequentialTransition();

		for (Label label : labels) {
			label.setOpacity(0);
			TranslateTransition translate = new TranslateTransition(Duration.millis(250), label);
			translate.setFromY(20);
			translate.setToY(0);

			FadeTransition fade = new FadeTransition(Duration.millis(250), label);
			fade.setFromValue(0);
			fade.setToValue(1);

			SequentialTransition transition = new SequentialTransition(translate, fade);
			sequence.getChildren().add(transition);
		}

		sequence.play();
	}
}
