package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Subcategoria;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.SubcategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subcategorias")
public class SubcategoriaResources {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @GetMapping
    public List<Subcategoria> getAllSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subcategoria> getSubcategoriaById(@PathVariable Long id) {
        Optional<Subcategoria> subcategoria = subcategoriaRepository.findById(id);
        if (subcategoria.isPresent()) {
            return ResponseEntity.ok(subcategoria.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Subcategoria createSubcategoria(@RequestBody Subcategoria subcategoria) {
        return subcategoriaRepository.save(subcategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subcategoria> updateSubcategoria(@PathVariable Long id, @RequestBody Subcategoria subcategoriaDetails) {
        Optional<Subcategoria> subcategoria = subcategoriaRepository.findById(id);
        if (subcategoria.isPresent()) {
            Subcategoria existingSubcategoria = subcategoria.get();
            existingSubcategoria.setNome(subcategoriaDetails.getNome());
            Subcategoria updatedSubcategoria = subcategoriaRepository.save(existingSubcategoria);
            return ResponseEntity.ok(updatedSubcategoria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubcategoria(@PathVariable Long id) {
        if (subcategoriaRepository.existsById(id)) {
            subcategoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
