package com.example.vadim.dpapp.containers;

/**
 * Created by Vadim on 05.05.2017.
 */
public class UserContainer {
    String login;
    String password;
    String uid;
    String contractor;
    String post;
    String right;


    public UserContainer(String login, String password, String uid){
        this.login = login;
        this.password = password;
        this.uid = uid;
    }

    public UserContainer(String login, String contractor, String post, String right) {
        this.login = login;
        this.contractor = contractor;
        this.post = post;
        this.right = right;
    }
    public UserContainer(String login, String contractor, String post, String right,String uid) {
        this.login = login;
        this.contractor = contractor;
        this.post = post;
        this.right = right;
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
