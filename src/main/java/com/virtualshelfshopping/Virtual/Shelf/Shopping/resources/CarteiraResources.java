package com.virtualshelfshopping.Virtual.Shelf.Shopping.resources;

import com.virtualshelfshopping.Virtual.Shelf.Shopping.entity.Carteira;
import com.virtualshelfshopping.Virtual.Shelf.Shopping.repository.CarteiraRepository;
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
@RequestMapping("/api/v1/carteiras")
@CrossOrigin(origins = "http://localhost:9000")
public class CarteiraResources {

    @Autowired
    private CarteiraRepository carteiraRepository;

    @PostMapping
    public ResponseEntity<Carteira> salvarCarteira(@RequestBody Carteira carteira)
    {
        Carteira saved = carteiraRepository.save(carteira);
        URI uri = RestUtil.getUri(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarCarteira(@PathVariable("id") Long id, @RequestBody Carteira carteira)
    {
        Optional<Carteira> optionalCarteira = carteiraRepository.findById(id);

        if (optionalCarteira.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Carteira não encontrada"));
        }
        Carteira existingCarteira = optionalCarteira.get();

        existingCarteira.setSaldo(carteira.getSaldo());

        carteiraRepository.save(existingCarteira);
        ResponseMessage responseMessage = new ResponseMessage(String.format("Carteira com ID %d atualizada", id));
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping
    public ResponseEntity<List<Carteira>> get()
    {
        List<Carteira> carteiraList = carteiraRepository.findAll();
        if (carteiraList.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carteiraList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id)
    {
        Optional<Carteira> carteira = carteiraRepository.findById(id);
        if (carteira.isEmpty())
        {
            ResponseMessage responseMessage = new ResponseMessage("Carteira não encontrada para o ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        return ResponseEntity.ok(carteira.get());
    }


}