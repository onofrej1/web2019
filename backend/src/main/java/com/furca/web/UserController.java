package com.furca.web;

import com.furca.model.User;
import com.furca.service.SecurityService;
import com.furca.service.UserService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    //@Autowired
    //private UserValidator userValidator;

    @PostMapping("/registration")
    public String registration(@Valid @RequestBody User user, BindingResult result) {
        //userValidator.validate(userForm, bindingResult);
    	if (result.hasErrors()) {     
    		//System.out.print(result.getFieldError("firstName").getDefaultMessage());
            System.out.print(result.getFieldError("lastName").getDefaultMessage());
            return "redirect:/chyba";            
        }  

        System.out.print(user.getUsername());
        userService.save(user);

        //securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    /* @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }*/
}

