package alararestaurant.service;

import alararestaurant.domain.dtos.in.ItemImportDto;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemServiceImpl implements ItemService {
    private static final String ITEMS_JSON_FILE_PATH = "src\\main\\resources\\files\\items.json";

    private ItemRepository itemRepository;
    private FileUtil fileUtil;
    private Gson gson;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, FileUtil fileUtil, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Boolean itemsAreImported() {
        return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {
        return this.fileUtil.readFile(ITEMS_JSON_FILE_PATH);
    }

    @Override
    public String importItems(String items) {
        StringBuilder importResult = new StringBuilder();
        ItemImportDto[] itemImportDtos = this.gson.fromJson(items, ItemImportDto[].class);

        for (ItemImportDto itemImportDto : itemImportDtos) {
            if (!this.validationUtil.isValid(itemImportDto)) {
                importResult.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }

            Item itemEntity = this.itemRepository.findItemByName(itemImportDto.getName()).orElse(null);
            if (itemEntity != null) {
                importResult.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }

            Category categoryEntity = this.categoryRepository.findCategoryByName(itemImportDto.getCategory()).orElse(null);
            if (categoryEntity == null) {
                categoryEntity = new Category();
                categoryEntity.setName(itemImportDto.getCategory());
                this.categoryRepository.saveAndFlush(categoryEntity);
            }

            itemEntity = this.modelMapper.map(itemImportDto, Item.class);
            itemEntity.setCategory(categoryEntity);

            this.itemRepository.saveAndFlush(itemEntity);
            importResult.append(String.format("Record %s successfully imported.", itemEntity.getName()))
                    .append(System.lineSeparator());
        }

        return importResult.toString().trim();
    }
}
