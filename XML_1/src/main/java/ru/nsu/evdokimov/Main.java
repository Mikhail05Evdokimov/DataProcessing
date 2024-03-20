package ru.nsu.evdokimov;

import com.google.gson.Gson;
import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator;
import org.xml.sax.SAXException;
import ru.nsu.evdokimov.data.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.validation.SchemaFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, JAXBException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        Persons persons = new Persons();
        Parser saxParser = new Parser(persons);

        try(var is = Main.class.getClassLoader().getResourceAsStream("test.xml")) {
            parser.parse(is, saxParser);
        }
        PeopleRefactor peopleRefactor = new PeopleRefactor(persons);
        peopleRefactor.startRefactor();

        try(FileWriter writer = new FileWriter("myFile.json")) {
            Gson gson = new Gson();
            gson.toJson(persons, writer);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        //писать результат сериализации будем в Writer(StringWriter)
        StringWriter writer = new StringWriter();

        //создание объекта Marshaller, который выполняет сериализацию
        try {
            JAXBContext context = JAXBContext.newInstance(Persons.class, Person.class, FullName.class, Child.class, Family.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // сама сериализация
            marshaller.marshal(persons, writer);

            //XmlSchemaGenerator xmlSchemaGenerator = new XmlSchemaGenerator(persons.getClass());

            try (FileWriter fileWriter = new FileWriter("output.xml")) {
                fileWriter.write(writer.toString());
            } catch (IOException e) {
                System.out.println("Writing error");
            }

        } catch (JAXBException e) {
            throw new JAXBException(e);
        }


    }

}

