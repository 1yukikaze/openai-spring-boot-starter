package io.github.yukikaze.insert_chatgpt.exception;

public class ChatgptException extends RuntimeException{
    public ChatgptException(Exception e){
        super();
    }
    public ChatgptException(String message){
        super(message);
    }
}
