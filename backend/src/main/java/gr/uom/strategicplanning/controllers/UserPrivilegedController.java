//package gr.uom.strategicplanning.controllers;
//
//import gr.uom.strategicplanning.models.User;
//import gr.uom.strategicplanning.services.UserPrivilegedService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//
//@Controller
//@RequestMapping("/admin")
//public class UserPrivilegedController {
//
//    @Autowired
//    private UserPrivilegedService userPrivilegedService;
//
//    @PutMapping("/verify")
//    User verifyUser(@RequestParam String email){
//        return userPrivilegedService.verifyUser(email);
//    }
//
//    @PutMapping("/authorize")
//    User givePrivilegeToUser(@RequestParam String email){
//        return userPrivilegedService.givePrivilegeToUser(email);
//    }
//}
