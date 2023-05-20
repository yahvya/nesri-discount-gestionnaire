package nesridiscount.app.util;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * écrit le fichier json en le hashant
 */
public class JsonWriter {
    private static int SECRET = 3;

    /**
     * lie l'objet json
     * @param link
     * @return l'objet ou null
     */
    public static JSONObject readObject(String link){
        try{
            String content = Files.readString(Path.of(link) );

            // recherche et remplacement des chaines
            Matcher matches = Pattern.compile("\"[^\"]*\"").matcher(content);

            while(matches.find() ){
                String match = matches.group();

                match = match.substring(1,match.length() - 1);

                content = content.replace(match,JsonWriter.quickDecrypt(match) );
            }
            
            return (JSONObject) new JSONParser().parse(content);
        }
        catch(Exception e){}

        return null;
    }

    /**
     * écris le fichier json dans le fichier donné
     * @param jsonData
     * @param filePath
     * @return
     */
    public static boolean writeObject(JSONObject jsonData,String link){
        FileWriter writer = null;
        
        try{
            writer = new FileWriter(new File(link) );

            String jsonString = jsonData.toJSONString();

            // recherche et remplacement des chaines
            Matcher matches = Pattern.compile("\"[^\"]*\"").matcher(jsonString);

            while(matches.find() ){
                String match = matches.group();

                match = match.substring(1,match.length() - 1);

                jsonString = jsonString.replace(match,JsonWriter.quickEncrypt(match) );
            }

            writer.write(jsonString);
        }
        catch(Exception e){}
        finally{
            if(writer != null){
                try{
                    writer.close();
                }
                catch(Exception e){}
            }
        }
        
        return false;
    }

    /**
     * crypte la chaine
     * @param toChange
     * @return la chaine crypté
     */
    private static String quickEncrypt(String toChange){
        char chars[] = toChange.toCharArray();

        for(int i = 0; i < chars.length; i++) chars[i] += JsonWriter.SECRET;

        return new String(chars);
    }

    /**
     * décrypte la chaine passé
     * @param toChange
     * @return la chaine décrypté
     */
    private static String quickDecrypt(String toChange){
        char chars[] = toChange.toCharArray();

        for(int i = 0; i < chars.length; i++) chars[i] -= JsonWriter.SECRET;

        return new String(chars);
    }
}
