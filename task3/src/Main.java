import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Требуется передать файлы <values.json> <tests.json> <report.json>");
            System.exit(1);
        }

        String valuesFilePath = args[0];
        String testsFilePath = args[1];
        String reportFilePath = args[2];
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> values = readValuesFile(mapper, valuesFilePath);

            // Читаем tests.json
            JsonNode testsTemplate = mapper.readTree(new File(testsFilePath));

            // Заполняем отчет
            JsonNode reportJson = fillReport(testsTemplate, values);

            // Записываем результат в report.json
            writeReport(mapper, reportFilePath, reportJson);

            System.out.println("Report успешно создан.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> readValuesFile(ObjectMapper mapper, String valuesFilePath) throws IOException {
        Map<String, String> valuesMap = new HashMap<>();
        JsonNode valuesNode = mapper.readTree(new File(valuesFilePath)).get("values");
        for (JsonNode valueNode : valuesNode) {
            String id = valueNode.get("id").asText();
            String value = valueNode.get("value").asText();
            valuesMap.put(id, value);
        }
        return valuesMap;
    }

    private static JsonNode fillReport(JsonNode templateNode, Map<String, String> values) {
        JsonNode resultNode = templateNode.deepCopy();
        fillValues(resultNode, values);
        return resultNode;
    }

    private static void fillValues(JsonNode node, Map<String, String> values) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            if (objectNode.has("value")) {
                String id = objectNode.get("id").asText();
                String value = values.get(id);
                if (value != null) {
                    objectNode.put("value", value);
                }
            }
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode valueNode = field.getValue();
                if (valueNode.isArray()) {
                    for (JsonNode elementNode : valueNode) {
                        fillValues(elementNode, values);
                    }
                } else {
                    fillValues(valueNode, values);
                }
            }
        }
    }
    private static void writeReport(ObjectMapper mapper, String reportFilePath, JsonNode reportJson) throws IOException {
        File file = new File(reportFilePath);
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Не удалось создать файл отчета.");
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, reportJson);
    }
}
