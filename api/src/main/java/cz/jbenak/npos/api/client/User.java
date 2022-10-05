package cz.jbenak.npos.api.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Class for user object. Can be used for creation and also change of the user.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-08-06
 */
public class User implements Comparable<User> {

    private int userId;
    private String userName;
    private String userSurname;
    private String phone;
    private String mail;
    private String note;
    private Set<String> accessRights;
    private List<Integer> userGroupIds;
    private int restLoginAttempts;
    private boolean userLocked;
    private LocalDateTime lastLoginTimestamp;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty(value = "userId", required = true)
    public int getUserId() {
        return userId;
    }

    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("userSurname")
    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("mail")
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @JsonProperty("note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @JsonProperty("accessRights")
    public Set<String> getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(Set<String> accessRights) {
        this.accessRights = accessRights;
    }

    @JsonProperty("userGroupIds")
    public List<Integer> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<Integer> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }

    @JsonProperty("restLoginAttempts")
    public int getRestLoginAttempts() {
        return restLoginAttempts;
    }

    public void setRestLoginAttempts(int restLoginAttempts) {
        this.restLoginAttempts = restLoginAttempts;
    }

    @JsonProperty("userLocked")
    public boolean isUserLocked() {
        return userLocked;
    }

    public void setUserLocked(boolean userLocked) {
        this.userLocked = userLocked;
    }


    @JsonProperty("lastLoginTimestamp")
    public LocalDateTime getLastLoginTimestamp() {
        return lastLoginTimestamp;
    }

    public void setLastLoginTimestamp(LocalDateTime lastLoginTimestamp) {
        this.lastLoginTimestamp = lastLoginTimestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public int compareTo(User o) {
        return Integer.compare(userId, o.userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(user.userId, userId);
    }

    //TODO some helper for human-readable lists
    @Override
    public String toString() {
        return "User = {" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", note='" + note + '\'' +
                ", accessRights=" + accessRights +
                ", userGroupIds=" + userGroupIds +
                ", restLoginAttempts=" + restLoginAttempts +
                ", userLocked=" + userLocked +
                ", lastLoginTimestamp=" + lastLoginTimestamp +
                '}';
    }
}
