package nesridiscount.app.session;

/**
 * session utilisateur
 */
public class Session {
    private static String username;
    private static String password;

    private static Role role;

    /**
     * initialise la session
     * @param username
     * @param password
     * @param role
     */
    public static void initSession(String username,String password,int role){
        Session.username = username;
        Session.password = password;
        Session.role = Role.getById(role);
    }

    /**
     * 
     * @return le nom d'utilisateur
     */
    public String getUsername(){
        return Session.username;
    }

    /**
     * 
     * @return le mot de passe
     */
    public String getPassword(){
        return Session.password;
    }

    /**
     * 
     * @return le role
     */
    public Role getRole(){
        return Session.role;
    }

    public enum Role{
        Admin(1),
        Special(2),
        Inter(3);

        public final int roleId;

        private Role(int roleId){
            this.roleId = roleId;
        }

        /**
         * trouve un role
         * @param roleId
         * @return Role le role ou null
         */
        public static Role getById(int roleId){
            for(Role r : Role.values() ){
                if(r.roleId == roleId) return r;
            }

            return null;
        }
    };
}
