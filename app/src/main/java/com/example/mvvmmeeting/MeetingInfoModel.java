package com.example.mvvmmeeting;

import java.util.Date;

public class MeetingInfoModel {


    private int meetingId;
    public String meetingName,meetingInformation,meetingTime,closingMeetingTime,meetingDate;
    public Date meetingClosingDate;
    private double meetinglat,meetingLng;

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

    public Date getMeetingClosingDate() {
        return meetingClosingDate;
    }

    public void setMeetingClosingDate(Date meetingClosingDate) {
        this.meetingClosingDate = meetingClosingDate;
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
