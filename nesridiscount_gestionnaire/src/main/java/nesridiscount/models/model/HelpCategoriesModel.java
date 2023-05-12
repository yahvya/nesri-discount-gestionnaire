package nesridiscount.models.model;

import nesridiscount.models.util.Column;
import nesridiscount.models.util.Table;

@Table("help_categories")
public class HelpCategoriesModel extends Model{
    @Column(linkedCol = "id",isAutoIncrement = true)
    private int id;

    @Column(linkedCol = "category_name",isAutoIncrement = false)
    public String categoryName;

    @Column(linkedCol = "help_content",isAutoIncrement =  false)
    public String helpContent;

    public HelpCategoriesModel() throws Exception{
        super();
    }

    public HelpCategoriesModel(String categoryName,String helpContent) throws Exception{
        super();

        this.categoryName = categoryName;
        this.helpContent = helpContent;
    }
}   
