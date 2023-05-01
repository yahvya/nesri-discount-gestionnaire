package nesridiscount.models.util;

import java.sql.PreparedStatement;
import java.util.HashMap;

/**
 * T type de la donnée de condition String Integer ou Long
 */
public class Condition<T> {
    private String linkedAttribute;
    
    private T data;
    
    private Type conditionType;
    
    private DataType dataType;
    
    private Separator separator; 

    /**
     * 
     * @param linkedAttribute le nom de l'attribut lié
     * @param data donné de condition
     * @param conditionType type de condition
     * @param separator séparateur sql
     */
    public Condition(String linkedAttribute,T data,Type conditionType,Separator separator){
        this.linkedAttribute = linkedAttribute;
        this.data = data;
        this.conditionType = conditionType;
        this.separator = separator;

        if(data instanceof Integer) 
            this.dataType = DataType.INT;
        else if(data instanceof Long)
            this.dataType = DataType.LONG;
        else 
            this.dataType = DataType.STRING;
    }

    /**
     * comparateur EQUAL par défaut
     * @param linkedAttribute le nom de l'attribut lié
     * @param data donné de condition
     * @param separator séparateur sql
     */
    public Condition(String linkedAttribute,T data,Separator separator){
        this(linkedAttribute,data,Type.EQUAL,separator);
    }

    /**
     * @param managers les gestionnaires de colonnes
     * @return la chaine sql de la condition
     */
    public String getConditionSql(HashMap<String,ColumnManager> managers){
        String condString = "";

        String columnName = managers.get(this.linkedAttribute).getLinkedColName();

        if(this.dataType == DataType.STRING){
            switch(this.conditionType){
                case LIKE: 
                    condString = columnName + " like ?";
                break;

                default:
                    condString = columnName + " = ?";
                break;
            }
        }
        else{
            switch(this.conditionType){
                case SUPERIOR: 
                    condString = columnName + " > ?";
                break;

                case INFERIOR: 
                    condString = columnName + " < ?";
                break;

                default:
                    condString = columnName + " = ?";
                break;
            }
        }

        switch(this.separator){
            case AND: condString += " and" ; break;
            case OR: condString += " or" ; break;
            default:;
        }

        return condString;
    }

    /**
     * ajoute la condition actuelle sur la requete donné
     * @param query
     * @param index
     */
    public void treatConditionOnQuery(PreparedStatement query,int index) throws Exception{

        if(this.dataType != DataType.STRING){
            switch(this.dataType){
                case INT:
                    query.setInt(index,(Integer) this.data);
                ; break;

                default:
                    query.setLong(index,(Long) this.data);
                ;
            }
        }
        else query.setString(index,(String) this.data);
    }

    /**
     * 
     * @param newSeparator
     * @return this
     */
    public Condition<T> setSeparator(Separator newSeparator){
        this.separator = newSeparator;

        return this;
    }

    public enum Type{
        EQUAL,
        LIKE,
        /**
         * la valeur en base de données est supérieure
         */
        SUPERIOR,
        /**
         * la valeur en base de donnée est inférieure
         */
        INFERIOR
    };

    public enum DataType{
        INT,
        LONG,
        STRING
    }

    public enum Separator{
        AND,
        OR,
        NULL
    }
}
