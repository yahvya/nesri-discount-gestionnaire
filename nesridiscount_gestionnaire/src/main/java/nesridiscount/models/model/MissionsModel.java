package nesridiscount.models.model;

import java.time.format.DateTimeFormatter;

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
}
