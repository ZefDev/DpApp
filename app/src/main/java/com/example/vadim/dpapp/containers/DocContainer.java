package com.example.vadim.dpapp.containers;

public class DocContainer {
    private String codeDoc;
    private String avtorDoc;
    private String messageDoc;
    private String lastDate;

    public DocContainer(String codeDoc, String avtorDoc, String messageDoc, String lastDate) {
        this.codeDoc = codeDoc;
        this.avtorDoc = avtorDoc;
        this.messageDoc = messageDoc;
        this.lastDate = lastDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getCodeDoc() {
        return codeDoc;
    }

    public void setCodeDoc(String codeDoc) {
        this.codeDoc = codeDoc;
    }

    public String getAvtorDoc() {
        return avtorDoc;
    }

    public void setAvtorDoc(String avtorDoc) {
        this.avtorDoc = avtorDoc;
    }

    public String getMessageDoc() {
        return messageDoc;
    }

    public void setMessageDoc(String messageDoc) {
        this.messageDoc = messageDoc;
    }
}
