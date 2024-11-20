package kz.zip.taskmaster.exception;

public class ManagerSaveException extends Exception{
    public ManagerSaveException(String message, Exception exception){
        super(message, exception);
    }
}
