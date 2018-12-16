/**
 * Exception that is thrown when player performs an invalid move
 */
public class IllegalMoveException extends RuntimeException {

    public IllegalMoveException(){
        super();
    }

    public IllegalMoveException(String message){
        super(message);
    }
}
