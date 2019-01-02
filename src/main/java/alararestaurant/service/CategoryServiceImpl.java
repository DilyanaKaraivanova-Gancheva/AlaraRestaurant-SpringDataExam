package alararestaurant.service;

import alararestaurant.domain.entities.Category;
import alararestaurant.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String exportCategoriesByCountOfItems() {
        StringBuilder exportResult = new StringBuilder();
        List<Category> categories = this.categoryRepository.findCategoriesByCountOfItems();

        for (Category category : categories) {
            exportResult.append(String.format("Category: %s", category.getName()))
                    .append(System.lineSeparator());

            String itemsString = category.getItems().stream()
                    .map(item -> String.format("--- Item Name: %s\n--- Item Price: %.2f\n", item.getName(), item.getPrice()))
                    .collect(Collectors.joining(System.lineSeparator()));

            exportResult.append(itemsString)
                    .append(System.lineSeparator());
        }

        return exportResult.toString().trim();
    }
}

