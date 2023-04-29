package nesridiscount.models.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import nesridiscount.models.model.Model;

/**
 * informations sur une colonne
 */
public class ColumnManager{
    private Field linkedField;

    private Model linkedModel;

    private Column columnAttribute;
    
    public ColumnManager(Field field,Column columnAttribute,Model linkedModel){
        this.linkedField = field;
        this.linkedModel = linkedModel;
        this.columnAttribute = columnAttribute;
    }

    /**
     * 
     * @return le nom de la colonne lié en base de donnée
     */
    public String getLinkedColName(){
        return this.columnAttribute.linkedCol();
    }

    /**
     * appelle le setter de la colonne lié avec la donnée présente dans le résultat
     * @param data
     * @return si le set réussi
     */
    public boolean setFieldFrom(ResultSet data){
        try{
            this.linkedField.set(this.linkedModel,data.getObject(this.columnAttribute.linkedCol() ) );

            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
