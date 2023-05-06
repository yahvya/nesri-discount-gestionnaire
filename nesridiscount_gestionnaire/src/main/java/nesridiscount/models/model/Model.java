package nesridiscount.models.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

    private int countOfAutoIncrementedElements = 0;

    public Model() throws Exception{
        try{
            // récupération des informations sur la table
            Class<? extends Model> thisClass = this.getClass();

            this.tableName = thisClass.getAnnotation(Table.class).value();

            Field[] fields = thisClass.getFields();

            this.columnsManagers = new HashMap<>();

            for(Field f : fields){
                Column columnAttribute = f.getAnnotation(Column.class);

                if(columnAttribute != null){
                    this.columnsManagers.put(f.getName(),new ColumnManager(f,columnAttribute,this) );
                    
                    if(columnAttribute.isAutoIncrement() ) this.countOfAutoIncrementedElements++;
                }
            }
        }
        catch(Exception e){
            throw new Exception();
        }
    }

    /**
     * met à jour le model en base de donnée
     * @param where les clés attributs de conditions à utilisé
     * @return si la mise à jour a réussi
     */
    public boolean update(String ...where){
        try{
            String request = "update " + this.tableName + " set ";

            Set<Entry<String, ColumnManager>> entries = this.columnsManagers.entrySet();

            for(Map.Entry<String,ColumnManager> entry : entries) request += entry.getValue().getLinkedColName() + "=?,";

            request = request.substring(0,request.length() - 1);

            if(where.length != 0){
                request += " where ";

                for(String attributeName : where) request += this.columnsManagers.get(attributeName).getLinkedColName() + " = ? and ";

                request = request.substring(0,request.length() - 4);
            }

            PreparedStatement preparedQuery = Model.getConnection().prepareStatement(request);

            int index = 1;

            // bind des valeurs à affecter
            for(Map.Entry<String,ColumnManager> entry : entries){
                Model.setValueInQuery(preparedQuery,entry.getValue().getField(),this,index);
                
                index++;
            }

            // bind des conditions where
            if(where.length != 0){
                for(String attributeName : where){
                    Model.setValueInQuery(preparedQuery,this.columnsManagers.get(attributeName).getField(),this,index);
                    
                    index++;
                }
            }

            preparedQuery.executeUpdate();

            return true;
        }   
        catch(Exception e){}

        return false;
    }

    /**
     * crée le model
     * @return si la création à réussi
     */
    public boolean create(){
        try{
            int countOfElements = this.columnsManagers.size() - this.countOfAutoIncrementedElements;

            String cols[] = new String[countOfElements];
            Object toInsert[] = new Object[countOfElements];

            int index = 0;

            for(ColumnManager manager : this.columnsManagers.values() ){
                if(!manager.getColumnAttribute().isAutoIncrement() ){
                    cols[index] = manager.getLinkedColName();
                    toInsert[index] = manager.getField().get(this);

                    index++;
                }
            }

            String sqlQuery = "insert into " + this.tableName + " (" + String.join(", ",cols) + ") values(" + String.join(",","?".repeat(countOfElements).split("") ) + ")";

            PreparedStatement query = Model.getConnection().prepareStatement(sqlQuery);

            index = 1;

            for(Object toAdd : toInsert){
                Class<?> toAddClass = toAdd.getClass();

                System.out.println(toAddClass);
                
                if(toAddClass == String.class)
                    query.setString(index,(String) toAdd);
                else if(toAddClass == Integer.class)
                    query.setInt(index,(Integer) toAdd);
                else if(toAddClass == Long.class || toAddClass == Double.class)
                    query.setBigDecimal(index,BigDecimal.valueOf((Double) toAdd) );
                else 
                    continue;
                
                index++;
            }

            query.executeUpdate();

            return true;
        }   
        catch(Exception e){
            return false;
        }

    }

    public HashMap<String,ColumnManager> getColumnsManagers(){
        return this.columnsManagers;
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
     * requete custom
     * @param toGet
     * @param query
     * @return les résultats ou null
     */
    public static ResultSet customResultQuery(Class<? extends Model>toGet,String query){
        try{
            Model model = toGet.getConstructor().newInstance();

            // remplacement du nom de la table
            query = query.replaceAll(toGet.getSimpleName(),model.tableName);

            // remplacement des attributs par non de colonne
            for(String key : model.columnsManagers.keySet() )
                query = query.replaceAll(key,model.columnsManagers.get(key).getLinkedColName() );

            return Model.getConnection().createStatement().executeQuery(query);
        }
        catch(Exception e){
            return null;
        }
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
     * ajoute la valeur dans la requete
     * @param query
     * @param toSetIn
     */
    private static void setValueInQuery(PreparedStatement query,Field toSetIn,Model linkedModel,int paramIndex){
        try{
            Object toSet = toSetIn.get(linkedModel);

            if(toSet instanceof Integer)
                query.setInt(paramIndex,(Integer) toSet);
            else if(toSet instanceof Long)
                query.setLong(paramIndex,(Long) toSet);
            else if(toSet instanceof String)
                query.setString(paramIndex,(String) toSet);
        }
        catch(Exception e){}
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
