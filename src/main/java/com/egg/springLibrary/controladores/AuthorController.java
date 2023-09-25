package com.egg.springLibrary.controladores;

import com.egg.springLibrary.entidades.Author;
import com.egg.springLibrary.entidades.Book;
//import com.egg.springLibrary.entidades.Author;
import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.servicios.AuthorService;
import com.egg.springLibrary.servicios.BookService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author irina
 */
@Controller
@RequestMapping("/author")
public class AuthorController {    //localhost:8080/author

    @Autowired
    private AuthorService aServ;
    @Autowired
    private BookService bServ;

    @GetMapping("/form")
    public String author() {  //localhost:8080/author/form
        return "author_form.html";
    }

    @PostMapping("/register")
    public String register(@RequestParam String nameAuthor, @RequestParam("imageAuthor") MultipartFile imageAuthor, ModelMap model) {
        try {

            //guardar nombre de la imagen por default
            String nombreFoto = "default.png";

            if (!imageAuthor.isEmpty()) {
                //debemos traer el directorio de la imagen
                Path imageDirectory = Paths.get("src//main//resources//static/IMG/imgAutores");
                //ruta absoluta
                String rutaAbs = imageDirectory.toFile().getAbsolutePath();
                
                //sacar la extension de la imagen
                int index = imageAuthor.getOriginalFilename().indexOf(".");
                String extension = "";
                extension = "."+imageAuthor.getOriginalFilename().substring(index+1);

                nombreFoto = Calendar.getInstance().getTimeInMillis()+extension;



                //realizamos la ruta completa
                Path completeDirectory = Paths.get(rutaAbs + "//" + nombreFoto);
                
                Files.write(completeDirectory, imageAuthor.getBytes());
                
                
            }

            aServ.createAuthor(nameAuthor,  nombreFoto);

        } catch (MyException ex) {
//            Logger.getLogger(AuthorController.class.getName()).log(Level.SEVERE, null, ex);
            model.put("error", ex.getMessage());
            return "author_form.html";
        } catch (IOException ex) { 
            model.put("error", "ERROR EN LA IMAGEN");
            return "author_form.html";
        }
        //caso de que no de una excepcion, retorne al index
        return "index.html";
    }

    /*Para mostrar todos los autores*/
    @GetMapping("/view")
    public String viewAuthors(ModelMap model) {
        //List<Author> authors = aServ.listAllAuthors();
        
        //anclar al modelo para que wse envie a la interfaz de usuario
        model.addAttribute("authors", aServ.listAllAuthors());
        return "authorView.html";
    }

    @GetMapping("/edit/{id}")
    public String editAuthor(@PathVariable String id, ModelMap model){ //el id debe de viajar
        model.put("author", aServ.getOne(id));

        return "authorEdit.html";
    } 


    @PostMapping("/edit/{id}")
    public String editAuthor(@PathVariable String id, @RequestParam String nameAuthor, @RequestParam("imageAuthor") MultipartFile imageAuthor, ModelMap model){
        try {
            Author autor = aServ.getOne(id);
            String nombreFoto = autor.getImage();

            if (!imageAuthor.isEmpty()) {
                //debemos traer el directorio de la imagen
                Path imageDirectory = Paths.get("src//main//resources//static/IMG/imgAutores");
                //ruta absoluta
                String rutaAbs = imageDirectory.toFile().getAbsolutePath();

                //sacar la extension de la imagen
                int index = imageAuthor.getOriginalFilename().indexOf(".");
                String extension = "";
                extension = "."+imageAuthor.getOriginalFilename().substring(index+1);

                nombreFoto = Calendar.getInstance().getTimeInMillis()+extension;



                //realizamos la ruta completa
                Path completeDirectory = Paths.get(rutaAbs + "//" + nombreFoto);
                
                Files.write(completeDirectory, imageAuthor.getBytes());
                
                
            } 

            aServ.updateOnlyOneAuthor(id, nameAuthor, nombreFoto);

        } catch (MyException ex) {
            model.put("error", ex.getMessage());
            return "authorEdit.html";
        } catch (IOException ex) { 
            model.put("error", "ERROR EN LA IMAGEN");
            return "authorEdit.html";
        }

        //si todo sale bien
        return "redirect:../view";
    }

    //Vamos a eliminar un autor
    @GetMapping("/deleteAuthor/{id}")
    public String deleteAuthor(@PathVariable String id, ModelMap model){
       model.put("author", aServ.getOne(id));
       return "Delete.html";
    }


    @PostMapping("/deleteAuthor/{id}")
    public String deleteAnAuthor(@PathVariable String id, ModelMap model){
        try {

            //comprobamos que la lista de los libro no este vacia
            if(!bServ.listAllBooksByAuthor(aServ.getOne(id).getName()).isEmpty()){
                //en el caso de que no este vacia, se procede a eliminar los libros por autor
                bServ.deleteBookByAuthorRepo(id);
            }

            //Elimina el autor
            aServ.deleteAnAuthor(id);
            //Redirecciona a la vista de autores
            return "redirect:../view";

        }catch(MyException ex){
            model.put("error", ex.getMessage());
            return "Delete.html";
        }
    }
}

