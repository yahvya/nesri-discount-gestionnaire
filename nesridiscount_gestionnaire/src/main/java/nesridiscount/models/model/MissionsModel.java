package nesridiscount.models.model;

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


    public MissionsModel() throws Exception {
        super();
    }

    public MissionsModel(String description, String moment, String technician) throws Exception{
        super();

        this.description = description;
        this.moment = moment;
        this.technician = technician;
    }
}
