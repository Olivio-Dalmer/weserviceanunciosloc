package com.AnunciosLoc.AnunciosLoc.bd.user;

import javax.persistence.*;

import com.AnunciosLoc.AnunciosLoc.bd.anuncio.Anuncio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    protected String username;
    @Column(name = "password", nullable = true)
    protected String password;
    @Column(name = "email", unique = true, nullable = false)
    protected String email;
    @Column(name = "datanascimento")
    protected String datanascimento;
    @Column(name = "genero")
    protected String genero;
    @Column(name = "tipo")
    protected int tipo;
    @Column(name = "telefone")
    protected String telefone; 

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Anuncio> anuncios;

    public void setAuthToken(Object object) {
        throw new UnsupportedOperationException("Unimplemented method 'setAuthToken'");
    }

}
