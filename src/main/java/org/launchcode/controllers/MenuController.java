package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String  index(Model model) {
        model.addAttribute("menu", menuDao.findAll());
        model.addAttribute("title", "Menu");
        return "menu/index";
    }

    @RequestMapping(value ="add", method = RequestMethod.GET)
    public String displayAddMenu(Model model){
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenu(@ModelAttribute @Valid Menu menu, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add Menu");
            model.addAttribute(new Error());
            model.addAttribute("menu", menuDao.findAll());
            return "menu/add";
        }
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
    @RequestMapping(value ="view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId){
        Menu newMen = menuDao.findOne(menuId);

            model.addAttribute("title", "Menu: " + newMen.getName());
            model.addAttribute("menu", newMen);
            return "menu/view";

    }
    @RequestMapping(value ="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        AddMenuItemForm addMenuItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form", addMenuItemForm);
        model.addAttribute("title", "Add item to menu:" + menuDao.findOne(menuId).getName());
        return "menu/add-item";
    }
    @RequestMapping(value ="add-item/{menuId}", method = RequestMethod.POST)
    public String processAddItem(Model model, @PathVariable int menuId, @ModelAttribute AddMenuItemForm addMenuItemForm, Errors errors, @RequestParam int cheeseId){
        if (errors.hasErrors()){
            return "menu/add-item";
        }

        Menu newMen = menuDao.findOne(menuId);
        Cheese newCheese = cheeseDao.findOne(cheeseId);
        newMen.addItem(newCheese);
        menuDao.save(newMen);

        return "redirect:/menu/view/"+ menuId;
    }

}
