package nesridiscount.app.process;

/**
 * gestion d'un processus de l'application
 */
public abstract class Process {
    private ProcessAction afterStart = () -> this.doProcess();
    private ProcessAction afterDo = () -> this.endProcess();
    private ProcessAction afterEnd = null;

    protected boolean isDelayed;

    /**
     * défini si le processus est délayé ou non , uniquement si oui les méthodes afterStart / afterDo / afterEnd seront appelé
     * afterStart / afterDo appellent doProcess et endProcess
     * afterEnd est par défaut à null
     */
    public Process(){
        this.isDelayed = false;
    }

    /**
     * démarre le processus
     * @return this
     * @throws Exception en cas d'erreur
     */
    public Process startProcess() throws Exception{
        if(this.isDelayed && this.afterStart != null) this.afterStart.toDoAdrer();

        return this;
    }

    /**
     * fais le processus
     * @return this
     * @throws Exception en cas d'erreur
     */
    public Process doProcess() throws Exception{
        if(this.isDelayed && this.afterDo != null) this.afterDo.toDoAdrer();

        return this;
    }

    /**
     * fini le processus
     * @return this
     * @throws Exception en cas d'erreur
     */
    public Process endProcess() throws Exception{
        if(this.isDelayed && this.afterEnd != null) this.afterEnd.toDoAdrer();

        return this;
    }

    /**
     * 
     * @param toDoAfter
     * @return this
     */
    public Process setAfterStart(ProcessAction toDoAfter){
        this.afterStart = toDoAfter;
        
        return this;
    }

    /**
     * 
     * @param toDoAfter
     * @return this
     */
    public Process setAfterDo(ProcessAction toDoAfter){
        this.afterDo = toDoAfter;
        
        return this;
    }

    /**
     * 
     * @param toDoAfter
     * @return this
     */
    public Process setAfterEnd(ProcessAction toDoAfter){
        this.afterEnd = toDoAfter;
        
        return this;
    }

    /**
     * crée et lance les étapes d'un processus
     * @param toExec
     * @param afterStart à faire après lancement
     * @param afterDo à faire après exécution
     * @param afterEnd à faire après fin
     * @return le processus crée ou null si échec
     * @throws Exception si une exception est envoyé durant une étape
     */
    public static Process execProcess(Class<Process> toExec,ProcessAction afterStart,ProcessAction afterDo,ProcessAction afterEnd) throws Exception{
        Process createdProcess = Process.createProcess(toExec);

        if(createdProcess != null){
            // affectation des élements à faire
            if(afterStart != null) createdProcess.setAfterStart(afterStart);
            if(afterDo != null) createdProcess.setAfterStart(afterDo);
            if(afterEnd != null) createdProcess.setAfterStart(afterEnd);

            // lancement entier du processus
            createdProcess
                .startProcess()
                .doProcess()
                .endProcess();
        }

        return createdProcess;
    }

    /**
     * @param class la class du process
     * @return l'instance du processus ou null
     */
    private static Process createProcess(Class<Process> toCreate){
        try{
            return toCreate.getConstructor().newInstance();
        }
        catch(Exception e){
            return null;
        }
    }
}
