package at.synchess.boardsoftware.exceptions;


public class AppManagerException extends Exception{
    Class controller;

    public Class getController() {
        return controller;
    }

    public void setController(Class controller) {
        this.controller = controller;
    }

    public AppManagerException(Class controller) {
        this.controller = controller;
    }
}
