package com.codecool.crudDemo.controller;

import com.codecool.crudDemo.model.User;
import com.codecool.crudDemo.repositiory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class RootController {

    private UserRepository userRepository;

    @Autowired
    public RootController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/index")
    String showUsers(Model model) {
        if (userRepository.count() > 0) {
            model.addAttribute("users", userRepository.findAll());
        } else {
            model.addAttribute("users", null);
        }
        return "index";
    }

    @GetMapping("/new")
    String showCreateForm(User user) {
        return "new-user";
    }

    @PostMapping("/create")
    String createUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "new-user";
        }
        userRepository.save(user);
        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    String updateUser (@PathVariable("id") Integer id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-user";
        }
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/index";
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    String deleteUser (@PathVariable("id") Integer id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepository.findAll());
            return "index";
        }
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/index";
    }

//    /* It updates model object. */
//    @RequestMapping(value="/editsave",method = RequestMethod.POST)
//    public String editsave(@ModelAttribute("emp") Emp emp){
//        dao.update(emp);
//        return "redirect:/viewemp";
//    }
//    /* It deletes record for the given id in URL and redirects to /viewemp */
//    @RequestMapping(value="/deleteemp/{id}",method = RequestMethod.GET)
//    public String delete(@PathVariable int id){
//        dao.delete(id);
//        return "redirect:/viewemp";
//    }
}
