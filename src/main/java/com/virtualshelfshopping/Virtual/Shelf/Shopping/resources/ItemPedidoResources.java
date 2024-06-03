package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.ItemPedido;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.ItemPedidoRepository;
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
@RequestMapping("/api/v1/itenspedidos")
public class ItemPedidoResources {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @PostMapping
    public ResponseEntity<ItemPedido> salvarItemPedido(@RequestBody ItemPedido itemPedido) {
        ItemPedido saved = itemPedidoRepository.save(itemPedido);

        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarItemPedido(@PathVariable("id") Long id, @RequestBody ItemPedido itemPedido) {
        Optional<ItemPedido> optionalItemPedido = itemPedidoRepository.findById(Math.toIntExact(id));

        if (optionalItemPedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ItemPedido existingItemPedido = optionalItemPedido.get();

        existingItemPedido.setQuantidade(itemPedido.getQuantidade()); // Atualize apenas o campo necessário
        existingItemPedido.setSubTotal(itemPedido.getSubTotal());

        itemPedidoRepository.save(existingItemPedido);
        ResponseMessage responseMessage = new ResponseMessage(String.format("Item Pedido com ID %d atualizado", id));
        return ResponseEntity.ok(responseMessage);
    }



    @GetMapping
    public ResponseEntity<List<ItemPedido>> get(){
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        if(itemPedidoList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itemPedidoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        Optional<ItemPedido> Estoque = itemPedidoRepository.findById(id.intValue());
        if(Estoque.isEmpty()){
            ResponseMessage responseMessage = new ResponseMessage("Item Pedido não encontrado para o ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        return ResponseEntity.ok(Estoque);

    }
}