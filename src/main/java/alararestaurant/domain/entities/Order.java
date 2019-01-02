package alararestaurant.domain.entities;

import alararestaurant.domain.entities.enums.OrderType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
public class Order extends BaseEntity {
    private String customer;
    private LocalDateTime dateTime;
    private OrderType orderType;
    private Employee employee;
    private List<OrderItem> orderItems;

    public Order() {
    }

    @Column(name = "customer", nullable = false, columnDefinition = "text")
    public String getCustomer() {
        return this.customer;
    }

    @Column(name = "date_time", nullable = false)
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    @Column(name = "type", nullable = false, columnDefinition = "ENUM('ForHere', 'ToGo') default 'ForHere'")
    @Enumerated(value = EnumType.STRING)
    public OrderType getOrderType() {
        return this.orderType;
    }

    @ManyToOne(targetEntity = Employee.class)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    public Employee getEmployee() {
        return this.employee;
    }

    @OneToMany(targetEntity = OrderItem.class, mappedBy = "order")
    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
