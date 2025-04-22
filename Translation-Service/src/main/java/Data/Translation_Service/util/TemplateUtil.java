package Data.Translation_Service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TemplateUtil {

    public static String renderTemplateText(String templateText, Map<String, String> templateData) throws IOException, TemplateException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> templateMap = new HashMap<>();
        if (templateData != null && !templateData.isEmpty()) {
            templateMap = objectMapper.convertValue(templateData, new TypeReference<>() {});
        }

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setTemplateLoader(new StringTemplateLoader());
        cfg.setDefaultEncoding("UTF-8");

        Template template = new Template("dbTemplate", new StringReader(templateText), cfg);

        StringWriter out = new StringWriter();
        template.process(templateMap, out);
        return out.toString();
    }
}
