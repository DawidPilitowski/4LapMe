package pl.lapme.adoption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(path = "/about")
    public String getAboutSite (){
        return "about";
    }
    @GetMapping("/contact")
    public String getContactSite () {
        return "contact";
    }
    @GetMapping("/help")
    public String getHelpSite () {
        return "help";
    }
    @GetMapping("/list")
    public String getListAnimal () {
        return "itemList";
    }
    @GetMapping("/faq")
    public String faq () {
        return "faq";
    }
    @GetMapping("/others")
    public String others () {
        return "others";
    }
    @GetMapping("/rules")
    public String rules () {
        return "rules";
    }

}
