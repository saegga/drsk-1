package ru.drsk.httptest2.util;

/**
 * Created by sergei on 04.02.2016.
 */
public class Session {

    private static Session instance;
    private String sessionId;
    private Session() {
    }

    public static Session getInstance() {
        if(instance == null){
            instance = new Session();
        }
        return instance;
    }

    public void deleteSession(){
        instance = null;
        sessionId = null;
    }
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
