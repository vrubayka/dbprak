package middle;

import daos.ProductDao;
import entities.InventoryEntity;
import entities.ProductEntity;
import logging.ReadLog;
import middle.wrapperClass.CategoryNode;
import middle.wrapperClass.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import parser.CSVParser;
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

public class MenuMapper implements IMenuMapper{

    private SessionFactory sessionFactory;

    @Override
    public void init(boolean reload) {

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

        if(reload) {
            HibernateQueries hibernateQueries = new HibernateQueries(sessionFactory);
            hibernateQueries.cleanDb();

            ReadLog log = new ReadLog();

            XmlParser xmlParser = new XmlParser();
            xmlParser.readFile("src/main/resources/data-files/leipzig_transformed.xml", sessionFactory);
            xmlParser.readFile("src/main/resources/data-files/dresden.xml", sessionFactory);
            xmlParser.readCategories("src/main/resources/data-files/categories.xml", sessionFactory);
            xmlParser.readSimilars("src/main/resources/data-files/leipzig_transformed.xml", sessionFactory);
            xmlParser.readSimilars("src/main/resources/data-files/dresden.xml", sessionFactory);
            CSVParser csvParser = new CSVParser();
            csvParser.createReviewEntity("src/main/resources/data-files/reviews.csv", sessionFactory);

            ReadLog.writeLogToCSV();
        }
    }

    @Override
    public void finish() {
        if(sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    public ProductEntity getProduct(String id) {
        ProductDao productDao = new ProductDao(sessionFactory);
        return productDao.findOne(id);
    }

    @Override
    public List<ProductEntity> getProducts(String pattern) {
        ProductDao productDao = new ProductDao(sessionFactory);
        List<ProductEntity> prodList = productDao.findByPattern(formatPattern(pattern));
        return prodList;
    }

    private String formatPattern(String pattern) {
        pattern = pattern.replace("\\", "\\\\");
        pattern = pattern.replace("_", "\\_");
        pattern = pattern.replace("%", "\\%");
        pattern = "%" + pattern + "%";
        return pattern;
    }

    @Override
    public CategoryNode getCategoryTree() {
        return null;
    }

    @Override
    public List<ProductEntity> getProductsByCategoryPath(String path) {
        return null;
    }

    @Override
    public List<ProductEntity> getTopProducts() {
        return null;
    }

    @Override
    public List<ProductEntity> getSimilarCheaperProduct() {
        return null;
    }

    @Override
    public void addNewReview() {

    }

    @Override
    public List<User> getTrolls() {
        return null;
    }

    @Override
    public List<InventoryEntity> getOffers() {
        return null;
    }

    private static List<Class<?>> getEntityClassesFromPackage(String packageName) throws ClassNotFoundException,
                                                                                        IOException,
                                                                                        URISyntaxException {
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

    private static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException,
                                                                                         URISyntaxException, ClassNotFoundException {
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
