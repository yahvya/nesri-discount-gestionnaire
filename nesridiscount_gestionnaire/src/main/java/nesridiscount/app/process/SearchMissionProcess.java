package nesridiscount.app.process;

import java.util.ArrayList;

import nesridiscount.models.model.MissionsModel;
import nesridiscount.models.model.Model;
import nesridiscount.models.util.Condition;
import nesridiscount.models.util.Condition.Separator;
import nesridiscount.models.util.Condition.Type;

/**
 * processus de recherche des missions
 */
public class SearchMissionProcess extends Process{
    private static final int COUNT_OF_CONDITIONS_PER_PART = 3;
    
    private String search = "";

    private Condition<?> conditions[];

    private ArrayList<MissionsModel> results = new ArrayList<>();

    @Override
    public Process startProcess() throws Exception{
        // construction des différentes parties de la requête
        String[] searchSplit = this.search.split("\\s");

        if(searchSplit.length == 0) return this;

        this.conditions = new Condition[searchSplit.length * COUNT_OF_CONDITIONS_PER_PART];

        int index = 0;

        // ajout des conditions
        for(String part : searchSplit){
            String like = "%" + part + "%";

            this.conditions[index] = new Condition<String>("technician",like,Type.LIKE,Separator.OR);
            this.conditions[index + 1] = new Condition<String>("description",like,Type.LIKE,Separator.OR);
            this.conditions[index + 2] = new Condition<String>("moment",like,Type.LIKE,Separator.OR);
            
            index += COUNT_OF_CONDITIONS_PER_PART;
        }

        this.conditions[index - 1].setSeparator(Separator.NULL);
        
        return super.startProcess();
    }

    @Override
    public Process doProcess() throws Exception {
        // éxécution de la requête et récupération des résultats
        @SuppressWarnings("unchecked")
        ArrayList<MissionsModel> results = (ArrayList<MissionsModel>) Model.find(MissionsModel.class,this.conditions);

        this.results = results;

        return super.doProcess();
    }

    @Override
    public Process endProcess() throws Exception {
        this.afterEnd.toDoAfter();
        
        return this;
    }

    public SearchMissionProcess setSearch(String search){
        this.search = search;

        return this;
    }

    public ArrayList<MissionsModel> getResults(){
        return this.results;
    }
}
