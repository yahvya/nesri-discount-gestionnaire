package nesridiscount.models.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

import nesridiscount.models.util.Column;
import nesridiscount.models.util.Table;

@Table("users")
public class UsersModel extends Model{
    @Column(linkedCol = "id")
    public int id;

    @Column(linkedCol = "username")
    public String username;

    @Column(linkedCol = "password")
    public String password;

    @Column(linkedCol = "role")
    public int role;

    public UsersModel() throws Exception{
        super();
    }

    public UsersModel(int id, String username, String password, int role) throws Exception {
        super();
        
        this.id = id;
        this.username = username;
        this.password = BCrypt.hashpw(password,BCrypt.gensalt() );
        this.role = role;
    }

    /**
     * vérifie le mot de passe hashé
     * @param password
     * @return
     */
    public boolean checkPassword(String password){
        return BCrypt.checkpw(password, this.password);
    }
}
