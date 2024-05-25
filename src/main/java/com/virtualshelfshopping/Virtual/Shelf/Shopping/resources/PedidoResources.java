package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Pedido;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.PedidoRepository;
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
@RequestMapping("/api/v1/pedidos")
public class PedidoResources {

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping
    public ResponseEntity<Pedido> salvarPedido(@RequestBody Pedido pedido) {
        Pedido saved = pedidoRepository.save(pedido);

        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarPedido(@PathVariable("id") Long id, @RequestBody Pedido pedido) {
        Optional<Pedido> optionalPedido = pedidoRepository.findById(Math.toIntExact(id));

        if (optionalPedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido existingPedido = optionalPedido.get();

        existingPedido.setPrecoTotal(pedido.getPrecoTotal()); // Atualize apenas o campo necessário

        pedidoRepository.save(existingPedido);
        ResponseMessage responseMessage = new ResponseMessage(String.format("Pedido com ID %d atualizado", id));
        return ResponseEntity.ok(responseMessage);
    }



    @GetMapping
    public ResponseEntity<List<Pedido>> get(){
        List<Pedido> categoriaList = pedidoRepository.findAll();
        if(categoriaList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoriaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        Optional<Pedido> Estoque = pedidoRepository.findById(id.intValue());
        if(Estoque.isEmpty()){
            ResponseMessage responseMessage = new ResponseMessage("Pedido não encontrado para o ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        return ResponseEntity.ok(Estoque);

    }
}