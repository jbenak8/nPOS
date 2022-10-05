package cz.jbenak.bo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("functions")
public record Function(@Id String id, String description, String component, KeyComponent key_component) {

    public enum KeyComponent {
        POS, BOS, BOC, KOM
    }
}
