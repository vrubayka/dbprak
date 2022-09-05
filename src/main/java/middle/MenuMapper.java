package middle;

import daos.ProductDao;
import entities.InventoryEntity;
import entities.ProductEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
    public void init() {

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
        return productDao;
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
