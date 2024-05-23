package com.virtualshelfshopping.Virtual.Shelf.Shopping.repository;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
}