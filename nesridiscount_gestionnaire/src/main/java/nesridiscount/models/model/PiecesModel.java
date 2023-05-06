package nesridiscount.models.model;

import nesridiscount.models.util.Column;
import nesridiscount.models.util.Table;

@Table("pieces")
public class PiecesModel extends Model{
    @Column(isAutoIncrement = true,linkedCol = "id")
    public int id;

    @Column(isAutoIncrement = false,linkedCol = "quantity")
    public int quantity;   

    @Column(isAutoIncrement = false,linkedCol = "piece_name")
    public String pieceName;

    @Column(isAutoIncrement = false,linkedCol = "enterprise_name")
    public String enterpriseName;

    @Column(isAutoIncrement = false,linkedCol = "external_ref")
    public String externalRef;

    @Column(isAutoIncrement = false,linkedCol = "internal_ref")
    public String internalRef;

    @Column(isAutoIncrement = false,linkedCol = "location")
    public String location;

    public PiecesModel() throws Exception{
        super();
    }

    public PiecesModel(int quantity, String pieceName, String enterpriseName, String externalRef, String internalRef, String location) throws Exception{
        super();
        
        this.quantity = quantity;
        this.pieceName = pieceName;
        this.enterpriseName = enterpriseName;
        this.externalRef = externalRef;
        this.internalRef = internalRef;
        this.location = location;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPieceName(String pieceName) {
        this.pieceName = pieceName;
    }
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }
    public void setInternalRef(String internalRef) {
        this.internalRef = internalRef;
    }
    public void setLocation(String location) {
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

    @Override
    public String toString(){
        return String.join(",",
            Integer.toString(this.id),
            Integer.toString(this.quantity),
            this.pieceName,
            this.internalRef,
            this.externalRef,
            this.enterpriseName,
            this.location
        );
    }

    public static String toStringHeader(){
        return String.join(",",
            "Id",
            "Quantité",
            "Nom de la pièce",
            "Référence interne",
            "Référence fabriquant",
            "Nom fabriquant",
            "Emplacement"
        );
    }
}
