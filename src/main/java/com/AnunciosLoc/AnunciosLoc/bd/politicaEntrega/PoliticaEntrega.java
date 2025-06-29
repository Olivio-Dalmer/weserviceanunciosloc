package com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.AnunciosLoc.AnunciosLoc.bd.anuncio.Anuncio;
import com.AnunciosLoc.AnunciosLoc.bd.condicaoPerfil.CondicaoPerfil;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "politica")
@NoArgsConstructor
public class PoliticaEntrega implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PoliticaTipo titulo; // WHITELIST ou BLACKLIST

    @OneToMany(mappedBy = "politicaEntrega", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CondicaoPerfil> condicoes;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "anuncio_id", referencedColumnName = "id")
    private Anuncio anuncio;
}
