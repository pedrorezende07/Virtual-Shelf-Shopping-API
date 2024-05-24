package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Estoque;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.EstoqueRepository;
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
@RequestMapping("/api/v1/estoque")
public class EstoqueResources {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @PostMapping
    public ResponseEntity<Estoque> salvarEstoque(@RequestBody Estoque estoque) {
        Estoque saved = estoqueRepository.save(estoque);

        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarEstoque(@PathVariable("id") Long id, @RequestBody Estoque estoque) {
        Optional<Estoque> optionalEstoque = estoqueRepository.findById(Math.toIntExact(id));

        if (!optionalEstoque.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Estoque existingEstoque = optionalEstoque.get();
        int valorAntigo = existingEstoque.getQuantProduto();
        existingEstoque.setQuantProduto(estoque.getQuantProduto()); // Atualize apenas o campo necessário

        estoqueRepository.save(existingEstoque);
        ResponseMessage responseMessage = new ResponseMessage(String.format("Estoque do produto com ID %d atualizado para %d o valor anterior era %d", id, estoque.getQuantProduto(),valorAntigo));
        return ResponseEntity.ok(responseMessage);
    }



    @GetMapping
    public ResponseEntity<List<Estoque>> get(){
        List<Estoque> categoriaList = estoqueRepository.findAll();
        if(categoriaList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoriaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        Optional<Estoque> Estoque = estoqueRepository.findById(id.intValue());
        if(Estoque.isEmpty()){
            ResponseMessage responseMessage = new ResponseMessage("Estoque não encontrada para o ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        return ResponseEntity.ok(Estoque);

    }
}