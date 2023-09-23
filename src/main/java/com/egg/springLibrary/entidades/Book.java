/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.springLibrary.entidades;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 * @author irina+
 * Data reemplaza el getter y setter
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data 
public class Book {
    @Id
    private Long isbn;
    
    
    private String title;
    private Integer copies;
    private LocalDate registration;
    
    // Atributo para el archivo .epub o .pdf
    private String ebook;

    // Atributo para la imagen
    private String coverImage;
    
    @ManyToOne
    private Author author;
    
    @ManyToOne
    private Editorial editorial;
}
