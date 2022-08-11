package cz.jbenak.npos.api.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Class for user object. Can be used for creation and also change of the user.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-08-06
 */

public class User implements Comparable<User> {

    private final String userId;
    private String userName;
    private String userSurname;
    private String phone;
    private String mail;
    private String note;
    private List<String> accessRights;
    private List<String> userGroupIds;
    private int restLoginAttempts;
    private String initPassword;
    private boolean changeInitPassword;
    private boolean userLocked;
    private LocalDateTime lastLoginTimestamp;
    private String lastLoginFrom;

    public User(String userId) {
        this.userId = userId;
    }

    @JsonCreator
    public User(@JsonProperty(value = "userId", required = true) String userId,
                @JsonProperty(value = "userName") String userName,
                @JsonProperty(value = "userSurname") String userSurname,
                @JsonProperty(value = "phone") String phone,
                @JsonProperty(value = "mail") String mail,
                @JsonProperty(value = "note") String note,
                @JsonProperty(value = "accessRights") List<String> accessRights,
                @JsonProperty(value = "userGroupIds") List<String> userGroupIds,
                @JsonProperty(value = "restLoginAttempts") int restLoginAttempts,
                @JsonProperty(value = "initPassword") String initPassword,
                @JsonProperty(value = "initPasswordChangeRequired") boolean changeInitPassword,
                @JsonProperty(value = "userLocked") boolean userLocked,
                @JsonProperty(value = "LastLoginTimestamp") LocalDateTime lastLoginTimestamp,
                @JsonProperty(value = "lastLoggedFrom") String lastLoginFrom) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.phone = phone;
        this.mail = mail;
        this.note = note;
        this.accessRights = accessRights;
        this.userGroupIds = userGroupIds;
        this.restLoginAttempts = restLoginAttempts;
        this.initPassword = initPassword;
        this.changeInitPassword = changeInitPassword;
        this.userLocked = userLocked;
        this.lastLoginTimestamp = lastLoginTimestamp;
        this.lastLoginFrom = lastLoginFrom;
    }

    @JsonProperty("userId")
    public String getUserId() {
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
    public List<String> getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(List<String> accessRights) {
        this.accessRights = accessRights;
    }

    @JsonProperty("userGroupIds")
    public List<String> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<String> userGroupIds) {
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

    @JsonProperty("initPassword")
    public String getInitPassword() {
        return initPassword;
    }

    public void setInitPassword(String initPassword) {
        this.initPassword = initPassword;
    }

    @JsonProperty("initPasswordChangeRequired")
    public boolean isChangeInitPassword() {
        return changeInitPassword;
    }

    public void setChangeInitPassword(boolean changeInitPassword) {
        this.changeInitPassword = changeInitPassword;
    }

    @JsonProperty("LastLoginTimestamp")
    public LocalDateTime getLastLoginTimestamp() {
        return lastLoginTimestamp;
    }

    public void setLastLoginTimestamp(LocalDateTime lastLoginTimestamp) {
        this.lastLoginTimestamp = lastLoginTimestamp;
    }

    @JsonProperty("lastLoggedFrom")
    public String getLastLoginFrom() {
        return lastLoginFrom;
    }

    public void setLastLoginFrom(String lastLoginFrom) {
        this.lastLoginFrom = lastLoginFrom;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public int compareTo(User o) {
        return userId.compareTo(o.userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(user.userId, userId);
    }
}
