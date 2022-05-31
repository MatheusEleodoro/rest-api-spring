package com.eleodoro.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="nome")
    private String nome;

    @Column(name="email")
    private String email;

    @Column(name = "senha")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_areaUsuario" ,referencedColumnName = "id")
    @JsonIgnoreProperties("usuarios")
    private AreaUsuario areaDeUsuario;

    public Usuario(String nome, String email, String password, AreaUsuario areaDeUsuario) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.areaDeUsuario = areaDeUsuario;
    }

    public List<SimpleGrantedAuthority> getPermissoes()
    {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+areaDeUsuario.getNome()));
        return authorities;
    }
}
