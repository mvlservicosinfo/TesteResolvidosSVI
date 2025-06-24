package org.example.Exercicio008;

import org.junit.Test;

public class TExercio008 {


    @Test
    public void testValidEmailFormat() {
        // Arrange
        UserService userService = new UserService();
        User user = new User("João Silva", "joao@email.com", "Rua A, 123", "11999999999");

        // Act
        boolean isValid = userService.isValidEmail(user.getEmail());

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testInvalidEmailFormat() {
        // Arrange
        UserService userService = new UserService();

        // Act & Assert
        assertFalse(userService.isValidEmail("email-invalido"));
        assertFalse(userService.isValidEmail("@email.com"));
        assertFalse(userService.isValidEmail("email@"));
    }
    @Test
    @Transactional
    public void testEmailUniquenessValidation() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        User existingUser = new User("Maria", "maria@email.com", "Rua B", "11888888888");
        when(userRepository.findByEmail("maria@email.com")).thenReturn(existingUser);

        User newUser = new User("João", "maria@email.com", "Rua C", "11777777777");

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> {
            userService.createUser(newUser);
        });
    }
    @Test
    public void testOnlyAdminCanDeleteUser() {
        // Arrange
        User adminUser = new User("Admin", "admin@email.com", "Rua Admin", "11111111111");
        adminUser.setRole(UserRole.ADMIN);

        User regularUser = new User("User", "user@email.com", "Rua User", "11222222222");
        regularUser.setRole(UserRole.USER);

        User targetUser = new User("Target", "target@email.com", "Rua Target", "11333333333");

        UserService userService = new UserService();

        // Act & Assert - Admin pode deletar
        assertDoesNotThrow(() -> {
            userService.deleteUser(targetUser.getId(), adminUser);
        });

        // Act & Assert - Usuário comum não pode deletar
        assertThrows(UnauthorizedException.class, () -> {
            userService.deleteUser(targetUser.getId(), regularUser);
        });
    }

    @Test
    public void testCompleteUserCreationFlow() {
        // Arrange
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/users");

        // Act
        driver.findElement(By.id("btn-new-user")).click();
        driver.findElement(By.id("name")).sendKeys("João Silva");
        driver.findElement(By.id("email")).sendKeys("joao@email.com");
        driver.findElement(By.id("address")).sendKeys("Rua A, 123");
        driver.findElement(By.id("phone")).sendKeys("11999999999");
        driver.findElement(By.id("btn-save")).click();

        // Assert
        WebElement successMessage = driver.findElement(By.className("success-message"));
        assertEquals("Usuário criado com sucesso!", successMessage.getText());

        // Cleanup
        driver.quit();
    }
}
