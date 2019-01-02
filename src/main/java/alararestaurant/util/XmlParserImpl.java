package alararestaurant.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

@Component
public class XmlParserImpl implements XmlParser {
    private JAXBContext jaxbContext;

    public XmlParserImpl() {
    }

    @Override
    public <T> T deserialize(Class<T> clazz, String filePath) throws JAXBException, IOException {
        this.jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
        T object = null;

        object = (T) unmarshaller.unmarshal(new File(filePath));

        return object;
    }
}
