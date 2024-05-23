package com.virtualshelfshopping.Virtual.Shelf.Shopping.repository;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
}
