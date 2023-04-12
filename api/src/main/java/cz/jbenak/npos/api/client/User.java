package cz.jbenak.npos.api.client;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Jacksonized
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
}
