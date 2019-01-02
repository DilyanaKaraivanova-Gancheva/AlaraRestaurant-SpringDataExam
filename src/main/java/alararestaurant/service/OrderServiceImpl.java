package alararestaurant.service;

import alararestaurant.domain.dtos.in.xml.ItemXmlImportDto;
import alararestaurant.domain.dtos.in.xml.OrderImportDto;
import alararestaurant.domain.dtos.in.xml.OrderImportRootDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Item;
import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import alararestaurant.domain.entities.enums.OrderType;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import alararestaurant.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String ORDERS_JSON_FILE_PATH = "src\\main\\resources\\files\\orders.xml";

    private OrderRepository orderRepository;
    private FileUtil fileUtil;
    private XmlParser xmlParser;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private EmployeeRepository employeeRepository;
    private ItemRepository itemRepository;
    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, FileUtil fileUtil, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, EmployeeRepository employeeRepository, ItemRepository itemRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Boolean ordersAreImported() {
        return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {
        return this.fileUtil.readFile(ORDERS_JSON_FILE_PATH);
    }

    @Override
    public String importOrders() throws JAXBException, IOException {
        StringBuilder importResult = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        OrderImportRootDto orderImportRootDtos = this.xmlParser.deserialize(OrderImportRootDto.class, ORDERS_JSON_FILE_PATH);

        for (OrderImportDto orderImportDto : orderImportRootDtos.getOrderImportDtos()) {
            if (!this.validationUtil.isValid(orderImportDto)) {
                importResult.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }

            Employee employeeEntity = this.employeeRepository.findEmployeeByName(orderImportDto.getEmployee()).orElse(null);
            if (employeeEntity == null) {
                importResult.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }

            Order orderEntity = new Order();
            orderEntity.setCustomer(orderImportDto.getCustomer());
            orderEntity.setDateTime(LocalDateTime.parse(orderImportDto.getDateTime(), dtf));
            orderEntity.setOrderType(OrderType.valueOf(orderImportDto.getType()));
            orderEntity.setEmployee(employeeEntity);
            //orderEntity = this.orderRepository.saveAndFlush(orderEntity);

            List<Item> items = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            boolean isAllItemsValid = true;

            for (ItemXmlImportDto itemXmlImportDto : orderImportDto.getItemXmlImportRootDto().getItemXmlImportDtos()) {
                if (!this.validationUtil.isValid(itemXmlImportDto)) {
                    importResult.append("Invalid data format.").append(System.lineSeparator());
                    isAllItemsValid = false;
                    break;
                }

                Item itemEntity = this.itemRepository.findItemByName(itemXmlImportDto.getName()).orElse(null);
                if (itemEntity == null) {
                    importResult.append("Invalid data format.").append(System.lineSeparator());
                    isAllItemsValid = false;
                    break;
                }

                items.add(itemEntity);
                quantities.add(itemXmlImportDto.getQuantity());
            }

            if (!isAllItemsValid) {
                continue;
            }

            orderEntity = this.orderRepository.saveAndFlush(orderEntity);

            for (int i = 0; i < items.size(); i++) {
                OrderItem orderItemEntity = new OrderItem();
                orderItemEntity.setOrder(orderEntity);
                orderItemEntity.setItem(items.get(i));
                orderItemEntity.setQuantity(quantities.get(i));
                this.orderItemRepository.saveAndFlush(orderItemEntity);
            }

            importResult.append(String.format("Order for %s on %s added",
                    orderImportDto.getCustomer(), orderImportDto.getDateTime())).append(System.lineSeparator());
        }

        return importResult.toString().trim();
    }

    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {
        StringBuilder exportResult = new StringBuilder();
        List<Order> orders = this.orderRepository.findOrdersFinishedByTheBurgerFlippers();

        for (Order order : orders) {
            exportResult.append(String.format("Name: %s", order.getEmployee().getName()))
                    .append(System.lineSeparator())
                    .append("Orders: ")
                    .append(System.lineSeparator())
                    .append(String.format("\tCustomer: %s", order.getCustomer()))
                    .append(System.lineSeparator())
                    .append("\tItems:")
                    .append(System.lineSeparator());

            String orderItemsString = order.getOrderItems().stream()
                    .map(item -> String.format("\t\tName: %s\n\t\tPrice: %.2f\n\t\tQuantity: %d\n",
                            item.getItem().getName(), item.getItem().getPrice(), item.getQuantity()))
                    .collect(Collectors.joining(System.lineSeparator()));

            exportResult.append(orderItemsString)
                    .append(System.lineSeparator());
        }

        return exportResult.toString().trim();
    }
}

