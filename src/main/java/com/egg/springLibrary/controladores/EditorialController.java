
package com.egg.springLibrary.controladores;

import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.servicios.EditorialService;
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

/**
 *
 * @author irina
 */
@Controller
@RequestMapping("/editorial")
public class EditorialController {
    
    @Autowired
    private EditorialService eServ;
    
    @GetMapping("/form")
    public String author(){  //localhost:8080/editorial/form
        return "editorialForm.html";
    }
    
    
    @PostMapping("/register")
    public String register(@RequestParam String nameEditorial, ModelMap model){
        try {
            eServ.createEditorial( nameEditorial);
        } catch (MyException ex) {
//            Logger.getLogger(EditorialController.class.getName()).log(Level.SEVERE, null, ex);
            
            model.put("error", ex.getMessage());
            return "editorialForm.html";
        }
        return "index.html";
    }

    @GetMapping("/view")
    public String viewEditorial(ModelMap model){
        model.addAttribute("editorials", eServ.listAllEditorials());
        return "editorialView.html";
    }

    @GetMapping("/edit/{id}")
    public String editEditorial(@PathVariable String id, ModelMap model){
        model.addAttribute("editorial", eServ.getOne(id));
        return "editorialEdit.html";
    }

    @PostMapping("/edit/{id}")
    public String editEditorial(@PathVariable String id, @RequestParam String nameEditorial, ModelMap model){
        try {
            eServ.updateOnlyOneEditorial(id, nameEditorial);
        } catch (MyException ex) {
            model.put("error", ex.getMessage());
            return "editorialEdit.html";
        }

        return "redirect:../view";
    }

}
