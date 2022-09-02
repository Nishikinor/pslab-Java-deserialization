
import data.productcatalog.ProductTemplate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import org.apache.commons.cli.*;

class Main {
    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option i = new Option("i", "id", true, "serialized object");
        i.setRequired(false);
        options.addOption(i);

        Option d = new Option("d", "deserial", true, "deserialize object");
        d.setRequired(false);
        options.addOption(d);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        if (cmd.hasOption("i")) {
            String id = cmd.getOptionValue("i");
            ProductTemplate originalObject = new ProductTemplate(id);
            String serializedObject = serialize(originalObject);
            System.out.println("Serialized object: " + serializedObject);
        }

        if (cmd.hasOption("d")) {
            String serializedObject = cmd.getOptionValue("d");
            ProductTemplate deserializedObject = deserialize(serializedObject);
            System.out.println("Deserialized object ID: " + deserializedObject.getId());
        }
    }

    private static String serialize(Serializable obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeObject(obj);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static <T> T deserialize(String base64SerializedObj) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(base64SerializedObj)))) {
            @SuppressWarnings("unchecked")
            T obj = (T) in.readObject();
            return obj;
        }
    }
}