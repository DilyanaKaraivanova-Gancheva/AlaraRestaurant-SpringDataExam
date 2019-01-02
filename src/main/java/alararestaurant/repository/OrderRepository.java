package alararestaurant.repository;

import alararestaurant.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("" +
            "SELECT o FROM orders o " +
            "JOIN o.employee e " +
            "JOIN e.position p " +
            "WHERE p.name = 'Burger Flipper' " +
            "ORDER BY e.name, o.id")
    List<Order> findOrdersFinishedByTheBurgerFlippers();
}


//    SELECT e.name, o.customer
//        FROM orders AS o
//        JOIN employees AS e
//        ON e.id = o.employee_id
//        JOIN positions AS p
//        ON p.id = e.position_id
//        WHERE p.name = 'Burger Flipper'
//        ORDER BY e.name, o.id;