package com.example.files.services;

import com.example.files.models.Libro;
import com.example.files.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class LibroService {

    @Autowired
    LibroRepository libroRepository;
    @Autowired
    private UploadService uploadService;

    String url = "http://localhost:8080/uploads/";

    public List<Libro> obtenerTodo(){
        List<Libro> libros = libroRepository.findAll();
        libros = libros.stream().map(libro -> {libro.setNombre(url+libro.getNombre());
            return libro;
        }).collect(Collectors.toList());

        return libros;
    }

    public Libro crearLibro(Libro libro, MultipartFile file) throws IOException {
        validarArchivo(file);
        String nombre = uploadService.saveUpload(file);
        libro.setNombre(nombre);
        return libroRepository.save(libro);
    }

    public Libro actualizarLibro(Libro libro, MultipartFile file) throws IOException {
        if (libro.getId() != null) {
            validarArchivo(file);
            String nombre = uploadService.saveUpload(file);
            libro.setNombre(nombre);
            return libroRepository.save(libro);
        }
        return null;
    }

    private void validarArchivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException();
        }

        String contentType = file.getContentType();
        if (!"image".equals(contentType)) {
            throw new IllegalArgumentException();
        }
    }


    public void borrarLibro(Long id){
        Libro libro = libroRepository.findById(id).get();
        String nombre = libro.getNombre();
        uploadService.deleteUpload(nombre);
        libroRepository.delete(libro);
    }
}
