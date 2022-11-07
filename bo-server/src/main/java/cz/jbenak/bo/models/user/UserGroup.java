package cz.jbenak.bo.models.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_groups")
public record UserGroup(@Id int id, String name, String description) {
}
