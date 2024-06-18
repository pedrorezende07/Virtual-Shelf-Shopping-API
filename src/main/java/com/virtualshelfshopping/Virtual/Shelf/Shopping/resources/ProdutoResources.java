package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Produto;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Estoque; // Certifique-se de importar a entidade Estoque
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.ProdutoRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.EstoqueRepository; // Importe o repositório de estoque
import com.virtualshelfshopping.Virtual.Shelf.Shopping.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoResources {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository; // Injetar o repositório de estoque

    @PostMapping
    public ResponseEntity<Produto> salvarProduto(@RequestBody Produto produto) {
        Produto saved = produtoRepository.save(produto);
        if (saved == null) {
            return ResponseEntity.noContent().build();
        }

        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> alterarProduto(@PathVariable("id") Long id,
                                                  @RequestBody Produto produto) {
        Optional<Produto> produtoDoBanco = produtoRepository.findById(Math.toIntExact(id));
        if (produtoDoBanco.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Produto produtoAtualizado = produtoDoBanco.get();
        produtoAtualizado.setNome(produto.getNome());
        produtoAtualizado.setValor(produto.getValor());
        produtoAtualizado.setDescricao(produto.getDescricao());
        produtoAtualizado.setCategoria(produto.getCategoria());
        produtoAtualizado.setSubcategoria(produto.getSubcategoria()); // Atualiza a subcategoria

        // Atualiza o estoque
        Estoque estoque = produtoAtualizado.getEstoque();
        if (estoque != null && produto.getEstoque() != null) {
            estoque.setQuantProduto(produto.getEstoque().getQuantProduto());
            estoqueRepository.save(estoque); // Salva as mudanças no estoque
        }

        produtoRepository.save(produtoAtualizado);

        return ResponseEntity.ok(produtoAtualizado);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> get() {
        List<Produto> produtoList = produtoRepository.findAll();
        if (produtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepository.findById(Math.toIntExact(id));
        if (!produto.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produto.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerProduto(@PathVariable("id") Long id) {
        produtoRepository.deleteById(Math.toIntExact(id));
        return ResponseEntity.noContent().build();
    }
}
