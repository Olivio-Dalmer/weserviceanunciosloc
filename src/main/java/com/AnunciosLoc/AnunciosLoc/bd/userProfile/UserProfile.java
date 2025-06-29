package com.AnunciosLoc.AnunciosLoc.bd.userProfile;

import javax.persistence.*;
import javax.xml.datatype.XMLGregorianCalendar;

import com.AnunciosLoc.AnunciosLoc.bd.local.Local;
import com.AnunciosLoc.AnunciosLoc.bd.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfile implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String chave;

    @Column(nullable = false)
    private String valor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
