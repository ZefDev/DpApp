package com.example.vadim.dpapp.containers;

import java.io.Serializable;

/**
 * Created by Vadim on 26.03.2017.
 */
public class ActivContainer  implements Serializable {

    private static final long serialVersionUID = -5792012811084133126L;

    private String code;
    private String name;
    private String typeActiv;
    private String shtrihCode;
    private String photo;
    private String contractor;
    private String mol;

    public String getMol() {
        return mol;
    }

    public void setMol(String mol) {
        this.mol = mol;
    }

    public String getConditionActiv() {
        return conditionActiv;
    }

    public void setConditionActiv(String conditionActiv) {
        this.conditionActiv = conditionActiv;
    }

    private String conditionActiv;

    public ActivContainer(String code, String name, String typeActiv, String shtrihCode, String photo, String contractor, String mol, String conditionActiv) {
        this.code = code;
        this.name = name;
        this.typeActiv = typeActiv;
        this.shtrihCode = shtrihCode;
        this.photo = photo;
        this.contractor = contractor;
        this.mol = mol;
        this.conditionActiv = conditionActiv;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeActiv() {
        return typeActiv;
    }

    public void setTypeActiv(String typeActiv) {
        this.typeActiv = typeActiv;
    }

    public String getShtrihCode() {
        return shtrihCode;
    }

    public void setShtrihCode(String shtrihCode) {
        this.shtrihCode = shtrihCode;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }
}
