package alararestaurant.util;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileUtilImpl implements FileUtil {
    @Override
    public String readFile(String filePath) throws IOException {
        StringBuilder fileContent = new StringBuilder();

        BufferedReader bufferedFileReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(filePath))));

        String line = bufferedFileReader.readLine();

        while (line != null) {
            fileContent
                    .append(line)
                    .append(System.lineSeparator());
            line = bufferedFileReader.readLine();
        }

        return fileContent.toString().trim();
    }
}
