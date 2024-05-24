package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Categoria;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.CategoriaRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.util.ResponseMessage;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaResources {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostMapping
    public ResponseEntity<Categoria> salvarCategoria(@RequestBody Categoria categoria) {
        Categoria saved = categoriaRepository.save(categoria);
        if (saved == null) {
            return ResponseEntity.noContent().build();
        }

        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> editarCategoria(@PathVariable("id") Long id,@RequestBody Categoria categoria) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(Math.toIntExact(id));

        if (!optionalCategoria.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Categoria existingCategoria = optionalCategoria.get();
        existingCategoria.setNome(categoria.getNome()); // Atualize apenas o campo necessário

        Categoria updatedCategoria = categoriaRepository.save(existingCategoria);

        return ResponseEntity.ok(updatedCategoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> get(){
        List<Categoria> categoriaList = categoriaRepository.findAll();
        if(categoriaList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoriaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id.intValue());
        if(categoria.isEmpty()){
            ResponseMessage responseMessage = new ResponseMessage("Categoria não encontrada para o ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        return ResponseEntity.ok(categoria);

    }
}