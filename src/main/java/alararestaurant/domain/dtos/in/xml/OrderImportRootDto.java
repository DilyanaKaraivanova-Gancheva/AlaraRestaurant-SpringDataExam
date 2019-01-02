package alararestaurant.domain.dtos.in.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderImportRootDto {
    @XmlElement(name = "order")
    private OrderImportDto[] orderImportDtos;

    public OrderImportRootDto() {
    }

    public OrderImportDto[] getOrderImportDtos() {
        return this.orderImportDtos;
    }

    public void setOrderImportDtos(OrderImportDto[] orderImportDtos) {
        this.orderImportDtos = orderImportDtos;
    }
}
