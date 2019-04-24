package com.example.mvvmmeeting.Models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ActionModel extends RealmObject {

    @PrimaryKey
    private int actionId;
    private int parentId, state;

    private String actionTitle,actionPerformerName, actionPerformerNumber;

    private Date actionDate;
    private String actionDescription;


    public int getActionId() {
        return actionId;
    }


    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public String getActionPerformerName() {
        return actionPerformerName;
    }

    public void setActionPerformerName(String actionPerformerName) {
        this.actionPerformerName = actionPerformerName;
    }

    public String getActionPerformerNumber() {
        return actionPerformerNumber;
    }

    public void setActionPerformerNumber(String actionPerformerNumber) {
        this.actionPerformerNumber = actionPerformerNumber;
    }



    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }
}
