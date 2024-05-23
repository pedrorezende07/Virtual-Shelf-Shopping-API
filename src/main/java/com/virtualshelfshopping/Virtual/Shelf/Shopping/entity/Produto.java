package com.virtualshelfshopping.Virtual.Shelf.Shopping.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_produto")
    private Long codProduto;

    private String nome;
    private float valor;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "cod_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "cod_estoque")
    private Estoque estoque;

    // Getters and setters


    public Long getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(Long codProduto) {
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public void update(Long id, Produto produto) {
        this.codProduto = id;
        this.nome = produto.getNome();
        this.valor = produto.getValor();
        this.descricao = produto.getDescricao();
        this.categoria = produto.getCategoria();
        this.estoque = produto.getEstoque();
    }
}
