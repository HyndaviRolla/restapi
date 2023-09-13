package com.prodapt.practice.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 
import com.prodapt.practice.model.RegistrationForm;
import com.prodapt.practice.service.DomainUserService;
import com.prodapt.practice.entity.Cycle;
import com.prodapt.practice.repository.CycleRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cycles")
public class CycleRestController {

    @Autowired
    private CycleRepository cycleRepository;
    
    @Autowired
    private DomainUserService domainUserService;
    
   
    @GetMapping
    public ResponseEntity<List<Cycle>> listCycles() {
        List<Cycle> cycles = cycleRepository.findAll();
        return new ResponseEntity<>(cycles, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCycle(@RequestBody @Valid Cycle cycle, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
        }
        Cycle existingCycle = cycleRepository.findByName(cycle.getName());
        if (existingCycle != null) {
            return new ResponseEntity<>("Cycle with this name already exists", HttpStatus.CONFLICT);
        }
        cycleRepository.save(cycle);
        return new ResponseEntity<>("Cycle added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/restock/{id}")
    public ResponseEntity<String> restockCycle(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Cycle cycle = cycleRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException());             
            cycle.setStock(cycle.getStock() + quantity);
            cycleRepository.save(cycle);

            return new ResponseEntity<>("Cycle restocked successfully", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Cycle not found", HttpStatus.NOT_FOUND);
        }
    }
 
    
   
    @GetMapping("/return/{id}")
    public ResponseEntity<String> returnCycle(@PathVariable Long id) {
        try {
            Cycle cycle = cycleRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException());

            if (cycle.getVehiclesBorrowed() > 0) {
                cycle.setStock(cycle.getStock() + 1);
                cycle.setVehiclesReturned(cycle.getVehiclesReturned() + 1);
                cycle.setVehiclesBorrowed(cycle.getVehiclesBorrowed() - 1);

                cycleRepository.save(cycle);
                return new ResponseEntity<>("Cycle returned successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No cycles borrowed to return", HttpStatus.BAD_REQUEST);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Cycle not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/borrow/{id}")
    public ResponseEntity<String> borrowCycle(@PathVariable Long id) {
        try {
            Cycle cycle = cycleRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException());

            if (cycle.getStock() > 0) {
                cycle.setStock(cycle.getStock() - 1);
                cycle.setVehiclesBorrowed(cycle.getVehiclesBorrowed() + 1);

                cycleRepository.save(cycle);
                return new ResponseEntity<>("Cycle borrowed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No cycles in stock to borrow", HttpStatus.BAD_REQUEST);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Cycle not found", HttpStatus.NOT_FOUND);
        }
    }
  
   
}