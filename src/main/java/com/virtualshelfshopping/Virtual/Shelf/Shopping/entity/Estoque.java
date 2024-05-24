package com.virtualshelfshopping.Virtual.Shelf.Shopping.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "estoque")
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int qtdProduto;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantProduto() {
        return qtdProduto;
    }

    public void setQuantProduto(int quantProduto) {
        this.qtdProduto = quantProduto;
    }
}
