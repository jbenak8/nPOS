package cz.jbenak.bo.models;

import java.time.LocalDateTime;
import java.util.Objects;

public record BoUser(int id,
                     String user_name,
                     String user_surname,
                     int group_id,
                     String mail,
                     String phone,
                     int rest_login_attempts,
                     boolean locked,
                     LocalDateTime last_login_timestamp,
                     String note) implements Comparable<BoUser> {

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
