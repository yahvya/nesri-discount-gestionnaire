package nesridiscount.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nesridiscount.app.process.SearchMissionProcess;
import nesridiscount.app.ui_util.MissionForm;
import nesridiscount.app.ui_util.MissionSearchResult;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.app.util.FileExporter;
import nesridiscount.models.model.MissionsModel;

public class MissionCalendarController {
    private static final Alert failAlert = UiAlert.newAlert(AlertType.ERROR,"Echec d'actualisation","Une erreur s'est produite lors de l'actualisation du calendrier des missions");

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
    private VBox calendarResultContainer;

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

    private ArrayList<MissionSearchResult> results;

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
            UiAlert.newAlert(AlertType.ERROR,"Echec de création","Il n'y a aucune mission à créer actuellement").show();
            
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
                    AlertType.ERROR,
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
                    AlertType.ERROR,
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

        // rafrachissement du mois
        this.setResultFromDate();
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
        if(this.results.size() == 0){
            UiAlert.newAlert(AlertType.ERROR,"Echec d'export","Il n'y a aucun résultat à exporter").show();

            return;
        }

        // création du contenu csv
        String csvContent = MissionsModel.toStringHeader() + "\n";

        for(MissionSearchResult result : this.results) csvContent += result.getResultModel().toString() + "\n";

        UiAlert.newAlert(
            AlertType.INFORMATION,
            "Export des résultats",
            FileExporter.export(csvContent,"*.csv") ? 
                "Votre fichier a bien été sauvegardé" : 
                "Echec d'enregistrement du fichier").show();
    }

    @FXML
    void removeResults(MouseEvent event) {
        // suppression des précédents résultats
        this.results.forEach(result -> result.delete() );
        this.results.clear();
    }

    @FXML
    void initialize(){
        // affectation de l'année actuelle
        LocalDate currentDate = LocalDate.now();

        this.currentYear = currentDate.getYear();
        this.currentMonth = currentDate.getMonth();

        this.setResultFromDate();

        this.searchMissionsProcess = new SearchMissionProcess();

        this.searchMissionsProcess.setAfterEnd(() -> this.showMissionsSearchResult() );

        this.missions = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    /**
     * rafraichi les résultats en fonction de la date choisis
     */
    private void setResultFromDate(){        
        // récupération des missions du mois donné
        ArrayList<MissionsModel> results = MissionsModel.getMissionsOf(this.currentMonth,this.currentYear);

        if(results == null){
            MissionCalendarController.failAlert.show();
            
            return;
        }

        // suppression des résultats précédents et affichage des résultats
        try{    
            // récupération des résultats
            HashMap<Integer,ArrayList<MissionsModel> > daysMap = new HashMap<>();

            results.forEach(missionModel -> {
                int day = LocalDateTime.parse(missionModel.moment,MissionsModel.momentFormatter).getDayOfMonth();

                ArrayList<MissionsModel> dayResusltList = daysMap.get(day);

                if(dayResusltList == null){
                    dayResusltList = new ArrayList<>();

                    daysMap.put(day,dayResusltList);
                }

                dayResusltList.add(missionModel);
            });

            int countOfDaysInMonth = this.currentMonth.length(false);

            ObservableList<Node> children = this.calendarResultContainer.getChildren();
            
            children.clear();

            HBox daysLine = new HBox(30);

            children.add(daysLine);

            // affichage des jours du mois
            for(int dayNumber = 1; dayNumber <= countOfDaysInMonth; dayNumber++){
                ArrayList<MissionsModel> foundedMissions = daysMap.get(dayNumber);

                Label dayLabel = new Label(Integer.toString(dayNumber) );

                HBox resultBox = new HBox(20,dayLabel);

                if(foundedMissions != null){
                    Label seeLabel = new Label("Voir");

                    seeLabel.getStyleClass().add("result-see");
                    seeLabel.setTooltip(new Tooltip("Voir les mission de ce jour") );
                    seeLabel.setOnMouseClicked((e) -> {
                        // remplacement des résultats de recherche par les résultats actuelles
                        ArrayList<MissionsModel> currentSearchResults = this.searchMissionsProcess.getResults();

                        currentSearchResults.clear();
                        currentSearchResults.addAll(foundedMissions);

                        this.showMissionsSearchResult();

                        // scroll sur le haut de page
                        Controller.pane.setVvalue(0);
                    });

                    resultBox.getChildren().add(seeLabel);
                }

                resultBox.setPrefWidth(100);
                resultBox.getStyleClass().addAll("bordered","period-filter","good-result");

                daysLine.getChildren().add(resultBox);

                if(dayNumber % 7 == 0){
                    daysLine = new HBox(30);

                    children.add(daysLine);
                }
            }
        }
        catch(Exception e){
            MissionCalendarController.failAlert.show();

            return;
        }

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
        this.removeResults(null);

        ObservableList<Node> children = this.resultContainer.getChildren();

        // création et affichage des résultats
        for(MissionsModel result : this.searchMissionsProcess.getResults() ){
            MissionSearchResult searchResult = new MissionSearchResult(this.resultContainer,result);

            searchResult.setToDoOnDelete(() -> this.results.remove(searchResult) );
            
            children.add(searchResult.createResult() );
            
            this.results.add(searchResult);
        }
    }
}