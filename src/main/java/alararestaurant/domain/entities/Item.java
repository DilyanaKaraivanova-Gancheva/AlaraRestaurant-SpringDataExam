package alararestaurant.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "items")
public class Item extends BaseEntity {
    private String name;
    private Category category;
    private BigDecimal price;
    private List<OrderItem> orderItems;

    public Item() {
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return this.name;
    }

    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    public Category getCategory() {
        return this.category;
    }

    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return this.price;
    }

    @OneToMany(targetEntity = OrderItem.class, mappedBy = "item")
    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
