package alararestaurant.domain.dtos.in;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ItemImportDto {
    @Expose
    private String name;

    @Expose
    private BigDecimal price;

    @Expose
    private String category;

    public ItemImportDto() {
    }

    @NotNull
    @Size(min = 3, max = 30)
    public String getName() {
        return this.name;
    }

    @NotNull
    @Positive
    @DecimalMin("0.01")
    public BigDecimal getPrice() {
        return this.price;
    }

    @NotNull
    @Size(min = 3, max = 30)
    public String getCategory() {
        return this.category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
