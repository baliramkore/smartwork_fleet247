package com.fleet247.driver.data.models.driverarrived;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 16/11/17.
 */

public class Passenger {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("admin_id")
    @Expose
    public String adminId;
    @SerializedName("group_id")
    @Expose
    public String groupId;
    @SerializedName("subgroup_id")
    @Expose
    public String subgroupId;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("people_cid")
    @Expose
    public String peopleCid;
    @SerializedName("people_name")
    @Expose
    public String peopleName;
    @SerializedName("people_email")
    @Expose
    public String peopleEmail;
    @SerializedName("people_contact")
    @Expose
    public String peopleContact;
    @SerializedName("age")
    @Expose
    public String age;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("id_proof_type")
    @Expose
    public String idProofType;
    @SerializedName("id_proof_no")
    @Expose
    public String idProofNo;
    @SerializedName("is_active")
    @Expose
    public String isActive;
    @SerializedName("has_dummy_email")
    @Expose
    public String hasDummyEmail;
    @SerializedName("fcm_regid")
    @Expose
    public String fcmRegid;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("modified")
    @Expose
    public String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSubgroupId() {
        return subgroupId;
    }

    public void setSubgroupId(String subgroupId) {
        this.subgroupId = subgroupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPeopleCid() {
        return peopleCid;
    }

    public void setPeopleCid(String peopleCid) {
        this.peopleCid = peopleCid;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getPeopleEmail() {
        return peopleEmail;
    }

    public void setPeopleEmail(String peopleEmail) {
        this.peopleEmail = peopleEmail;
    }

    public String getPeopleContact() {
        return peopleContact;
    }

    public void setPeopleContact(String peopleContact) {
        this.peopleContact = peopleContact;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public String getIdProofNo() {
        return idProofNo;
    }

    public void setIdProofNo(String idProofNo) {
        this.idProofNo = idProofNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getHasDummyEmail() {
        return hasDummyEmail;
    }

    public void setHasDummyEmail(String hasDummyEmail) {
        this.hasDummyEmail = hasDummyEmail;
    }

    public String getFcmRegid() {
        return fcmRegid;
    }

    public void setFcmRegid(String fcmRegid) {
        this.fcmRegid = fcmRegid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
