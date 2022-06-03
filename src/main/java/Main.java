import entities.*;
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

        SessionFactory sessionFactory;

        try {
            Configuration configuration = new Configuration().configure();
            for (Class cls : getEntityClassesFromPackage("entities")) {
                configuration.addAnnotatedClass(cls);
            }
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        HibernateQueries hibernateQueries = new HibernateQueries(sessionFactory);
        hibernateQueries.cleanDb();

        ReadLog log = new ReadLog();

        XmlParser xmlParser = new XmlParser();
        //xmlParser.readFile("src/main/resources/data-files/leipzig_transformed.xml", sessionFactory);
        //xmlParser.readFile("src/main/resources/data-files/dresden.xml", sessionFactory);
        //xmlParser.readCategories("src/main/resources/data-files/categories.xml", sessionFactory);
        CSVParser csvParser = new CSVParser();
        csvParser.createReviewEntity("src/main/resources/data-files/reviews.csv", sessionFactory);
        xmlParser.readSimilars("src/main/resources/data-files/leipzig_transformed.xml", sessionFactory);
        xmlParser.readSimilars("src/main/resources/data-files/dresden.xml", sessionFactory);

        ReadLog.writeLogToCSV();

    }

    public static List<Class<?>> getEntityClassesFromPackage(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        List<String> classNames = getClassNamesFromPackage(packageName);
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (String className : classNames) {
            Class<?> cls = Class.forName(packageName + "." + className);
            Annotation[] annotations = cls.getAnnotations();

            for (Annotation annotation : annotations) {
                System.out.println(cls.getCanonicalName() + ": " + annotation.toString());
                if (annotation instanceof jakarta.persistence.Entity) {
                    classes.add(cls);
                }
            }
        }

        return classes;
    }

    public static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException, URISyntaxException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<String> names = new ArrayList<String>();

        packageName = packageName.replace(".", "/");
        URL packageURL = classLoader.getResource(packageName);

        URI uri = new URI(packageURL.toString());
        File folder = new File(uri.getPath());
        File[] files = folder.listFiles();
        for (File file: files) {
            String name = file.getName();
            name = name.substring(0, name.lastIndexOf('.'));  // remove ".class"
            names.add(name);
        }

        return names;
    }
}
