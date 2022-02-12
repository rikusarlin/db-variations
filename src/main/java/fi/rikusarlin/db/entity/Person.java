package fi.rikusarlin.db.entity;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Person extends PanacheEntity {
    public Long id;
    public String firstName;
    public String lastName;   
}
