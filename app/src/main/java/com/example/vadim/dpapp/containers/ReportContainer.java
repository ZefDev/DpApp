package com.example.vadim.dpapp.containers;

/**
 * Created by Vadim on 03.06.2017.
 */
public class ReportContainer {
    public String shtrihCod;
    public String nameActiv;
    public String mol;
    public String status;
    public String divisionOfContractor;

    public ReportContainer(String shtrihCod, String nameActiv, String mol, String status, String divisionOfContractor) {
        this.shtrihCod = shtrihCod;
        this.nameActiv = nameActiv;
        this.mol = mol;
        this.status = status;
        this.divisionOfContractor = divisionOfContractor;
    }

    public String getShtrihCod() {

        return shtrihCod;
    }

    public void setShtrihCod(String shtrihCod) {
        this.shtrihCod = shtrihCod;
    }

    public String getNameActiv() {
        return nameActiv;
    }

    public void setNameActiv(String nameActiv) {
        this.nameActiv = nameActiv;
    }

    public String getMol() {
        return mol;
    }

    public void setMol(String mol) {
        this.mol = mol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDivisionOfContractor() {
        return divisionOfContractor;
    }

    public void setDivisionOfContractor(String divisionOfContractor) {
        this.divisionOfContractor = divisionOfContractor;
    }
}
