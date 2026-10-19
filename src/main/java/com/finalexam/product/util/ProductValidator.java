package com.finalexam.product.util;

import com.finalexam.product.model.Product;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** Kiem tra dau vao truoc khi luu xuong MySQL. */
public final class ProductValidator {
    private static final BigDecimal MAX_PRICE = new BigDecimal("9999999999.99");
    private static final int MAX_QUANTITY = 1_000_000_000;

    private ProductValidator() {
    }

    public static ValidationResult validate(String rawName, String rawPrice, String rawQuantity) {
        Map<String, String> errors = new LinkedHashMap<>();
        String name = rawName == null ? "" : rawName.trim();
        if (name.isEmpty()) {
            errors.put("name", "Vui lòng nhập tên sản phẩm.");
        } else if (name.length() > 150) {
            errors.put("name", "Tên sản phẩm không được vượt quá 150 ký tự.");
        }

        BigDecimal price = null;
        try {
            price = new BigDecimal(rawPrice == null ? "" : rawPrice.trim());
            if (price.scale() > 2 || price.compareTo(BigDecimal.ZERO) < 0
                    || price.compareTo(MAX_PRICE) > 0) {
                errors.put("price", "Giá phải từ 0 đến 9.999.999.999,99 và có tối đa 2 chữ số thập phân.");
            }
        } catch (NumberFormatException ex) {
            errors.put("price", "Giá phải là một số hợp lệ.");
        }

        Integer quantity = null;
        try {
            quantity = Integer.valueOf(rawQuantity == null ? "" : rawQuantity.trim());
            if (quantity < 0 || quantity > MAX_QUANTITY) {
                errors.put("quantity", "Số lượng phải từ 0 đến 1.000.000.000.");
            }
        } catch (NumberFormatException ex) {
            errors.put("quantity", "Số lượng phải là số nguyên hợp lệ.");
        }

        if (!errors.isEmpty()) {
            return new ValidationResult(null, errors);
        }
        return new ValidationResult(new Product(0L, name, price, quantity), errors);
    }

    public static final class ValidationResult {
        private final Product product;
        private final Map<String, String> errors;

        private ValidationResult(Product product, Map<String, String> errors) {
            this.product = product;
            this.errors = errors;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public Product getProduct() {
            return product;
        }

        public Map<String, String> getErrors() {
            return errors;
        }
    }
}