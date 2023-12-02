package com.example.demo.validators;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;

@Component
public class EnufPartsValidator implements ConstraintValidator<ValidEnufParts, Product> {

    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(ValidEnufParts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Product product, ConstraintValidatorContext constraintValidatorContext) {
        if (context == null) return true;

        ProductService repo = context.getBean(ProductServiceImpl.class);

        if (product.getId() != 0) {
            Product myProduct = repo.findById((int) product.getId());

            for (Part p : myProduct.getParts()) {
                // Check if adding/updating the product would cause any associated part to fall below the minimum inventory
                if (p.getInv() + product.getInv() - myProduct.getInv() < p.getMinInv()) {
                    addValidationError((BindingResult) constraintValidatorContext, "Adding/updating the product would cause the associated part '" + p.getName() + "' to fall below the minimum inventory.");
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    // Extend validation to handle both InhousePart and OutsourcedPart
    public boolean isValid(@Valid Part part, BindingResult bindingResult) {
        // Implement common validation for both InhousePart and OutsourcedPart
        if (part.getInv() < part.getMinInv()) {
            addValidationError(bindingResult, "Inventory cannot be below the minimum.");
            return false;
        }

        if (part.getInv() > part.getMaxInv()) {
            addValidationError(bindingResult, "Inventory cannot be above the maximum.");
            return false;
        }

        return !bindingResult.hasErrors();
    }

    // Helper method to add a validation error
    private void addValidationError(BindingResult bindingResult, String message) {
        bindingResult.reject("inventory.invalid", message);
    }
}