package com.example.mvvmmeeting.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MemberModel extends RealmObject {
    @PrimaryKey
    private int memberId;

    private int parentId;

    private String memberName;

    private String memberNumber;

    private boolean memberPeresent;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memerName) {
        this.memberName = memerName;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public boolean isMemberPeresent() {
        return memberPeresent;
    }

    public void setMemberPeresent(boolean memberPeresent) {
        this.memberPeresent = memberPeresent;
    }
}
