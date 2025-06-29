package com.AnunciosLoc.AnunciosLoc.bd.condicaoPerfil;

import javax.persistence.*;

import com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega.PoliticaEntrega;

import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "condicao_perfil")
@NoArgsConstructor
@AllArgsConstructor
public class CondicaoPerfil implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String chave; // Ex: "profissao"

    @Column(nullable = true)
    private String valor; // Ex: "Estudante"

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "politica_entrega_id")
    private PoliticaEntrega politicaEntrega;

}