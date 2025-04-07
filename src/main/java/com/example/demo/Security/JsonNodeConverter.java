package com.example.demo.Security;

//
//import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Converter
//public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Override
//    public String convertToDatabaseColumn(JsonNode attribute) {
//        try {
//            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
//
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Error converting", e);
//        }
//    }
//
//    @Override
//    public JsonNode convertToEntityAttribute(String dbData) {
//        try {
//            return dbData == null ? null : objectMapper.readTree(dbData);
//
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Error converting String", e);
//        }
//    }
//}
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        try {
            if (attribute == null) return null;

            ObjectNode copy = attribute.deepCopy();

            if (copy.has("ftpPassword") && !copy.get("ftpPassword").isNull()) {
                String rawPassword = copy.get("ftpPassword").asText();
                String encryptedPassword = getEncryptor().encrypt(rawPassword);
                copy.put("ftpPassword", encryptedPassword);
            }

            return objectMapper.writeValueAsString(copy);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JsonNode to String", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) return null;

            JsonNode node = objectMapper.readTree(dbData);

            if (node.has("ftpPassword") && !node.get("ftpPassword").isNull()) {
                String encryptedPassword = node.get("ftpPassword").asText();
                String decryptedPassword = getEncryptor().decrypt(encryptedPassword);
                ((ObjectNode) node).put("ftpPassword", decryptedPassword);
            }

            return node;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting String to JsonNode", e);
        }
    }

    private AesEncryptor getEncryptor() {
        return SpringContext.getBean(AesEncryptor.class);
    }
}
