package com.example.files.controllers;

import com.example.files.models.Libro;
import com.example.files.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/libros")
@CrossOrigin("*")

public class LibroController {
    @Autowired
    LibroService libroService;


    @GetMapping("/all")
    public ResponseEntity<List<Libro>> getLibros(){
        return new ResponseEntity<>(libroService.obtenerTodo(), HttpStatus.OK);
    }

    @PostMapping("/crear")
    public ResponseEntity<Libro> crearLibro(@RequestPart MultipartFile file, @RequestPart Libro libro) {
        try {
            return new ResponseEntity<>(libroService.crearLibro(libro, file), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Libro> update(@RequestPart Libro libro, @RequestPart MultipartFile file) {
        try {
            Libro libroActualizado = libroService.actualizarLibro(libro, file);
            return libroActualizado != null ? new ResponseEntity<>(libroActualizado, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        libroService.borrarLibro(id);
        return new ResponseEntity<>( HttpStatus.OK) ;
    }


}
