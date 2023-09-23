
package com.egg.springLibrary.servicios;

import com.egg.springLibrary.entidades.Editorial;
import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.repositorios.EditorialRepository;
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
public class EditorialService {
    // intanciar los repositorios como variables globales
    @Autowired
    private EditorialRepository eRepo;
    
    
    // CREATE AN EDITORIAL ----------------------------------------------------------------------------------------------------
    @Transactional
    public void createEditorial(String nameE) throws MyException{
        //Validar datos
        validar(nameE);
        
        // INSTANCIAR OBJETO DE TIPO EDITORIAL
        Editorial editorial = new Editorial();
        
        //seteamos los atributos
        editorial.setName(nameE);
        
        //guardar en la bd
        eRepo.save(editorial);
    }
    
    //TAKE ALL EDITORIALS ----------------------------------------------------------------------------------------------------
    public List<Editorial> listAllEditorials(){
        List<Editorial> editorials = new ArrayList<>();
        
        editorials = eRepo.findAll();
        //Metodo propio del jpaRepo es traer todos los datos de la tabla con el ".findAll()"
        return editorials;
    }
    
    // UPDATE EDITORIAL ----------------------------------------------------------------------------------------------------------
    @Transactional
    public void updateOnlyOneEditorial(String idE, String nameE) throws MyException{
        //Validar
        validar(nameE);
        // en caso de que el id de la editorial este mal digitado o que no se encuentre, se debe de usar un optional 
        Optional<Editorial> answerE = eRepo.findById(idE);
        
        //comprobar de que si exista un dato con el mismo id
        if (answerE.isPresent()) {
            //instanciamos un objeto de tipo Editorial
            Editorial editorial = answerE.get();
            
            //seteamos el nombre, porque el id no hay como modificarle
            editorial.setName(nameE);
            
            //guardamos
            eRepo.save(editorial);
        }
    }

    // TRAER UNA SOLO EDITORIAL --------------------------------------------------------------------------------------------------
    public Editorial getOne(String id){
        return eRepo.getOne(id);
    }
    
     // METODO PARA MANEJAR LA EXCEPCION ----------------------------------------------------------------------------------------------
    private void validar( String nameE) throws MyException {

        
        if (nameE.isEmpty() || nameE == null) {
            throw new MyException("el nombre del autor no puede ser nulo o estar vacio");
        }

    }
}
