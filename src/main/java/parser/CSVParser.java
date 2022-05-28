package parser;

import com.opencsv.bean.CsvToBeanBuilder;
import daos.GenericDao;
import entities.ProductEntity;
import entities.ReviewEntity;
import org.hibernate.SessionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVParser {

        final static String formatFilePath = "src/main/resources/data-files/format.txt";
        public void createReviewEntity(String file, SessionFactory sessionFactory){

        try {
            List<CSVBean> csvBeanList = new CsvToBeanBuilder(new FileReader(file))
                    .withType(CSVBean.class).build().parse();

            for (CSVBean csvBean : csvBeanList) {
                try { //TODO:
                    String formatReviewSummary = formatSummary(csvBean);
                    String formatReviewText = formatReview(csvBean);
                    ReviewEntity re = new ReviewEntity();
                    re.setProdId(csvBean.getProdId());
                    re.setRating(csvBean.getRating());
                    re.setHelpfulRating(csvBean.getHelpful_rating());
                    re.setReviewdate(csvBean.getReviewdate());
                    re.setUsername(csvBean.getUsername());
                    re.setReviewSum(formatReviewSummary);
                    re.setReviewText(formatReviewText);
                    ProductEntity product = new ProductEntity();
                    product.setProdId(csvBean.getProdId());
                    product.setProdName("platzhalter");
                    product.setRating(2.342);
                    GenericDao<ProductEntity> productEntityDao = new GenericDao<>(sessionFactory);
                    GenericDao<ReviewEntity> reviewEntityDao = new GenericDao<>(sessionFactory);
                    productEntityDao.create(product);
                    reviewEntityDao.create(re);
                    System.out.println(re.getProdId() + " " + re.getUsername() + " "+ re.getReviewText());
                } catch(jakarta.persistence.PersistenceException e){
                    System.out.println("Duplicate review entry " + csvBean.getProdId() + " declined");
                }
            }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
              }
        }

        public String formatSummary(CSVBean csvBean){
            String text = csvBean.getReviewSum();
            Map<String, String> mapFromFile = HashMapFromTextFile();
            for (String key : mapFromFile.keySet()){
                if (text.contains(key)){
                    text = text.replace(key, mapFromFile.get(key));
                }
            }
            return text;
        }

    public String formatReview(CSVBean csvBean){
            String text = csvBean.getReview_text();
            Map<String, String> mapFromFile = HashMapFromTextFile();
            for (String key : mapFromFile.keySet()){
                if (text.contains(key)){
                    text = text.replace(key, mapFromFile.get(key));
                }
            }
            return text;
    }

        private Map<String, String> HashMapFromTextFile() {
            Map<String, String> map = new HashMap<String, String>();
            BufferedReader br = null;
            try {

                // create file object
                File file = new File(formatFilePath);

                // create BufferedReader object from the File
                br = new BufferedReader(new FileReader(file));

                String line = null;

                // read file line by line
                while ((line = br.readLine()) != null) {

                    // split the line by :
                    String[] parts = line.split(":");

                    // first part is name, second is number
                    String code = parts[0].trim();
                    String zeichen = parts[1].trim();

                    // put name, number in HashMap if they are
                    // not empty
                    if (!code.equals("") && !zeichen.equals(""))
                        map.put(code, zeichen);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {

                // Always close the BufferedReader
                if (br != null) {
                    try {
                        br.close();
                    }
                    catch (Exception e) {
                    };
                }
            }

            return map;
        }
}

