package com.virtualshelfshopping.Virtual.Shelf.Shopping.repository;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}