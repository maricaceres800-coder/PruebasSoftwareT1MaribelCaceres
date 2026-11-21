package pe.edu.cibertec.pruebassoftwaret1.subcription;

public class ExpiredCardException extends RuntimeException{
    public ExpiredCardException(String msg){
        super(msg);
    }
}
