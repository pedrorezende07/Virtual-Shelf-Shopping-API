package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.dto.PaymentRequest;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.dto.PedidoItem;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Carteira;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Estoque;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Pedido;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Usuario;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.CarteiraRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.EstoqueRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.PedidoRepository;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.UsuarioRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

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

    @PostMapping("/checkout")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(paymentRequest.getEmail());
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Email ou senha inválidos"));
        }

        Usuario usuario = optionalUsuario.get();
        if (!usuario.getSenha().equals(paymentRequest.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Email ou senha inválidos"));
        }

        Optional<Carteira> optionalCarteira = carteiraRepository.findByUsuarioId(usuario.getId());
        if (optionalCarteira.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Carteira não encontrada"));
        }

        Carteira carteira = optionalCarteira.get();
        if (carteira.getSaldo() < paymentRequest.getTotalValue()) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(new ResponseMessage("Saldo insuficiente"));
        }

        // Subtrair o valor do saldo da carteira
        carteira.setSaldo((float) (carteira.getSaldo() - paymentRequest.getTotalValue()));
        carteiraRepository.save(carteira);

        // Atualizar o estoque dos produtos
        for (PedidoItem item : paymentRequest.getPedidoItems()) {
            Optional<Estoque> optionalEstoque = estoqueRepository.findById(item.getProdutoId());
            if (optionalEstoque.isPresent()) {
                Estoque estoque = optionalEstoque.get();
                if (estoque.getQuantProduto() < item.getQuantidade()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Quantidade insuficiente no estoque para o produto ID: " + item.getProdutoId()));
                }
                estoque.setQuantProduto(estoque.getQuantProduto() - item.getQuantidade());
                estoqueRepository.save(estoque);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Produto não encontrado no estoque para ID: " + item.getProdutoId()));
            }
        }

        // Aqui você pode criar e salvar um novo Pedido se necessário

        return ResponseEntity.ok(new ResponseMessage("Pagamento realizado com sucesso"));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> get() {
        List<Pedido> pedidosList = pedidoRepository.findAll();
        if (pedidosList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidosList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id.intValue());
        if (pedido.isEmpty()) {
            ResponseMessage responseMessage = new ResponseMessage("Pedido não encontrado para o ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        return ResponseEntity.ok(pedido);
    }
}
