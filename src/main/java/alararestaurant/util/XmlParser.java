package alararestaurant.util;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface XmlParser {
    public <T> T deserialize(Class<T> clazz, String filePath) throws JAXBException, IOException;
}
