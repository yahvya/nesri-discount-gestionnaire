package nesridiscount.app.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.TableView;
import nesridiscount.App;
import nesridiscount.models.model.Model;
import nesridiscount.models.model.PiecesModel;
import nesridiscount.models.util.Condition;
import nesridiscount.models.util.Condition.Separator;
import nesridiscount.models.util.Condition.Type;

/**
 * processus de recherche de pièces
 */
public class SearchPiecesProcess extends Process{
    /**
     * délai de frappe
     */
    private static int SEARCH_DELAY = 600;

    private TableView<PiecesModel> resultArray; 

    private Timer timer = null;

    private HashMap<PieceFilter,Boolean> filters;

    private String lastSearch = null;

    private int countOfActiveElements;

    public SearchPiecesProcess(TableView<PiecesModel> resultArray){
        this.resultArray = resultArray;

        App.registerAction(this,() -> {
            if(this.timer != null) this.timer.cancel(); 
        });

        this.initFilters();
    }

    /**
     * vide les résultats précédents
     */
    @Override
    public Process startProcess() throws Exception {
        this.resultArray.getItems().clear();

        return this;
    }

    /**
     * fais la recherche
     */
    public Process doProcess(String toSearch) throws Exception {
        if(this.timer != null){
            this.timer.cancel();
            this.timer = null;
        }

        if(toSearch.length() != 0){
            this.timer = new Timer();

            SearchPiecesProcess reference = this;

            final String search = "%" + toSearch + "%";

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    reference.timer.cancel();
                    reference.timer = null;

                    Condition<?>[] conditions = new Condition[reference.countOfActiveElements];

                    // création des conditions
                    int index = 0;
                    
                    for(Map.Entry<PieceFilter,Boolean> entry : reference.filters.entrySet() ){
                        if(entry.getValue() == true){
                            conditions[index] = SearchPiecesProcess.getConditionFromFilter(entry.getKey(),search);

                            index++;
                        }
                    }

                    // modification du séparateur de suite de la dernière condition
                    conditions[reference.countOfActiveElements - 1].setSeparator(Separator.NULL);

                    @SuppressWarnings("unchecked")
                    ArrayList<PiecesModel> results = (ArrayList<PiecesModel>) Model.find(PiecesModel.class,conditions);
                        reference.lastSearch = search;

                    // éxécution dans le thread de la vue
                    Platform.runLater(() -> {
                        try{
                            reference.endProcess(results);
                        }
                        catch(Exception e){}
                    });
                }
            },SearchPiecesProcess.SEARCH_DELAY);
        }
        
        return this;
    }

    /**
     * affiche les résultats
     */
    public Process endProcess(ArrayList<PiecesModel> results) throws Exception {
        this.resultArray.getItems().addAll(results);
        
        return super.endProcess();
    }

    /**
     * lance la recherche
     * @param search
     * @return this
     * @throws Exception
     */
    public Process execEntireProcess(String search) throws Exception {
        this.startProcess();
        this.doProcess(search);
        
        return this;
    }

    /**
     * désactive un filtre et met à jour les résultats
     * @param toDisable
     * @return this
     * @throws Exception
     */
    public SearchPiecesProcess disableFilter(PieceFilter toDisable) throws Exception{
        this.setFilterState(toDisable,false);

        return this;
    }

    /**
     * active un filtre et met à jour les résultats
     * @param toActive
     * @return this
     * @throws Exception
     */
    public SearchPiecesProcess activeFilter(PieceFilter toActive) throws Exception{
        this.setFilterState(toActive,true);

        return this;
    }

    /**
     * défini l'état d'activation d'un filtre
     * @param filter
     * @param state
     * @return this
     * @throws Exception
     */
    public SearchPiecesProcess setFilterState(PieceFilter filter,boolean state) throws Exception{
        // vérification si un réel changement est fait
        if(this.filters.get(filter) != state){
            this.filters.put(filter,state);

            // mise à jour de nombre d'élements active
            this.countOfActiveElements += state == false ? -1 : 1;

            // relance la dernière recherche faites avec les nouveaux filtres
            if(this.lastSearch != null) this.execEntireProcess(this.lastSearch);
        }

        return this;
    }

    /**
     * initie les filtres par défaut
     */
    private void initFilters(){
        this.filters = new HashMap<>(Map.of(
            PieceFilter.NAME,true,
            PieceFilter.INTERNAL_REF,true,
            PieceFilter.ENTERPRISE_NAME,true,
            PieceFilter.ENTERPRISE_REF,true
        ) );

        this.countOfActiveElements = 4;
    }

    /**
     * créer une condition à partir d'un filtre
     * @param filter le fitlre
     * @param toSearch la recherche
     * @return la condition crée ou null en cas d'erreur interne
     */
    private static Condition<?> getConditionFromFilter(PieceFilter filter,String toSearch){
        switch(filter){
            case NAME:
                return new Condition<String>("pieceName",toSearch,Type.LIKE,Separator.OR);
            
            case INTERNAL_REF:
                return new Condition<String>("internalRef",toSearch,Type.LIKE,Separator.OR);
            
            case ENTERPRISE_REF:
                return new Condition<String>("externalRef",toSearch,Type.LIKE,Separator.OR);
            
            case ENTERPRISE_NAME:
                return new Condition<String>("enterpriseName",toSearch,Type.LIKE,Separator.OR);

            default: 
                return null;
        }
    }

    /**
     * filtres de recherche des pièces
     */
    public enum PieceFilter{
        NAME,
        INTERNAL_REF,
        ENTERPRISE_REF,
        ENTERPRISE_NAME,
    };
}
