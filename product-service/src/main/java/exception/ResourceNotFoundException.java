package exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message); // chama o construtor da classe pai (RuntimeException) com a mensagem de erro
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause); // chama o construtor da classe pai com a mensagem de erro e a causa da exceção
    }
}
