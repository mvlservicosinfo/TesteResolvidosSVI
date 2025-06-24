package org.example;
import java.util.*;
import java.util.Scanner;
import static org.example.TExercicio001.AnagramGeneratorTest.runAllTests;

public class Main {
    public static void main(String[] args) {

        System.out.println("Digite o item 1 ou 2 para executar o Exercicios");
        Scanner item = new Scanner(System.in);
        Integer menuItem = item.nextInt();

        switch (menuItem){
            case  1: {
                runAllTests();
                break;
            }
            case 2 : {
                // Criando produtos com mesmo SKU mas propriedades diferentes
                TExercicio002 produto1 = new TExercicio002("SKU001", "Notebook Dell", 2500.00, "Electronics");
                TExercicio002 produto2 = new TExercicio002("SKU001", "Notebook Dell Updated", 2600.00, "Computers");
                TExercicio002 produto3 = new TExercicio002("SKU002", "Mouse Logitech", 50.00, "Electronics");

                System.out.println("=== Demonstração do equals() ===");
                System.out.println("product1.equals(product2): " + produto1.equals(produto2)); // true
                System.out.println("product1.equals(product3): " + produto1.equals(produto3)); // false
                System.out.println("product1 == product2: " + (produto1 == produto2)); // false

                System.out.println("\n=== Demonstração do hashCode() ===");
                System.out.println("product1.hashCode(): " + produto1.hashCode());
                System.out.println("product2.hashCode(): " + produto2.hashCode());
                System.out.println("product3.hashCode(): " + produto3.hashCode());

                System.out.println("\n=== Uso em Collections (HashSet) ===");
                Set<TExercicio002> produtoSet = new HashSet<>();
                produtoSet.add(produto1);
                produtoSet.add(produto2); // Não será adicionado pois é "igual" ao product1
                produtoSet.add(produto3);

                System.out.println("Tamanho do Set: " + produtoSet.size()); // 2, não 3
                System.out.println("Produtos no Set:");
                for (TExercicio002 p : produtoSet) {
                    System.out.println("  " + p);
                }

                System.out.println("\n=== Verificação de contenção ===");
                TExercicio002 searchProduct = new TExercicio002("SKU001", "Qualquer Nome", 0.0, "Qualquer Categoria");
                System.out.println("Set contém produto com SKU001: " + produtoSet.contains(searchProduct)); // true

                System.out.println("\n=== Demonstração com HashMap ===");
                Map<TExercicio002, Integer> inventory = new HashMap<>();
                inventory.put(produto1, 10);
                inventory.put(produto2, 15); // Substitui o valor anterior pois product1.equals(product2)
                inventory.put(produto3, 5);

                System.out.println("Tamanho do Map: " + inventory.size()); // 2
                System.out.println("Estoque do produto SKU001: " + inventory.get(searchProduct)); // 15
            }
            default:
                System.out.println("Valor Incorreto! Escolha um valor entre 1 e 2");

        }
    }

}