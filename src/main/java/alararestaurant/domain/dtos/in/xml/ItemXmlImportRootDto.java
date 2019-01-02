package alararestaurant.domain.dtos.in.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemXmlImportRootDto {
    @XmlElement(name = "item")
    private ItemXmlImportDto[] itemXmlImportDtos;

    public ItemXmlImportRootDto() {
    }

    public ItemXmlImportDto[] getItemXmlImportDtos() {
        return this.itemXmlImportDtos;
    }

    public void setItemXmlImportDtos(ItemXmlImportDto[] itemXmlImportDtos) {
        this.itemXmlImportDtos = itemXmlImportDtos;
    }
}
