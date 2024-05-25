package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Usuario;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.UsuarioRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioResources {

    @Autowired
    private UsuarioRepository usuariousRepository;

    @PostMapping
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
        Usuario saved = usuariousRepository.save(usuario);
        if (saved == null) {
            return ResponseEntity.noContent().build();
        }

        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> alterarUsuario(@PathVariable("id") Long id,
                                           @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioDoBanco = usuariousRepository.findById(String.valueOf(id));
        if (usuarioDoBanco.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioDoBanco.get().update(id, usuario);
        Usuario saved = usuariousRepository.save(usuario);
        if (saved == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> get() {
        List<Usuario> usuarioList = usuariousRepository.findAll();
        if (usuarioList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarioList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable("id") Long id) {
        Optional<Usuario> usuarioList = usuariousRepository.findById(String.valueOf(id));
        if (!usuarioList.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarioList.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable("id") Long id) {
        usuariousRepository.deleteById(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}