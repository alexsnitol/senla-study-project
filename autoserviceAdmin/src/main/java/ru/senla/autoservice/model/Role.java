package ru.senla.autoservice.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Role extends AbstractModel {

    @Id
    @SequenceGenerator(name = "roles_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    private Long id;

    private String name;
//    @Transient
//    @Formula(
//            "(SELECT " +
//            "    a.name " +
//            "FROM " +
//            "    role_authorities " +
//            "INNER JOIN authorities a on a.id = role_authorities.authority_id " +
//            "WHERE " +
//            "    role_authorities.role_id = id)"
//    )
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "role_authority",
            joinColumns = { @JoinColumn(name = "role_id") },
            inverseJoinColumns = { @JoinColumn(name = "authority_id") }
    )
    private List<Authority> authorities;

}
