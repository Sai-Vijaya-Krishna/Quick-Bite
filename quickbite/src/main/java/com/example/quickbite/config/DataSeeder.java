package com.example.quickbite.config;

import com.example.quickbite.entity.MenuItem;
import com.example.quickbite.entity.User;
import com.example.quickbite.repository.MenuItemRepository;
import com.example.quickbite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Seed admin user
        if (!userRepository.existsByEmail("admin@quickbite.com")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@quickbite.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Admin created: admin@quickbite.com / admin123");
        }

        // Seed menu items
        if (menuItemRepository.count() == 0) {
            String[][] items = {
                {"Classic Burger", "Juicy beef patty with lettuce, tomato, and cheese", "149.0", "BURGER"},
                {"Cheese Burger", "Double cheese patty with special sauce", "179.0", "BURGER"},
                {"Veggie Burger", "Crispy veggie patty with fresh veggies", "129.0", "BURGER"},
                {"Margherita Pizza", "Classic tomato and mozzarella pizza", "249.0", "PIZZA"},
                {"Pepperoni Pizza", "Loaded with pepperoni and cheese", "299.0", "PIZZA"},
                {"Paneer Pizza", "Indian style paneer tikka pizza", "279.0", "PIZZA"},
                {"French Fries", "Crispy golden fries with dipping sauce", "89.0", "SIDES"},
                {"Onion Rings", "Crunchy golden onion rings", "99.0", "SIDES"},
                {"Coleslaw", "Creamy homemade coleslaw", "59.0", "SIDES"},
                {"Coke", "Chilled Coca-Cola 500ml", "49.0", "DRINKS"},
                {"Mango Shake", "Fresh mango milkshake", "79.0", "DRINKS"},
                {"Water", "Mineral water 1L", "29.0", "DRINKS"},
                {"Chicken Wrap", "Grilled chicken in a tortilla wrap", "169.0", "WRAPS"},
                {"Paneer Wrap", "Spiced paneer in a soft tortilla", "149.0", "WRAPS"}
            };
            for (String[] i : items) {
                MenuItem item = new MenuItem();
                item.setName(i[0]);
                item.setDescription(i[1]);
                item.setPrice(Double.parseDouble(i[2]));
                item.setCategory(i[3]);
                item.setAvailable(true);
                menuItemRepository.save(item);
            }
            System.out.println("Menu items seeded!");
        }
    }
}
