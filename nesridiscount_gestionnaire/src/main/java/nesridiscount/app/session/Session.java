package nesridiscount.app.session;

import java.io.File;
import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.json.simple.JSONObject;

import nesridiscount.App;
import nesridiscount.app.util.JsonWriter;
import nesridiscount.models.model.Model;
import nesridiscount.models.model.UsersModel;
import nesridiscount.models.util.Condition;
import nesridiscount.models.util.Condition.Separator;

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
    private static String loginTime;

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
        
        Timestamp now = Timestamp.from(Instant.now() );

        Session.loginTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(now.getTime() ) );

        if(keepAlive) Session.save();
    }

    /**
     * sauvegarde la session
     */
    public static void save(){
        Timestamp now = Timestamp.from(Instant.now() );

        JSONObject saveObject = new JSONObject(Map.of(
            "username",Session.username,
            "password",Session.password,
            "loginTime", now.toString()
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
            JSONObject sessionObject = JsonWriter.readObject(App.loadResource("/documents/" + Session.SESSION_SAVE_FILE).toURI().toString() );
            
            Timestamp loginTime = Timestamp.valueOf((String) sessionObject.get("loginTime") );

            Timestamp now = Timestamp.from(Instant.now() );

            // vérifie si la limite est passé
            if(loginTime.before(now) && ChronoUnit.MINUTES.between(loginTime.toLocalDateTime(),now.toLocalDateTime() ) >= Session.ALLOWED_TIME){
                Session.loggout();

                return false;
            }
            else{
                // création de la session
                Session.username = (String) sessionObject.get("username");
                Session.password = (String) sessionObject.get("password");
                Session.loginTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(loginTime.getTime() ) );

                Model foundedModel = Model.findOneBy(UsersModel.class,new Condition[]{
                    new Condition<>("username",Session.username,Separator.NULL)
                });

                if(foundedModel != null){
                    Session.role = Role.getById(((UsersModel) foundedModel).role );

                    return true;
                }
                else Session.loggout();
            }
        }
        catch(Exception e){}

        return false;
    }

    /**
     * déconnecte l'utilisateur, supprime la session sauvegardé
     * @return si la déconnexion a réussi
     */
    public static boolean loggout(){
        try{
            return new File(new URI(App.loadResource("/documents/" + Session.SESSION_SAVE_FILE).toURI().toString() ) ).delete();
        }
        catch(Exception e){
            return false;
        }
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
     * @return l'heure de la dernière connexion
     */
    public static String getLoginTime(){
        return Session.loginTime;
    }

    /**
     * 
     * @return le role
     */
    public static Role getRole(){
        return Session.role;
    }

    public enum Role{
        /**
         * super administrateur
         */
        Admin(1),
        /**
         * administrateur
         */
        Special(2),
        /**
         * stagiaire
         */
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
