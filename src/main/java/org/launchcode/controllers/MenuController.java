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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
            model.addAttribute(new Menu());
            return "menu/add";
        }
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
    @RequestMapping(value ="view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId){
        Menu newMen = menuDao.findOne(menuId);
        if (newMen != null) {
            model.addAttribute("title", "Menu: " + newMen.getName());
            model.addAttribute("menu", newMen.getCheeses());
            return "menu/view";
        }
        return "redirect:";
    }
    @RequestMapping(value ="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        AddMenuItemForm addMenuItemForm = new AddMenuItemForm(menu, cheeses);
        model.addAttribute("form", addMenuItemForm);
        model.addAttribute("title", "Add item to menu:" + menuDao.findOne(menuId).getName());
        return "menu/add-item";
    }

}