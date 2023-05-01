package nesridiscount.app.util;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import javax.swing.filechooser.FileSystemView;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nesridiscount.App;

/**
 * exporte un fichier
 */
public class FileExporter {
    /**
     * exporte le fichier dans le fichier choisi par l'utilisateur
     * @param fileContent contenu du fichier
     * @param fileExtension l'extension du fichier
     * @return si l'export à réussi
     */
    public static boolean export(String fileContent,String fileExtension){
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Sauvegardez le document");
        chooser.getExtensionFilters().add(new ExtensionFilter("Extension",fileExtension) );
        chooser.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory() );

        File selectedPath = chooser.showSaveDialog(App.getStage() );

        boolean returnValue = false;

        if(selectedPath != null){
            FileWriter writer = null; 

            try{
                // écriture du fichier
                writer = new FileWriter(selectedPath,StandardCharsets.UTF_8); 

                writer.write(fileContent);

                returnValue = true;
            }
            catch(Exception e){}
            finally{
                try{
                    if(writer != null) writer.close();
                }
                catch(Exception e){}
            }
        }

        return returnValue;
    } 
}
