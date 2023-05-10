package nesridiscount.models.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import nesridiscount.models.util.Column;
import nesridiscount.models.util.Table;

@Table("missions")
public class MissionsModel extends Model{
    @Column(linkedCol = "id",isAutoIncrement = true)
    public int id;

    @Column(linkedCol = "description",isAutoIncrement = false)
    public String description;

    @Column(linkedCol = "moment",isAutoIncrement = false)
    public String moment;

    @Column(linkedCol = "technician",isAutoIncrement = false)
    public String technician;

    public static final DateTimeFormatter momentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MissionsModel() throws Exception {
        super();
    }

    public MissionsModel(String description, String moment, String technician) throws Exception{
        super();

        this.description = description;
        this.moment = moment;
        this.technician = technician;
    }

    @Override
    public String toString(){
        return String.join(",",
            Integer.toString(this.id),
            this.technician,
            this.moment,
            this.description
        );
    } 

    public static String toStringHeader(){
        return String.join(",",
            "Id",
            "Technicien",
            "Date de la mission",
            "Description"
        );
    }

    /**
     * recherche les missions du mois et de l'année donné
     * @param month
     * @param year
     * @return les résultats de la requête sous forme de model ou null en cas d'échec
     */
    public static ArrayList<MissionsModel> getMissionsOf(Month month,int year){
        try{    
            MissionsModel utilModel = new MissionsModel();

            String request = "select * from " + utilModel.tableName + " where strftime('%m',moment) = ? and strftime('%Y',moment) = ?"; 

            Connection connection = Model.getConnection();

            PreparedStatement query = connection.prepareStatement(request);

            String monthStr = Integer.toString(month.getValue() );

            if(monthStr.length() == 1) monthStr = "0" + monthStr;

            query.setString(1,monthStr);
            query.setString(2,Integer.toString(year) );

            ResultSet results = query.executeQuery();

            ArrayList<MissionsModel> resultsModel = new ArrayList<>();

            while(results.next() ){
                MissionsModel model = new MissionsModel(
                    results.getString("description"),
                    results.getString("moment"),
                    results.getString("technician")
                );

                model.id = results.getInt("id");

                resultsModel.add(model);
            }

            return resultsModel;
        }
        catch(Exception e){System.out.println(e.getMessage() );}
        
        return null;
    }
}
