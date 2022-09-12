import entities.*;
import frontend.Menu;
import logging.ReadLog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.MutationQuery;
import parser.CSVParser;
import parser.CategoryReader;
import parser.XmlParser;
import queries.HibernateQueries;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException {
        Menu menu = new Menu();
        menu.printMenu();

    }
}
