/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.springLibrary.servicios;

import com.egg.springLibrary.entidades.*;
import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.repositorios.*;
import java.time.LocalDate;
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
public class BookService {
    // Instanciar los repositorios como variables globales
    @Autowired
    private BookRepository bRepo;
    @Autowired
    private AuthorRepository aRepo;
    @Autowired
    private EditorialRepository eRepo;
    
    // CREATE A BOOK ----------------------------------------------------------------------------------------------------------
    @Transactional
    public void createBook(Long isbnB, String titleB, Integer copiesB, String documentB, String imageB, String idAuthor, String idEditorial) throws MyException{
        //VALIDAR QUE LOS ATRIBUTOS NO ESTEN VACIOS
        validar(isbnB, titleB, copiesB, idAuthor, idEditorial);
        
        // INSTANCIAR EL OBJETO LIBRO
        Book book = new Book();
        
            //Metodo propio del jpaRepo, encontrar por ID
        Author author = aRepo.findById(idAuthor).get();
        Editorial editorial = eRepo.findById(idEditorial).get();
        
        // setear los datos
        book.setIsbn(isbnB);
        book.setTitle(titleB);
        book.setCopies(copiesB);
        book.setCoverImage(imageB);
        book.setEbook(documentB);
        
        book.setRegistration(LocalDate.now());
        
        book.setAuthor(author);
        book.setEditorial(editorial);
        
        // para guardar en la bd
        bRepo.save(book);
    }
    
    
    // TAKE ALL BOOKS ---------------------------------------------------------------------------------------------------------
    public List<Book> listAllBooks(){
        
        List<Book> books = new ArrayList<>();
        
        books = bRepo.findAll();
        //Metodo propio del jpaRepo es traer todos los datos de la tabla con el ".findAll()"
        return books;
    }

    //TRAER TODOS LOS LIBROS POR AUTOR -----------------------------------------------------------------------------------------
    public List<Book> listAllBooksByAuthor(String nameAuthor){
        List<Book> books = new ArrayList<>();
        
        books = bRepo.findBookByAuthor(nameAuthor);
        
        return books;
    }

    //TRAER TODOS LOS LIBROS POR EDITORIAL -----------------------------------------------------------------------------------------
    public List<Book> listAllBooksByEditorial(String nameEditorial){
        List<Book> books = new ArrayList<>();
        
        books = bRepo.findBookByEditorial(nameEditorial);
        
        return books;
    }
    
    // UPDATE BOOK -----------------------------------------------------------------------------------------------------------
    @Transactional
    public void updateOnlyOneBook(Long isbnB, String titleB, String documentB, String imageB, String idAuthorB, String idEditorialB, Integer copiesB) throws MyException{
        
        //VALIDAR QUE LOS ATRIBUTOS NO ESTEN VACIOS
        validar(isbnB, titleB, copiesB, idAuthorB, idEditorialB);
        
        //caso de que el isbn este mal tipeado o que no exista el isbn, se debe de poner el optional
        Optional<Book> answerB = bRepo.findById(isbnB);
        //hacer un optional para el autor y la editorial para el mismo caso del isbn
        Optional<Author> answerA = aRepo.findById(idAuthorB);
        Optional<Editorial> answerE = eRepo.findById(idEditorialB);
        
        //instanciamos objetos de las clase autor y editorial
        Author author;
        Editorial editorial;
        
        //Comprobar que todos 3 optional contienen algo
        if (answerB.isPresent() && answerA.isPresent() && answerE.isPresent()) {
            //en caso de que si encuentre
            Book book = answerB.get(); //Instanciar un objeto de tipo libro
            
            author = answerA.get(); // le pasamos el autor del optional al objeto que ya instanciamos 
            editorial = answerE.get(); // le pasamos la editorial del optional al objeto que ya instanciamos 
            
            //seteamos todos los valores del libro
            book.setTitle(titleB); 
            book.setAuthor(author);
            book.setCopies(copiesB);
            book.setEditorial(editorial);
            book.setCoverImage(imageB);
            book.setEbook(documentB);
            
            //le guardamos
            bRepo.save(book);
        }
    }

    //ELIMINAR LIBRO POR ID ---------------------------------------------------------------------------------------------------
    @Transactional
    public void deleteABookByID(Long isbnB) throws MyException{
        //caso de que el isbn este mal tipeado o que no exista el isbn, se debe de poner el optional
        Optional<Book> answerB = bRepo.findById(isbnB);

        //Comprobar que si tenga algo
        if (answerB.isPresent()) {
            //en caso de que si encuentre
            Book book = answerB.get(); //Instanciar un objeto de tipo libro
            
            //le borramos
            bRepo.delete(book);
        }

    }

    //ELIMINAR LIBRO POR AUTOR ---------------------------------------------------------------------------------------------------
    @Transactional
    public void deleteBookByAuthorRepo(String idAuthor) throws MyException{
        //caso de que el isbn este mal tipeado o que no exista el isbn, se debe de poner el optional
        Optional<Author> answerA = aRepo.findById(idAuthor);

        //Comprobar si trae algo
        if(answerA.isPresent()){
            Author author = answerA.get();
            /*
            List<Book> books = bRepo.findAll();

            for(Book book : books){
                if(book.getAuthor().getId().equals(author.getId())){
                    bRepo.delete(book);
                }
            }
            */
            bRepo.deleteBookByAuthor(author.getId());
        } 
    }

    //ELIMINAR LIBRO POR EDITORIAL ---------------------------------------------------------------------------------------------------
    @Transactional
    public void deleteBookByEditorial(String idEditorial) throws MyException{
        //caso de que el id de la editorial este mal tipeado o que no exista el isbn, se debe de poner el optional
        Optional<Editorial> answerE = eRepo.findById(idEditorial);

        //Comprobar si trae algo
        if(answerE.isPresent()){
            Editorial editorial = answerE.get();

            bRepo.deleteBookByEditorial(editorial.getId());
        }
    }

    // TRAER UN SOLO DATO DE AUTOR --------------------------------------------------------------------------------------------------
    public Book getOne(Long isbn){
        return bRepo.getOne(isbn);
    }
    
    // METODO PARA MANEJAR LA EXCEPCION ----------------------------------------------------------------------------------------------
    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditor) throws MyException {

        if (isbn == null) {
            throw new MyException("el isbn no puede ser nulo");
        }
        if (titulo.isEmpty() || titulo == null) {
            throw new MyException("el titulo no puede ser nulo o estar vacio");
        }
        if (ejemplares == null) {
            throw new MyException("ejemplares no puede ser nulo");
        }
        
        if (idAutor.isEmpty() || idAutor == null) {
            throw new MyException("el Autor no puede ser nulo o estar vacio");
        }

        if (idEditor.isEmpty() || idEditor == null) {
            throw new MyException("La Editorial no puede ser nula o estar vacia");
        }
    }
}
