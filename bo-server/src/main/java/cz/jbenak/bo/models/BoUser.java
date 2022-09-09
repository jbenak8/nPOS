package cz.jbenak.bo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table("users")
public class BoUser implements Comparable<BoUser> {

    @Id
    private int id;
    private String user_name;
    private String user_surname;
    private int group_id;
    private String mail;
    private String phone;
    private int rest_login_attempts;
    private boolean locked;
    private LocalDateTime last_login_timestamp;
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRest_login_attempts() {
        return rest_login_attempts;
    }

    public void setRest_login_attempts(int rest_login_attempts) {
        this.rest_login_attempts = rest_login_attempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLast_login_timestamp() {
        return last_login_timestamp;
    }

    public void setLast_login_timestamp(LocalDateTime last_login_timestamp) {
        this.last_login_timestamp = last_login_timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "User = [" +
                "userId=" + id +
                ", userName=" + user_name +
                ", userSurname=" + user_surname +
                ", phone=" + phone +
                ", mail=" + mail +
                ", note=" + note +
                ", groupId=" + group_id +
                ", restLoginAttempts=" + rest_login_attempts +
                ", userLocked=" + locked +
                ", lastLoginTimestamp=" + last_login_timestamp +
                ']';
    }

    @Override
    public int compareTo(BoUser o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BoUser user = (BoUser) obj;
        return Objects.equals(user.id, id);
    }
}
