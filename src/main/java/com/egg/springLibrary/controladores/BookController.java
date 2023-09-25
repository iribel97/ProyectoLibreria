
package com.egg.springLibrary.controladores;


import com.egg.springLibrary.entidades.Book;
import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.servicios.AuthorService;
import com.egg.springLibrary.servicios.BookService;
import com.egg.springLibrary.servicios.EditorialService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

//import java.util.List;
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
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookServ;
    @Autowired
    private AuthorService authorServ;
    @Autowired
    private EditorialService editorialServ;
    
    //METODO QUE RETORNE LA PAGINA DEL FORM
    @GetMapping("/form")
    public String formBook(ModelMap model){
        //Llamar a los autores que ya tenemos creados
        //List<Editorial> editorials = editorialServ.listAllEditorials();
        
        //anclar al modelo para que wse envie a la interfaz de usuario
        model.addAttribute("authors", authorServ.listAllAuthors());
        model.addAttribute("editorials", editorialServ.listAllEditorials());
        
        return "bookForm.html";
    }
    
    
    //METODO PARA REGISTRAR EL LIBRO EN LA BASE DE DATOS MEDIANTE EL SERVICE
        //Tener en cuenta que los parametros tengan el mismo nombre que el atributo name de los input en el HTML
    @PostMapping("/register")
    public String register(@RequestParam(required = false) Long isbnBook,@RequestParam String titleBook,
            @RequestParam(required = false) Integer copiesBook,@RequestParam MultipartFile documentBook, 
            @RequestParam MultipartFile imageBook,
            @RequestParam String authorBook,
            @RequestParam String editorialBook, ModelMap model){
        
        try {

            //guardar nombre de la imagen por default
            String nombreFoto = "default.png";

            if (!imageBook.isEmpty()) {
                //debemos traer el directorio de la imagen
                Path imageDirectory = Paths.get("src//main//resources//static/IMG/imgPortadas");
                //ruta absoluta
                String rutaAbs = imageDirectory.toFile().getAbsolutePath();
                
                //sacar la extension de la imagen
                int index = imageBook.getOriginalFilename().indexOf(".");
                String extension = "";
                extension = "."+imageBook.getOriginalFilename().substring(index+1);

                nombreFoto = Calendar.getInstance().getTimeInMillis()+extension;



                //realizamos la ruta completa
                Path completeDirectory = Paths.get(rutaAbs + "//" + nombreFoto);
                
                Files.write(completeDirectory, imageBook.getBytes());
            }
            
            //Caso del libro
            if (!documentBook.isEmpty()) {
                //debemos traer el directorio de los libros
                Path bookDirectory = Paths.get("src//main//resources//static/BookDocument");
                //ruta absoluta
                String rutaBookAbs = bookDirectory.toFile().getAbsolutePath();
                //pasamos a bytes la imagen que ingresa
                byte[] byteBook = documentBook.getBytes();
                //realizamos la ruta completa
                Path completeBookDirectory = Paths.get(rutaBookAbs + "//" + documentBook.getOriginalFilename());
                
                Files.write(completeBookDirectory, byteBook);
                
            }
            
            bookServ.createBook(isbnBook, titleBook, copiesBook,documentBook.getOriginalFilename(),nombreFoto, authorBook, editorialBook);
        } catch (MyException ex) {
//            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            model.addAttribute("authors", authorServ.listAllAuthors());
            model.addAttribute("editorials", editorialServ.listAllEditorials());
            model.put("error", ex.getMessage());
            return "bookForm.html";
        } catch (IOException ex) {
            model.addAttribute("authors", authorServ.listAllAuthors());
            model.addAttribute("editorials", editorialServ.listAllEditorials());
            model.put("error", "Error al procesar los archivos.");
            return "bookForm.html";
        }
        return "index.html";
    }
    
    //Metodo para ver los libros
    @GetMapping("/view")
    public String viewBooks(ModelMap model){
        //Llamar a los autores que ya tenemos creados
        //List<Book> books = bookServ.listAllBooks();
        
        //anclar al modelo para que wse envie a la interfaz de usuario
        model.addAttribute("books", bookServ.listAllBooks());
        
        return "bookView.html";
    }


    @GetMapping("/edit/{isbnBook}")
    public String editBook(@PathVariable Long isbnBook, ModelMap model){ //el id debe de viajar
        model.put("book", bookServ.getOne(isbnBook));

        //anclar al modelo para que wse envie a la interfaz de usuario
        model.addAttribute("authors", authorServ.listAllAuthors());
        model.addAttribute("editorials", editorialServ.listAllEditorials());

        return "bookEdit.html";
    } 

    @PostMapping("/edit/{isbnBook}")
    public String editBook(@PathVariable Long isbnBook, @RequestParam String titleBook,
            @RequestParam(required = false) Integer copiesBook,@RequestParam MultipartFile documentBook, 
            @RequestParam MultipartFile imageBook,
            @RequestParam String authorBook,
            @RequestParam String editorialBook, ModelMap model){
        try {
//anclar al modelo para que wse envie a la interfaz de usuario
        model.addAttribute("authors", authorServ.listAllAuthors());
        model.addAttribute("editorials", editorialServ.listAllEditorials());

            //Buscamos el libro antes de editarlo
            Book libro = bookServ.getOne(isbnBook);

            //guardar nombre de la imagen ydocumento preexistente
            String nombreFoto = libro.getCoverImage();
            String nombreDocumento = libro.getEbook();

            if (!imageBook.isEmpty()) {
                //debemos traer el directorio de la imagen
                Path imageDirectory = Paths.get("src//main//resources//static/IMG/imgPortadas");
                //ruta absoluta
                String rutaAbs = imageDirectory.toFile().getAbsolutePath();
                
                //sacar la extension de la imagen
                int index = imageBook.getOriginalFilename().indexOf(".");
                String extension = "";
                extension = "."+imageBook.getOriginalFilename().substring(index+1);

                nombreFoto = Calendar.getInstance().getTimeInMillis()+extension;



                //realizamos la ruta completa
                Path completeDirectory = Paths.get(rutaAbs + "//" + nombreFoto);
                
                Files.write(completeDirectory, imageBook.getBytes());
            }
            
            //Caso del libro
            if (!documentBook.isEmpty()) {
                //debemos traer el directorio de los libros
                Path bookDirectory = Paths.get("src//main//resources//static/BookDocument");
                //ruta absoluta
                String rutaBookAbs = bookDirectory.toFile().getAbsolutePath();
                //sacar la extension de la imagen
                int index = documentBook.getOriginalFilename().indexOf(".");
                String extension = "";
                extension = "."+documentBook.getOriginalFilename().substring(index+1);
                nombreDocumento = documentBook.getOriginalFilename() + extension;
                //realizamos la ruta completa
                Path completeBookDirectory = Paths.get(rutaBookAbs + "//" + documentBook.getOriginalFilename());
                
                Files.write(completeBookDirectory, documentBook.getBytes());
                
            }
            
            bookServ.updateOnlyOneBook(isbnBook, titleBook,nombreDocumento,nombreFoto, authorBook, editorialBook, copiesBook);
        } catch (MyException ex) {
//            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            model.addAttribute("authors", authorServ.listAllAuthors());
            model.addAttribute("editorials", editorialServ.listAllEditorials());
            model.put("error", ex.getMessage());
            return "bookEdit.html";
        } catch (IOException ex) {
            model.addAttribute("authors", authorServ.listAllAuthors());
            model.addAttribute("editorials", editorialServ.listAllEditorials());
            model.put("error", "Error al procesar los archivos.");
            return "bookEdit.html";
        }
        return "redirect:../view";
    }

    // METODO PARA ELIMINAR LIBRO --------------------------------------------------------------------
    @GetMapping("/deleteBook/{isbn}")
    public String deleteBook(@PathVariable Long isbn, ModelMap model){
        model.put("book", bookServ.getOne(isbn));

        return "Delete.html";
    }

    @PostMapping("/deleteBook/{isbn}")
    public String deleteABook(@PathVariable Long isbn , ModelMap model){
        try{
            bookServ.deleteABookByID(isbn);
            return "redirect:../view";
        }catch(MyException ex){
            return "Delete.html";
        }
    }
}
