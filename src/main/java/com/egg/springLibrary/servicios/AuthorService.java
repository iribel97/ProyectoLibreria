/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.springLibrary.servicios;

import com.egg.springLibrary.entidades.Author;
import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.repositorios.AuthorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author irina
 */
@Service
public class AuthorService {
    // Instanciar los repositorios como variables globales
    @Autowired
    private AuthorRepository aRepo;
    
    // CREATE AN AUTHOR -------------------------------------------------------------------------------------------------------
    @Transactional
    public void createAuthor(String nameA, String imageA) throws MyException{
        //VALIDAR LA EXISTENCIA DE DATOS
        validar(nameA);
        
        // INSTANCIAR EL OBJETO AUTHOR
        Author author = new Author();
        
        // setear atributos del objeto (el id no porque se autogenera)
        author.setName(nameA);
        author.setImage(imageA);
        
        //guardar en la bd
        aRepo.save(author);
    }
    
    // TAKE ALL AUTHORS -------------------------------------------------------------------------------------------------------
    public List<Author> listAllAuthors(){
        List<Author> authors = new ArrayList<>();
        authors = aRepo.findAll();
        //Metodo propio del jpaRepo es traer todos los datos de la tabla con el ".findAll()"
        return authors;
    }
    
    // UPDATE AUTHOR ----------------------------------------------------------------------------------------------------------
    @Transactional
    public void updateOnlyOneAuthor(String idA, String nameA, String imageA) throws MyException{
        
        //validar datos
        validar(nameA);
        
        // en caso de que el id del autor este mal digitado o que no se encuentre, se debe de usar un optional
        Optional<Author> answerA = aRepo.findById(idA);
        
        // verificamos si el optional devuelve algo
        if (answerA.isPresent()) {
            // le pasamos el resultado a un objeto del tipo autor
            Author author = answerA.get();
            
            //seteamos el valor del nombre
            author.setName(nameA);
            author.setImage(imageA);
            
            //guardamos
            aRepo.save(author);
        }
    }

    
    // TRAER UN SOLO DATO DE AUTOR --------------------------------------------------------------------------------------------------
    public Author getOne(String id){
        return aRepo.getOne(id);
    }


    //ELIMINAR AUTOR
    @Transactional
    public void deleteAnAuthor(String idAuthor) throws MyException{
        //Verificar que el autor exista
        Optional<Author> answerA = aRepo.findById(idAuthor);

        //caso de que el autor si exista
        if(answerA.isPresent()){
            //traemos el autor
            Author author = answerA.get();

            //se elimina el autor por un metodo que tiene el repositorio por default
            aRepo.delete(author);
        }
    }
    
    // METODO PARA MANEJAR LA EXCEPCION ----------------------------------------------------------------------------------------------
    private void validar( String nameA) throws MyException {

        
        if (nameA.isEmpty() || nameA == null) {
            throw new MyException("el nombre del autor no puede ser nulo o estar vacio");
        }

    }

}
