package org.example;

import java.util.*;

public class TExercicio002 {



        private String sku;
        private String name;
        private double price;
        private String category;

        public TExercicio002(String sku, String name, double price, String category) {
            this.sku = sku;
            this.name = name;
            this.price = price;
            this.category = category;
        }

        @Override
        public boolean equals(Object obj) {
            // Verificação de reflexividade
            if (this == obj) {
                return true;
            }

            // Verificação de null
            if (obj == null) {
                return false;
            }

            // Verificação de tipo
            if (getClass() != obj.getClass()) {
                return false;
            }

            // Cast seguro após verificação de tipo
            TExercicio002 outros = (TExercicio002) obj;

            // Comparação do campo que define a identidade (SKU)
            return Objects.equals(this.sku, outros.sku);
        }

        /**
         * Override do método hashCode() para manter consistência com equals()
         *
         * Regra fundamental: Se dois objetos são equals(),
         * eles DEVEM ter o mesmo hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hash(sku);
        }

        /**
         * Override do toString() para facilitar debug e logging
         */
        @Override
        public String toString() {
            return String.format("Product{sku='%s', name='%s', price=%.2f, category='%s'}",
                    sku, name, price, category);
        }

        // Getters
        public String getSku() { return sku; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }

        // Setters (exceto SKU que é imutável)
        public void setName(String name) { this.name = name; }
        public void setPrice(double price) { this.price = price; }
        public void setCategory(String category) { this.category = category; }
    }




