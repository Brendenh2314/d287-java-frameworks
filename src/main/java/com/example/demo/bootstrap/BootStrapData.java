package com.example.demo.bootstrap;

import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.repositories.OutsourcedPartRepository;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.OutsourcedPartService;
import com.example.demo.service.OutsourcedPartServiceImpl;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BootStrapData implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;
    private final OutsourcedPartRepository outsourcedPartRepository;

    public BootStrapData(
            PartRepository partRepository,
            ProductRepository productRepository,
            OutsourcedPartRepository outsourcedPartRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
        this.outsourcedPartRepository = outsourcedPartRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        long partCount = partRepository.count();
        long productCount = productRepository.count();

        if (partCount == 0 && productCount == 0) {
            // Add sample inventory for a skate shop
            addSkateShopInventory();
        } else {
            // Print a message indicating that sample inventory is already loaded
            System.out.println("Sample inventory is full.");
        }

        // Additional logic or logging can be added here if needed
        System.out.println("Bootstrap complete.");
    }

    private void addSkateShopInventory() {
        // Code to add sample inventory for a skate shop
        OutsourcedPart skateboardDeck = createOutsourcedPart("Skateboard Deck", 50, 49.99, 10, 100, "Skate Supply Co.");
        OutsourcedPart trucks = createOutsourcedPart("Trucks", 30, 29.99, 5, 50, "Skate Supply Co.");
        OutsourcedPart wheels = createOutsourcedPart("Wheels", 40, 19.99, 8, 80, "Skate Supply Co.");
        OutsourcedPart bearings = createOutsourcedPart("Bearings", 25, 9.99, 5, 40, "Skate Supply Co.");
        OutsourcedPart gripTape = createOutsourcedPart("Grip Tape", 20, 7.99, 4, 30, "Skate Supply Co.");

        outsourcedPartRepository.save(skateboardDeck);
        outsourcedPartRepository.save(trucks);
        outsourcedPartRepository.save(wheels);
        outsourcedPartRepository.save(bearings);
        outsourcedPartRepository.save(gripTape);

        Product completeSkateboard = createProduct("Complete Skateboard", 99.99, 15);
        Product skateboardDeckOnly = createProduct("Skateboard Deck Only", 49.99, 20);
        Product replacementWheels = createProduct("Replacement Wheels", 19.99, 25);
        Product bearingSet = createProduct("Bearing Set", 9.99, 30);
        Product gripTapePack = createProduct("Grip Tape Pack", 7.99, 35);

        productRepository.save(completeSkateboard);
        productRepository.save(skateboardDeckOnly);
        productRepository.save(replacementWheels);
        productRepository.save(bearingSet);
        productRepository.save(gripTapePack);

        // Print statements moved within the method
        System.out.println("Started in Bootstrap");
        System.out.println("Number of Products"+productRepository.count());
        System.out.println(productRepository.findAll());
        System.out.println("Number of Parts"+partRepository.count());
        System.out.println(partRepository.findAll());
    }

    private OutsourcedPart createOutsourcedPart(String name, int inv, double price, int min, int max, String companyName) {
        OutsourcedPart part = new OutsourcedPart();
        part.setName(name);
        part.setInv(inv);
        part.setPrice(price);
        part.setMinimum(min);
        part.setMaximum(max);
        part.setCompanyName(companyName);
        return part;
    }

    private Product createProduct(String name, double price, int inv) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setInv(inv);
        return product;
    }
}