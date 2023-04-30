package nesridiscount.app.session;

import java.io.File;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.json.simple.JSONObject;

import nesridiscount.App;
import nesridiscount.app.util.JsonWriter;

/**
 * session utilisateur
 */
public class Session {
    private static String SESSION_SAVE_FILE = "session.json";

    /**
     * une semaine en minutes
     */
    private static int ALLOWED_TIME = 60 * 24 * 7;

    private static String username;
    private static String password;

    private static Role role;

    /**
     * initialise la session
     * @param username
     * @param password
     * @param keepAlive si la session doit être gardé
     * @param role
     */
    public static void initSession(String username,String password,int role,boolean keepAlive){
        Session.username = username;
        Session.password = password;
        Session.role = Role.getById(role);

        if(keepAlive) Session.save();
    }

    /**
     * sauvegarde la session
     */
    public static void save(){
        JSONObject saveObject = new JSONObject(Map.of(
            "username",Session.username,
            "password",Session.password,
            "role",Session.role.roleId,
            "loginTime", Timestamp.from(Instant.now() ).toString()
        ) );

        try{
            // sauvegarde du fichier
            JsonWriter.writeObject(saveObject,App.loadResource("/documents/").toURI().toString() + Session.SESSION_SAVE_FILE);
        }
        catch(Exception e){}
    }

    /**
     * tente de charger la session à partir du fichier de session
     * @return si la session est chargé
     */
    public static boolean loadSession(){
        try{
            String link = App.loadResource("/documents/" + Session.SESSION_SAVE_FILE).toURI().toString();

            JSONObject sessionObject = JsonWriter.readObject(link);
            
            Timestamp loginTime = Timestamp.valueOf((String) sessionObject.get("loginTime") );

            Timestamp now = Timestamp.from(Instant.now() );

            // vérifie si la limite est passé
            if(loginTime.before(now) && ChronoUnit.MINUTES.between(loginTime.toLocalDateTime(),now.toLocalDateTime() ) >= Session.ALLOWED_TIME){
                new File(new URI(link) ).delete();
            }
            else{
                // création de la session
                Session.username = (String) sessionObject.get("username");
                Session.password = (String) sessionObject.get("password");
                Session.role = Role.getById(((Long) sessionObject.get("role") ).intValue() );

                return true;
            }
        }
        catch(Exception e){System.out.println(e.getMessage());}

        return false;
    }

    /**
     * 
     * @return le nom d'utilisateur
     */
    public static String getUsername(){
        return Session.username;
    }

    /**
     * 
     * @return le mot de passe
     */
    public static String getPassword(){
        return Session.password;
    }

    /**
     * 
     * @return le role
     */
    public static Role getRole(){
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
