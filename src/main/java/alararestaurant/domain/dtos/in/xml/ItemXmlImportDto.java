package alararestaurant.domain.dtos.in.xml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemXmlImportDto {
    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "quantity")
    private Integer quantity;

    public ItemXmlImportDto() {
    }

    @NotNull
    @Size(min = 3, max = 30)
    public String getName() {
        return this.name;
    }

    @NotNull
    @Positive
    public Integer getQuantity() {
        return this.quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
