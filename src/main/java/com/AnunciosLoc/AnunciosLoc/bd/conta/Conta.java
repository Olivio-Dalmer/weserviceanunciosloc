package com.AnunciosLoc.AnunciosLoc.bd.conta;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.AnunciosLoc.AnunciosLoc.bd.user.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Conta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Conta implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "titular ")
    private String titular ;

    @Column(name = "saldo")
    private double saldo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

}
