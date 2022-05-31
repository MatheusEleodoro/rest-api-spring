package com.eleodoro.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "area_usuario")
@NoArgsConstructor
public class AreaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private  String nome;

    @Column(name="descricao")
    private String descricao;

    @OneToMany(mappedBy = "areaDeUsuario",fetch = FetchType.EAGER)
    private Collection<Usuario> usuarios;

    @ManyToOne
    @JoinColumn(name = "responsavel" ,referencedColumnName = "id")
    @JsonIncludeProperties(value = {"id","nome"})
    private Usuario responsavel;
    public AreaUsuario(Long id) {
        this.id = id;
    }
}
