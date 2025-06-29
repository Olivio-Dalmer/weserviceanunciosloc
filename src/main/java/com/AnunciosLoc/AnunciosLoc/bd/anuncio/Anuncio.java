package com.AnunciosLoc.AnunciosLoc.bd.anuncio;

import javax.persistence.*;

import java.time.LocalDateTime;
import com.AnunciosLoc.AnunciosLoc.bd.local.Local;
import com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega.PoliticaEntrega;
import com.AnunciosLoc.AnunciosLoc.bd.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "anuncio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Anuncio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Local localizacao;

    @OneToOne(mappedBy = "anuncio", cascade = CascadeType.ALL)
    private PoliticaEntrega politicaEntrega;

}
