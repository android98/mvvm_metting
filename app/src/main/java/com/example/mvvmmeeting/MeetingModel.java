package com.example.mvvmmeeting;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MeetingModel extends RealmObject {

    @PrimaryKey
    private int meetingId;
    public String meetingName,meetingInformation,meetingTime,closingMeetingTime,meetingDate;
    public Date meetingClosingDat;
    private double meetinglat,meetingLng;

    public MeetingModel(int meetingId, String meetingName, String meetingInformation, String meetingTime, String closingMeetingTime, String meetingDate, Date meetingClosingDat, double meetinglat, double meetingLng) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.meetingInformation = meetingInformation;
        this.meetingTime = meetingTime;
        this.closingMeetingTime = closingMeetingTime;
        this.meetingDate = meetingDate;
        this.meetingClosingDat = meetingClosingDat;
        this.meetinglat = meetinglat;
        this.meetingLng = meetingLng;
    }


    public MeetingModel(String meetingName, String meetingInformation, String meetingDate) {
        this.meetingName = meetingName;
        this.meetingInformation = meetingInformation;
        this.meetingDate = meetingDate;
    }

    public MeetingModel() {
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingInformation() {
        return meetingInformation;
    }

    public void setMeetingInformation(String meetingInformation) {
        this.meetingInformation = meetingInformation;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getClosingMeetingTime() {
        return closingMeetingTime;
    }

    public void setClosingMeetingTime(String closingMeetingTime) {
        this.closingMeetingTime = closingMeetingTime;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Date getMeetingClosingDat() {
        return meetingClosingDat;
    }

    public void setMeetingClosingDat(Date meetingClosingDat) {
        this.meetingClosingDat = meetingClosingDat;
    }

    public double getMeetinglat() {
        return meetinglat;
    }

    public void setMeetinglat(double meetinglat) {
        this.meetinglat = meetinglat;
    }

    public double getMeetingLng() {
        return meetingLng;
    }

    public void setMeetingLng(double meetingLng) {
        this.meetingLng = meetingLng;
    }
}
