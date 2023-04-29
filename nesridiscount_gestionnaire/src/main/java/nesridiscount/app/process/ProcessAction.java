package nesridiscount.app.process;

/**
 * action à exécuter après une étape d'un processus
 */
public interface ProcessAction {
    /**
     * l'action à faire
     * @throws Exception en cas d'erreur
     */
    public void toDoAdrer() throws Exception;
}
