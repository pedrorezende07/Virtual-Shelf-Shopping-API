package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Usuario;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Carteira;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.CarteiraRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.UsuarioRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioResources {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @PostMapping
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
        Usuario saved = usuarioRepository.save(usuario);
        if (saved == null) {
            return ResponseEntity.noContent().build();
        }
        Carteira carteira = new Carteira();
        carteira.setUsuario(saved);
        carteira.setSaldo(0);
        carteiraRepository.save(carteira);

        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> alterarUsuario(@PathVariable("id") Long id,
                                           @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioDoBanco = usuarioRepository.findById(String.valueOf(id));
        if (usuarioDoBanco.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioDoBanco.get().update(id, usuario);
        Usuario saved = usuarioRepository.save(usuario);
        if (saved == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> get() {
        List<Usuario> usuarioList = usuarioRepository.findAll();
        if (usuarioList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarioList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable("id") Long id) {
        Optional<Usuario> usuarioList = usuarioRepository.findById(String.valueOf(id));
        if (!usuarioList.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarioList.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable("id") Long id) {
        usuarioRepository.deleteById(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-duplicity")
    public ResponseEntity<Map<String, Boolean>> checkDuplicity(@RequestParam String field, @RequestParam String value) {
        boolean exists = false;
        switch (field) {
            case "cpf":
                exists = usuarioRepository.existsByCpf(value);
                break;
            case "email":
                exists = usuarioRepository.existsByEmail(value);
                break;
            case "telefone":
                exists = usuarioRepository.existsByTelefone(value);
                break;
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}