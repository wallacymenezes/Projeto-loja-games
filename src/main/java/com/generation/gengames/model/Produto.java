package com.generation.gengames.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O atributo 'nome' é obrigatório")
    private String nome;

    @NotBlank(message = "O atributo descricao é Obrigatório!")
    @Size(min = 5, max = 1000, message = "O atributo descrição deve conter no mínimo 5 e no máximo 1000 caracteres")
    private String descricao;

    @NotNull(message = "O atributo preco é Obrigatório!")
    private double preco;

    @NotNull(message = "O atributo quantidade é Obrigatório!")
    private int quantidade;

    @ManyToOne
    @JsonIgnoreProperties("produto")
    private Categoria categoria;

    @ManyToOne
    @JsonIgnoreProperties("produto")
    private Usuario usuario;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    //GETTERS E SETTERS
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String titulo) { this.nome = titulo; }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
