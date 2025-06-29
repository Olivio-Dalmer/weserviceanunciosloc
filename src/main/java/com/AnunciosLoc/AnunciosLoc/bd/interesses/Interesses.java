package com.AnunciosLoc.AnunciosLoc.bd.interesses;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "interesses")
public class Interesses implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    public Interesses() {
    }

    public Interesses(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Override
    public String toString() {
        return "Interesses(id=" + id + ", name=" + name + ")";
    }
}
