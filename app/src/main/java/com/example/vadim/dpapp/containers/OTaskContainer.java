package com.example.vadim.dpapp.containers;

public class OTaskContainer {
    private int code;
    private String opisanie;
    private String codeActiv;
    private String codeTask;
    private String time;

    public OTaskContainer( int code,String codeTask,String codeActiv, String opisanie ) {
        this.code = code;
        this.codeTask = codeTask;
        this.opisanie = opisanie;
        this.codeActiv = codeActiv;
    }

    public OTaskContainer( int code,String codeTask,String codeActiv, String opisanie,String time ) {
        this.code = code;
        this.codeTask = codeTask;
        this.opisanie = opisanie;
        this.codeActiv = codeActiv;
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getOpisanie() {
        return opisanie;
    }

    public void setOpisanie(String opisanie) {
        this.opisanie = opisanie;
    }

    public String getCodeActiv() {
        return codeActiv;
    }

    public void setCodeActiv(String codeActiv) {
        this.codeActiv = codeActiv;
    }

    public String getCodeTask() {
        return codeTask;
    }

    public void setCodeTask(String codeTask) {
        this.codeTask = codeTask;
    }

    @Override
    public String toString() {
        return "OTaskContainer{" +
                "code=" + code +
                ", opisanie='" + opisanie + '\'' +
                ", codeActiv='" + codeActiv + '\'' +
                ", codeTask='" + codeTask + '\'' +
                '}';
    }
}
