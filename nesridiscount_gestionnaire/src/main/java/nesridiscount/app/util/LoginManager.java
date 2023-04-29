package nesridiscount.app.util;

import nesridiscount.app.session.Session;
import nesridiscount.models.model.Model;
import nesridiscount.models.model.UsersModel;
import nesridiscount.models.util.Condition;
import nesridiscount.models.util.Condition.Separator;

/**
 * processus de connexion
 */
public class LoginManager {
    /**
     * tente de créer la session utilisateur à partir des données envoyés
     * @param username
     * @param password
     * @return si la session est crée
     */
    public boolean crateSessionFrom(String username,String password){
        Model foundedModel = Model.findOneBy(UsersModel.class,new Condition[]{
            new Condition<String>("username",username,Separator.NULL)
        });

        if(foundedModel != null){
            UsersModel userModel = (UsersModel) foundedModel;

            if(userModel.checkPassword(password) ){
                Session.initSession(userModel.username,userModel.password,userModel.role);

                return true;
            }
        }

        return false;
    }
}   
