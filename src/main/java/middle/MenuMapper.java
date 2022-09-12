package middle;

import daos.*;
import entities.*;
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
import java.util.Arrays;
import java.util.List;

public class MenuMapper implements IMenuMapper {

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

        if (reload) {
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
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    public ProductEntity getProduct(String id) {
        ProductDao productDao = new ProductDao(sessionFactory);
        ProductEntity productEntity = productDao.findOne(id);
        retrieveInventory(productEntity);
        if (productEntity.getBookByProdId() != null) {
            AuthorDao authorDao = new AuthorDao(sessionFactory);
            productEntity.getBookByProdId().setAuthorsByBookId(authorDao.findAuthorByBookId(id));

        } else if (productEntity.getCdByProdId() != null) {
            CdArtistDao cdArtistDao = new CdArtistDao(sessionFactory);
            productEntity.getCdByProdId().setCdArtistsByCdId(cdArtistDao.findArtistForCd(id));

        } else if (productEntity.getDvdByProdId() != null) {
            DvdPersonDao dvdPersonDao = new DvdPersonDao(sessionFactory);
            productEntity.getDvdByProdId().setDvdPeopleByDvdId(dvdPersonDao.findPersonByDvdId(id));
        }
        return productEntity;
    }

    @Override
    public List<ProductEntity> getProducts(String pattern) {
        ProductDao productDao = new ProductDao(sessionFactory);
        List<ProductEntity> prodList = productDao.findByPattern(formatPattern(pattern));
        for (ProductEntity product : prodList){
            retrieveInventory(product);
        }
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
        CategoryNode root = new CategoryNode("root", null);
        CategoryDao categoryDao = new CategoryDao(sessionFactory);
        findChildCategories(categoryDao, root, null);
        return root;
    }

    private void findChildCategories(CategoryDao categoryDao, CategoryNode parentNode, Long parentId) {
        List<CategoryEntity> childCategoryEntities = categoryDao.findBySuperCategory(parentId);
        for (CategoryEntity categoryEntity : childCategoryEntities) {
            CategoryNode childNode = new CategoryNode(categoryEntity.getCategoryName(), categoryEntity.getCategoryId());
            findChildCategories(categoryDao, childNode, childNode.getId());
            parentNode.getChildCategories().add(childNode);
        }
    }

    @Override
    public List<ProductEntity> getProductsByCategoryPath(String path) {
        CategoryEntity currentCategory = new CategoryEntity();
        CategoryDao categoryDao = new CategoryDao(sessionFactory);
        ArrayList<String> pathList = createListFromPath(path);

        while (!(pathList.isEmpty() || currentCategory == null)) {
            currentCategory = categoryDao.findByNameAndSuperCategory(pathList.remove(0),
                                                                     currentCategory.getCategoryId());
        }

        if (currentCategory == null) {
            return null;
        } else {
            ProductCategoryDao productCategoryDao = new ProductCategoryDao(sessionFactory);
            List<ProductCategoryEntity> productCategoryEntities =
                    productCategoryDao.findByCategoryId(currentCategory.getCategoryId());

            List<ProductEntity> productEntityList = new ArrayList<>();
            for(ProductCategoryEntity productCategoryEntity: productCategoryEntities) {
                productEntityList.add(productCategoryEntity.getProductByProdId());
            }

            return productEntityList;
        }

        // ToDo: get products when path not complete
    }

    private ArrayList<String> createListFromPath(String path) {
        String[] pathArray = path.split("/");
        return new ArrayList<>(Arrays.asList(pathArray));
    }

    @Override
    public List<Object[]> getTopProducts(int k) {
        ReviewDao reviewDao = new ReviewDao(sessionFactory);
        return reviewDao.findTopProducts(k);
    }

    @Override
    public List<ProductEntity> getSimilarCheaperProduct() {
        return null;
    }

    @Override
    public void addNewReview() {

    }

    @Override
    public List<User> getTrolls(Double rating) {
        ReviewDao reviewDao = new ReviewDao(sessionFactory);
        List<Object[]> usernameAvgListe = reviewDao.findAggregateRatingOfUser(rating);
        List<User> userList = new ArrayList<>();
        for (Object[] object : usernameAvgListe){
            User user = new User((String) object[0]);
            userList.add(user);
        }
        return userList;
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
                                                                                         URISyntaxException,
                                                                                         ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<String> names = new ArrayList<String>();

        packageName = packageName.replace(".", "/");
        URL packageURL = classLoader.getResource(packageName);

        URI uri = new URI(packageURL.toString());
        File folder = new File(uri.getPath());
        File[] files = folder.listFiles();
        for (File file : files) {
            String name = file.getName();
            name = name.substring(0, name.lastIndexOf('.'));  // remove ".class"
            names.add(name);
        }

        return names;
    }

    public void retrieveInventory(ProductEntity productEntity){
        String id = productEntity.getProdId();
        InventoryDao inventoryDao = new InventoryDao(sessionFactory);
        productEntity.setInventoriesByProdId(inventoryDao.findInventoryForProduct(id));
    }
}
