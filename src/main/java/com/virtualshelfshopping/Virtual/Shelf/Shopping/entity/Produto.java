package com.virtualshelfshopping.Virtual.Shelf.Shopping.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_produto")
    private int codProduto;

    private String nome;
    private float valor;
    private String descricao;

    @ElementCollection
    private List<String> categoria;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Estoque estoque;



    // Getters and setters

    public long getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(int codProduto) {
        this.codProduto = codProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getCategoria() {
        return categoria;
    }

    public void setCategoria(List<String> categoria) {
        this.categoria = categoria;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }


    public void update(int id, Produto produto) {
        this.codProduto = id;
        this.nome = produto.getNome();
        this.valor = produto.getValor();
        this.descricao = produto.getDescricao();
        this.categoria = produto.getCategoria();
        this.estoque = produto.getEstoque();
    }
}
