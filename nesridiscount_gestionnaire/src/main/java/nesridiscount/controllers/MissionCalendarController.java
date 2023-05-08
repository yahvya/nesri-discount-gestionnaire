package nesridiscount.controllers;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MissionCalendarController {

    @FXML
    private VBox container;

    @FXML
    private VBox calendarContainer;

    @FXML
    private Label monthlabel;

    @FXML
    private VBox newMissionsContainer;

    @FXML
    private VBox resultContainer;

    @FXML
    private Label resultDataLabel;

    @FXML
    private TextField searchbar;

    @FXML
    private Label yearLabel;

    private Month currentMonth;

    private int currentYear;

    @FXML
    void searchMission(MouseEvent event) {

    }

    @FXML
    void createNewMissions(MouseEvent event){

    }

    @FXML
    void addNewMissions(MouseEvent event){
        
    }

    @FXML
    void showNextMonth(MouseEvent event) {
        try{
            Month nextMonth = Month.of(currentMonth.getValue() + 1);

            this.currentMonth = nextMonth;
        }
        catch(Exception e){
            this.currentMonth = Month.of(1);
        }

        this.setResultFromDate();
    }

    @FXML
    void showPreviousMonth(MouseEvent event) {
        try{
            Month previousMonth = Month.of(currentMonth.getValue() - 1);

            this.currentMonth = previousMonth;
        }
        catch(Exception e){
            this.currentMonth = Month.of(12);
        }

        this.setResultFromDate();
    }

    @FXML
    void showNextYear(MouseEvent event) {
        this.currentYear++;

        this.setResultFromDate();
    }

    @FXML
    void showPreviousYear(MouseEvent event) {
        this.currentYear--;

        this.setResultFromDate();
    }

    @FXML
    void exportResults(MouseEvent event) {

    }

    @FXML
    void removeResults(MouseEvent event) {

    }

    @FXML
    void initialize(){
        // affectation de l'année actuelle
        LocalDate currentDate = LocalDate.now();

        this.currentYear = currentDate.getYear();
        this.currentMonth = currentDate.getMonth();

        this.setResultFromDate();

        this.container.getChildren().remove(this.resultContainer);
    }

    /**
     * rafraichi les résultats en fonction de la date choisis
     */
    private void setResultFromDate(){
        String month = this.currentMonth.getDisplayName(TextStyle.FULL,Locale.FRANCE).toUpperCase();
        String year = Integer.toString(this.currentYear);

        this.yearLabel.setText(year);
        this.monthlabel.setText(month);

        this.resultDataLabel.setText(String.join(" ","Résultats",month,year) );
    }
}
