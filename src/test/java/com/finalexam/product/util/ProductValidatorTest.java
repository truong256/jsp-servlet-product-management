package com.finalexam.product.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductValidatorTest {
    @Test
    void acceptsValidData() {
        ProductValidator.ValidationResult result = ProductValidator.validate("  Bàn phím  ", "250000.50", "10");
        assertTrue(result.isValid());
        assertEquals("Bàn phím", result.getProduct().getName());
        assertEquals(new BigDecimal("250000.50"), result.getProduct().getPrice());
        assertEquals(10, result.getProduct().getQuantity());
    }

    @Test
    void rejectsEmptyName() {
        assertTrue(ProductValidator.validate(" ", "100", "1").getErrors().containsKey("name"));
    }

    @Test
    void rejectsNegativePrice() {
        assertTrue(ProductValidator.validate("A", "-1", "1").getErrors().containsKey("price"));
    }

    @Test
    void rejectsMoreThanTwoDecimalPlaces() {
        assertTrue(ProductValidator.validate("A", "1.235", "1").getErrors().containsKey("price"));
    }

    @Test
    void rejectsFractionalQuantity() {
        assertTrue(ProductValidator.validate("A", "10", "1.5").getErrors().containsKey("quantity"));
    }

    @Test
    void rejectsNegativeQuantity() {
        assertTrue(ProductValidator.validate("A", "10", "-1").getErrors().containsKey("quantity"));
    }

    @Test
    void rejectsInvalidNumbers() {
        ProductValidator.ValidationResult result = ProductValidator.validate("A", "khong phai so", "xx");
        assertFalse(result.isValid());
        assertTrue(result.getErrors().containsKey("price"));
        assertTrue(result.getErrors().containsKey("quantity"));
    }
}