package com.egg.springLibrary.servicios;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.springLibrary.entidades.UserEn;
import com.egg.springLibrary.enumeradores.Rol;
import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.repositorios.UserRepository;


@Service
public class UserService implements UserDetailsService{
    
    @Autowired
    private UserRepository uRepo;

    //Metodo para registrar al usuario
    @Transactional
    public void registrar(String nombre, String email, String password, String password2) throws MyException{
        validar(nombre, email, password, password2);
        
        UserEn user = new UserEn();
        
        user.setName(nombre);
        user.setEmail(email);
        user.setPassword(password);

        user.setRol(Rol.USER);
        
        uRepo.save(user);
    }


    private void validar(String nombre, String email, String password, String password2) throws MyException{
        if(nombre == null || nombre.isEmpty()){
            throw new MyException("El nombre no puede estar vacio");
        }
        if(email == null || email.isEmpty()){
            throw new MyException("El email no puede estar vacio");
        }
        if(password == null || password.isEmpty() || password.length() < 6){
            throw new MyException("La contraseña no puede estar vacia, y debe de ser mas de 5 caracteres");
        }
        if(password2 == null || password2.isEmpty()){
            throw new MyException("La contraseña no puede estar vacia");
        }
        if(!password.equals(password2)){
            throw new MyException("Las contraseñas no coinciden");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEn usuario = uRepo.findByEmail(email);
        if(usuario != null){
            List<GrantedAuthority> permissions = new ArrayList<>(); //Contenedor de Permisos

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); //Permisos

            permissions.add(p); //Agregamos los permisos al contenedor

            return new User(usuario.getEmail(), usuario.getPassword(), permissions); //Retornamos el usuario con sus permisos
        } else {
            return null;
        }
    }

}
