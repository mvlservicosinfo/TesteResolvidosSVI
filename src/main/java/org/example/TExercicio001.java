package org.example;

import java.util.*;

public class TExercicio001 {
    /**
     * Gera todos os anagramas possíveis a partir de um conjunto de letras.
     *
     * @param letters String contendo as letras distintas para gerar anagramas
     * @return Lista ordenada contendo todos os anagramas possíveis
     * @throws IllegalArgumentException se a entrada for inválida
     */
    public static List<String> generateAnagrams(String letters) {
        // Validação da entrada
        validateInput(letters);

        // Converte para array de caracteres para facilitar manipulação
        char[] chars = letters.toLowerCase().toCharArray();

        // Remove duplicatas (caso existam) e ordena para consistência
        Set<Character> uniqueChars = new TreeSet<>();
        for (char c : chars) {
            uniqueChars.add(c);
        }

        // Converte de volta para array
        char[] uniqueArray = new char[uniqueChars.size()];
        int i = 0;
        for (char c : uniqueChars) {
            uniqueArray[i++] = c;
        }

        // Gera permutações usando algoritmo recursivo
        List<String> anagrams = new ArrayList<>();
        generatePermutations(uniqueArray, 0, anagrams);

        // Ordena o resultado para saída consistente
        Collections.sort(anagrams);

        return anagrams;
    }

    /**
     * @param chars      Array de caracteres a serem permutados
     * @param startIndex Índice atual da recursão
     * @param result     Lista para armazenar os resultados
     */
    private static void generatePermutations(char[] chars, int startIndex, List<String> result) {
        // Caso base: se chegou ao final, adiciona a permutação atual
        if (startIndex == chars.length) {
            result.add(new String(chars));
            return;
        }

        // Para cada posição restante, tenta colocar cada caractere disponível
        for (int i = startIndex; i < chars.length; i++) {
            // Troca o caractere atual com o da posição i
            swap(chars, startIndex, i);

            // Recursão para as próximas posições
            generatePermutations(chars, startIndex + 1, result);

            // Backtrack: desfaz a troca para tentar próxima combinação
            swap(chars, startIndex, i);
        }
    }

    /**
     * Troca dois elementos de posição no array.
     *
     * @param chars Array de caracteres
     * @param i     Primeira posição
     * @param j     Segunda posição
     */
    private static void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    /**
     * Valida a entrada fornecida pelo usuário.
     *
     * @param input String de entrada a ser validada
     * @throws IllegalArgumentException se a entrada for inválida
     */
    private static void validateInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Entrada não pode ser nula");
        }

        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException("Entrada não pode estar vazia");
        }

        // Verifica se contém apenas letras
        for (char c : input.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Entrada deve conter apenas letras. Caractere inválido: " + c);
            }
        }
    }

    /**
     * Método utilitário para exibir os anagramas de forma formatada.
     *
     * @param letters  Letras de entrada
     * @param anagrams Lista de anagramas gerados
     */
    public static void displayAnagrams(String letters, List<String> anagrams) {
        System.out.println("Anagramas para '" + letters + "':");
        System.out.println("Total de combinações: " + anagrams.size());
        System.out.println("Resultado: " + String.join(", ", anagrams));
        System.out.println();
    }


    /**
     * Classe de testes unitários para validar a funcionalidade do gerador de anagramas.
     */
    class AnagramGeneratorTest {

        /**
         * Testa o caso básico com 3 letras conforme especificado no enunciado.
         */
        public static void testBasicCase() {
            System.out.println("=== Teste Caso Básico ===");
            List<String> result = TExercicio001.generateAnagrams("abc");
            List<String> expected = Arrays.asList("abc", "acb", "bac", "bca", "cab", "cba");

            assert result.size() == 6 : "Deveria ter 6 anagramas";
            Collections.sort(expected);
            assert result.equals(expected) : "Anagramas não coincidem com o esperado";

            System.out.println("✓ Teste passou - Resultado: " + result);
            System.out.println();
        }

        /**
         * Testa caso extremo com uma única letra.
         */
        public static void testSingleLetter() {
            System.out.println("=== Teste Letra Única ===");
            List<String> result = TExercicio001.generateAnagrams("a");

            assert result.size() == 1 : "Deveria ter 1 anagrama";
            assert result.get(0).equals("a") : "Único anagrama deveria ser 'a'";

            System.out.println("✓ Teste passou - Resultado: " + result);
            System.out.println();
        }

        /**
         * Testa casos de entrada inválida.
         */
        public static void testInvalidInput() {
            System.out.println("=== Teste Entradas Inválidas ===");

            // Teste entrada vazia
            try {
                TExercicio001.generateAnagrams("");
                assert false : "Deveria ter lançado exceção para entrada vazia";
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Entrada vazia rejeitada corretamente: " + e.getMessage());
            }

            // Teste entrada nula
            try {
                TExercicio001.generateAnagrams(null);
                assert false : "Deveria ter lançado exceção para entrada nula";
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Entrada nula rejeitada corretamente: " + e.getMessage());
            }

            // Teste entrada com números
            try {
                TExercicio001.generateAnagrams("ab3");
                assert false : "Deveria ter lançado exceção para entrada com números";
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Entrada com números rejeitada corretamente: " + e.getMessage());
            }

            System.out.println();
        }

        /**
         * Testa caso com letras duplicadas (devem ser removidas).
         */
        public static void testDuplicateLetters() {
            System.out.println("=== Teste Letras Duplicadas ===");
            List<String> result = TExercicio001.generateAnagrams("aab");

            // Deve tratar como "ab" (remove duplicata)
            assert result.size() == 2 : "Deveria ter 2 anagramas após remover duplicatas";
            List<String> expected = Arrays.asList("ab", "ba");
            Collections.sort(expected);
            assert result.equals(expected) : "Resultado deveria ser equivalente a 'ab'";

            System.out.println("✓ Teste passou - Duplicatas removidas corretamente: " + result);
            System.out.println();
        }

        /**
         * Testa a performance com um conjunto maior de letras.
         */
        public static void testPerformance() {
            System.out.println("=== Teste Performance ===");
            long startTime = System.currentTimeMillis();

            List<String> result = TExercicio001.generateAnagrams("abcdef");

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            assert result.size() == 720 : "6! = 720 permutações esperadas";
            System.out.println("✓ Teste passou - " + result.size() + " anagramas gerados em " + duration + "ms");
            System.out.println();
        }

        /**
         * Executa todos os testes unitários.
         */
        public static void runAllTests() {
            System.out.println("========================================");
            System.out.println("EXECUTANDO TESTES UNITÁRIOS");
            System.out.println("========================================");

            try {
                testBasicCase();
                testSingleLetter();
                testInvalidInput();
                testDuplicateLetters();
                testPerformance();

                System.out.println("========================================");
                System.out.println("✓ TODOS OS TESTES PASSARAM COM SUCESSO!");
                System.out.println("========================================");

            } catch (AssertionError e) {
                System.err.println("✗ Teste falhou: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("✗ Erro inesperado: " + e.getMessage());
            }
        }
    }
}

