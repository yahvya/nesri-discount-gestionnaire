package nesridiscount.models.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

import nesridiscount.models.util.Column;
import nesridiscount.models.util.Table;

@Table("users")
public class UsersModel extends Model{
    @Column(isAutoIncrement = true,linkedCol = "id")
    public int id;

    @Column(isAutoIncrement = false,linkedCol = "username")
    public String username;

    @Column(isAutoIncrement = false,linkedCol = "password")
    public String password;

    @Column(isAutoIncrement = false,linkedCol = "role")
    public int role;

    public UsersModel() throws Exception{
        super();
    }

    public UsersModel(String username, String password, int role) throws Exception {
        super();
        
        this.username = username;
        this.role = role;
        this.setPassword(password);
    }

    /**
     * vérifie le mot de passe hashé
     * @param password
     * @return
     */
    public boolean checkPassword(String password){
        try{
            return BCrypt.checkpw(password, this.password);
        }
        catch(Exception e){
            return false;
        }
    }

    public UsersModel setPassword(String password){
        this.password = BCrypt.hashpw(password,BCrypt.gensalt() );
        
        return this;
    }
}
