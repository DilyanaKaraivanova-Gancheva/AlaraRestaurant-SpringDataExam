package alararestaurant.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "categories")
public class Category extends BaseEntity {
    private String name;
    private List<Item> items;

    public Category() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    @OneToMany(targetEntity = Item.class, mappedBy = "category")
    public List<Item> getItems() {
        return this.items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
