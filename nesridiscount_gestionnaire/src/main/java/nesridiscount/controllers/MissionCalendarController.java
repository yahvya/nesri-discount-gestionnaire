package nesridiscount.controllers;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nesridiscount.app.process.SearchMissionProcess;
import nesridiscount.app.ui_util.MissionForm;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.models.model.MissionsModel;

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

    private SearchMissionProcess searchMissionsProcess;

    private ArrayList<MissionForm> missions;

    @FXML
    void searchMission(MouseEvent event) {
        try{
            this.searchMissionsProcess
                .setSearch(this.searchbar.getText() )
                .execEntireProcess();
        }
        catch(Exception e){}
    }

    @FXML
    void createNewMissions(MouseEvent event){
        if(this.missions.size() == 0){
            UiAlert.newAlert(AlertType.INFORMATION,"Echec de création","Il n'y a aucune mission à créer actuellement").show();
            
            return;
        }

        ArrayList<MissionsModel> toCreate = new ArrayList<>(); 

        int index = 1;

        // récupération des model
        for(MissionForm form : this.missions){
            MissionsModel missionModel = form.getMission();

            index++;

            if(missionModel == null){
                UiAlert.newAlert(
                    AlertType.INFORMATION,
                    "Echec de création",
                    "La mission numéro (" + Integer.toString(index) + ") est mal formé"
                ).show();
                
                continue;
            }

            toCreate.add(missionModel);
            
            form.delete();
        }

        for(MissionsModel model : toCreate){
            if(!model.create() ){
                UiAlert.newAlert(
                    AlertType.INFORMATION,
                    "Echec de création",
                    String.join(" ",
                        "Une erreur technique s'est produite lors de la mission (",
                        model.technician,
                        "-",
                        model.moment,
                        ")"
                    )
                ).show();

                index++;
            }
        }
    }

    @FXML
    void addNewMissions(MouseEvent event){
        MissionForm form = new MissionForm(this.newMissionsContainer);

        form.setToDoOnDelete(() -> this.missions.remove(form) );

        this.missions.add(0,form);

        this.newMissionsContainer.getChildren().add(form.createForm() );
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

        this.searchMissionsProcess = new SearchMissionProcess();

        this.searchMissionsProcess.setAfterEnd(() -> this.showMissionsSearchResult() );

        this.missions = new ArrayList<>();
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

    /**
     * affiche les résultats de la recherche
     */
    private void showMissionsSearchResult(){
        ArrayList<MissionsModel> results = this.searchMissionsProcess.getResults();

        System.out.println("affichage des résultats");

        for(MissionsModel resultModel : results) System.out.println(resultModel.description);
    }
}
