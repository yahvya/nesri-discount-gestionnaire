package nesridiscount.models.model;

import nesridiscount.models.util.Column;
import nesridiscount.models.util.Table;

@Table("pieces")
public class PiecesModel extends Model{
    @Column(linkedCol = "id")
    public int id;

    @Column(linkedCol = "quantity")
    public int quantity;   

    @Column(linkedCol = "piece_name")
    public String pieceName;

    @Column(linkedCol = "enterprise_name")
    public String enterpriseName;

    @Column(linkedCol = "external_ref")
    public String externalRef;

    @Column(linkedCol = "internal_ref")
    public String interalRef;

    @Column(linkedCol = "location")
    public String location;

    public PiecesModel() throws Exception{
        super();
    }

    public PiecesModel(int id, int quantity, String pieceName, String enterpriseName, String externalRef, String interalRef, String location) throws Exception{
        super();
        
        this.id = id;
        this.quantity = quantity;
        this.pieceName = pieceName;
        this.enterpriseName = enterpriseName;
        this.externalRef = externalRef;
        this.interalRef = interalRef;
        this.location = location;
    }

}
