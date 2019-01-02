package alararestaurant.repository;

import alararestaurant.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryByName(String name);

    @Query("" +
            "SELECT c FROM categories c " +
            "JOIN c.items i " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(i.id) DESC, SUM(i.price) DESC ")
    List<Category> findCategoriesByCountOfItems();
}
