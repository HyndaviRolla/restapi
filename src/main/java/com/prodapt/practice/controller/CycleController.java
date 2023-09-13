 package com.prodapt.practice.controller;
 
import java.util.List; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.prodapt.practice.business.LoggedInUser;
import com.prodapt.practice.entity.Cycle;
import com.prodapt.practice.repository.CycleRepository;
import jakarta.validation.Valid;
@Controller
public class CycleController {
    @Autowired
    private CycleRepository cycleRepository;
    @Autowired
    private LoggedInUser loggedInUser;
    @GetMapping("/cycles")
    public String listCycles(Model model) {
 
        List<Cycle> cycles = cycleRepository.findAll();
        model.addAttribute("cycles", cycles);
        return "cycles";
    }
    @GetMapping("/cycles/add")
    public String showAddCycleForm(Model model) {
        model.addAttribute("cycle", new Cycle());
        return "redirect:/cycles";
    }
    @PostMapping("/cycles/add")
    public String addCycle(@ModelAttribute @Valid Cycle cycle, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {   
            return "redirect:/cycles";
        }   
        Cycle existingCycle = cycleRepository.findByName(cycle.getName());
        if (existingCycle != null) {   
            return "redirect:/cycles";
        }

        cycleRepository.save(cycle);
        
        return "redirect:/cycles";
    }
    @GetMapping("/cycles/restock/{id}")
    public String restockCycle(@PathVariable Long id, @RequestParam int quantity) throws NotFoundException {
    	  if (this.loggedInUser.getLoggedInUser() == null) {
              return "redirect:/loginpage";
          }
        Cycle cycle = cycleRepository.findById(id).orElseThrow(() -> new NotFoundException());
            cycle.setStock(cycle.getStock() +quantity);  
            cycleRepository.save(cycle);
        
        return "redirect:/cycles";
    }
   
    @GetMapping("/cycles/return/{id}")
    public String returnCycle(@PathVariable Long id) throws NotFoundException {
        Cycle cycle = cycleRepository.findById(id).orElseThrow(() -> new NotFoundException());
      
		if (cycle!=null && cycle.getVehiclesBorrowed()>0) {
            cycle.setStock(cycle.getStock() + 1);  
            cycle.setVehiclesReturned(cycle.getVehiclesReturned() + 1);
            cycle.setVehiclesBorrowed(cycle.getVehiclesBorrowed() - 1);
            cycleRepository.save(cycle);
        }
        return "redirect:/cycles";
    }

    @PostMapping("/cycles/borrow/{id}")
    public String borrowCycle(@PathVariable Long id) throws NotFoundException {
        Cycle cycle = cycleRepository.findById(id).orElseThrow(() -> new NotFoundException());
        if (cycle.getStock() > 0) {
            cycle.setStock(cycle.getStock() - 1);
            cycle.setVehiclesBorrowed(cycle.getVehiclesBorrowed() + 1);
            
            cycleRepository.save(cycle);
        }
        return "redirect:/cycles";
    }
}


    

