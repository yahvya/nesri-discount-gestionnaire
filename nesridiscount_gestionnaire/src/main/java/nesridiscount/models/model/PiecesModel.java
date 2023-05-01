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
    public String internalRef;

    @Column(linkedCol = "location")
    public String location;

    public PiecesModel() throws Exception{
        super();
    }

    public PiecesModel(int id, int quantity, String pieceName, String enterpriseName, String externalRef, String internalRef, String location) throws Exception{
        super();
        
        this.id = id;
        this.quantity = quantity;
        this.pieceName = pieceName;
        this.enterpriseName = enterpriseName;
        this.externalRef = externalRef;
        this.internalRef = internalRef;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getPieceName() {
        return this.pieceName;
    }

    public String getEnterpriseName() {
        return this.enterpriseName;
    }

    public String getExternalRef() {
        return this.externalRef;
    }

    public String getInternalRef() {
        return this.internalRef;
    }

    public String getLocation() {
        return this.location;
    }
}
