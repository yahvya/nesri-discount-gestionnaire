package nesridiscount.models.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import nesridiscount.App;
import nesridiscount.models.util.Column;
import nesridiscount.models.util.ColumnManager;
import nesridiscount.models.util.Condition;
import nesridiscount.models.util.Table;

/**
 * gestion des modèles
 */
public abstract class Model {
    private static Connection con = null;

    private String tableName;

    private HashMap<String,ColumnManager> columnsManagers;

    public Model() throws Exception{
        try{
            // récupération des informations sur la table
            Class<? extends Model> thisClass = this.getClass();

            this.tableName = thisClass.getAnnotation(Table.class).value();

            Field[] fields = thisClass.getFields();

            this.columnsManagers = new HashMap<>();

            for(Field f : fields){
                Column columnAttribute = f.getAnnotation(Column.class);

                if(columnAttribute != null) this.columnsManagers.put(f.getName(),new ColumnManager(f,columnAttribute,this) );
            }
        }
        catch(Exception e){
            throw new Exception();
        }
    }

    /**
     * cherche des enfants 
     * @param map
     * @return le modèle crée ou null
     */
    public static Model findOneBy(Class<? extends Model> toGet,Condition<?>[] conditions){
        try{
            Model model = toGet.getConstructor().newInstance();

            ResultSet results = Model.getResults("select *,count(*) as countOfResults from " + model.tableName,conditions,model);

            if(results.getInt("countOfResults") == 0) return null;

            model.columnsManagers.forEach((key,manager) -> manager.setFieldFrom(results) );

            return model;
        }
        catch(Exception e){}
        
        return null;
    }   

    /**
     * recherche plus lignes
     * @param toGet
     * @param conditions
     * @return liste des model trouvé
     */
    public static ArrayList<? extends Model> find(Class<? extends Model> toGet,Condition<?>[] conditions){
        ArrayList<Model> resultsList = new ArrayList<>();

        try{
            Constructor<? extends Model> constructor = toGet.getConstructor();

            Model model = constructor.newInstance();

            ResultSet results = Model.getResults("select * from " + model.tableName,conditions,model);

            // création des modèles
            while(results.next() ){
                Model newModel = constructor.newInstance();

                newModel.columnsManagers.forEach((key,manager) -> manager.setFieldFrom(results) );

                resultsList.add(newModel);
            }
        }
        catch(Exception e){}

        return resultsList;
    }   

    /**
     * 
     * @return la connexion 
     * @throws Exception si connexion null
     */
    protected static Connection getConnection() throws Exception{
        if(Model.con == null) Model.con = Model.createConnection();

        if(Model.con == null) throw new Exception();

        return Model.con;
    }

    /**
     * prépare et renvoi une requete
     * @param request
     * @return la liste des résultats
     * @throws Exception e
     */
    private static ResultSet getResults(String request,Condition<?>[] conds,Model fromModel) throws Exception{
        try{
            Connection con = Model.getConnection();

            HashMap<String,ColumnManager> managers = fromModel.columnsManagers;

            if(conds.length != 0) request += " where ";

            for(Condition<?> cond : conds) request += cond.getConditionSql(managers) + " ";

            PreparedStatement query = con.prepareStatement(request);

            int index = 1;

            // création de la requete et bind des valeurs
            for(Condition<?> cond : conds){
                cond.treatConditionOnQuery(query,index);

                index++;
            }

            ResultSet results = query.executeQuery();

            return results;
        }
        catch(Exception e){
            throw new Exception();
        }
    }

    /**
     * crée une connexion
     * @return la connexion ou null
     */
    private static Connection createConnection(){
        try{
            return DriverManager.getConnection(Model.getDatabaseUrl() );
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * 
     * @return lien menant au fichier de base de données
     * @throws Exception
     */
    private static String getDatabaseUrl() throws Exception{
        URL url = App.loadResource("/documents/db.sqlite");

        if(url == null) throw new Exception();

        return "jdbc:sqlite:" + url.toString();
    }
}
