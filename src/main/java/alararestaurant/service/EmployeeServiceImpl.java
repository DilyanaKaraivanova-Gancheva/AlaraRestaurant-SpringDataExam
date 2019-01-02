package alararestaurant.service;

import alararestaurant.domain.dtos.in.EmployeeImportDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEES_JSON_FILE_PATH = "src\\main\\resources\\files\\employees.json";

    private EmployeeRepository employeeRepository;
    private FileUtil fileUtil;
    private Gson gson;
    private ValidationUtil validationUtil;
    private PositionRepository positionRepository;
    private ModelMapper modelMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, FileUtil fileUtil, Gson gson, ValidationUtil validationUtil, PositionRepository positionRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.positionRepository = positionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEES_JSON_FILE_PATH);
    }

    @Override
    public String importEmployees(String employees) {
        StringBuilder importResult = new StringBuilder();
        EmployeeImportDto[] employeeImportDtos = this.gson.fromJson(employees, EmployeeImportDto[].class);

        for (EmployeeImportDto employeeImportDto : employeeImportDtos) {
            if (!validationUtil.isValid(employeeImportDto)) {
                importResult.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }

            Position positionEntity = this.positionRepository.findPositionByName(employeeImportDto.getPosition()).orElse(null);
            if (positionEntity == null) {
                positionEntity = new Position();
                positionEntity.setName(employeeImportDto.getPosition());
                positionEntity = this.positionRepository.saveAndFlush(positionEntity);
            }

            Employee employeeEntity = this.modelMapper.map(employeeImportDto, Employee.class);
            employeeEntity.setPosition(positionEntity);

            this.employeeRepository.saveAndFlush(employeeEntity);
            importResult.append(String.format("Record %s successfully imported.", employeeEntity.getName()))
                    .append(System.lineSeparator());
        }

        return importResult.toString().trim();
    }
}
