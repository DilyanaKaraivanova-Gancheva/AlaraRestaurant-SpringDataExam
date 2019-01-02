package alararestaurant.util;

import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.Validator;

@Component
public class ValidationUtilImpl implements ValidationUtil {
    private Validator validator;

    public ValidationUtilImpl() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <E> boolean isValid(E entity) {
        return this.validator.validate(entity).size() == 0;
    }
}
