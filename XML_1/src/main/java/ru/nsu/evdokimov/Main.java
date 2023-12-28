package ru.nsu.evdokimov;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        Parser saxParser = new Parser();



        try(var is = Main.class.getClassLoader().getResourceAsStream("people.xml")) {
            parser.parse(is, saxParser);
        }


    }

}

