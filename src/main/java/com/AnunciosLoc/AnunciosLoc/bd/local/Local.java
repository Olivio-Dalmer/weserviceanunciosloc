package com.AnunciosLoc.AnunciosLoc.bd.local;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.AnunciosLoc.AnunciosLoc.bd.anuncio.Anuncio;
// import com.AnunciosLoc.AnunciosLoc.bd.coodernadasTipo.coodernadasTipo;
// import com.AnunciosLoc.AnunciosLoc.bd.coordenadas.Coordenadas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "local")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Local implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "localizacao", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Anuncio> ads = new ArrayList<>();

}
