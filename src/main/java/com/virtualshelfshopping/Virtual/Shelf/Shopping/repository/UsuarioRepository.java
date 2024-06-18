package com.virtualshelfshopping.Virtual.Shelf.Shopping.repository;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    boolean existsByCpf(String value);

    boolean existsByEmail(String value);

    boolean existsByTelefone(String value);

    Optional<Usuario> findByEmail(String email);
}

