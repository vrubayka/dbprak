package parser;

import com.opencsv.bean.CsvToBeanBuilder;
import daos.GenericDao;
import daos.ProductDao;
import daos.ReviewDao;
import entities.ProductCategoryEntityPK;
import entities.ProductEntity;
import entities.ReviewEntity;
import entities.ReviewEntityPK;
import jakarta.persistence.PersistenceException;
import logging.ReadLog;
import logging.ReadingError;
import logging.exceptions.ShopReaderExceptions;
import org.hibernate.SessionFactory;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class CSVParser {

    final static String formatFilePath = "src/main/resources/data-files/format.txt";

    public void createReviewEntity(String file, SessionFactory sessionFactory) throws ShopReaderExceptions {
        ArrayList<String> reviewList = new ArrayList<>();
        Set<String> reviewSet = new LinkedHashSet<>();
        try {
            List<CSVBean> csvBeanList = new CsvToBeanBuilder(new FileReader(file))
                    .withType(CSVBean.class).build().parse();

            for (CSVBean csvBean : csvBeanList) {
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
                GenericDao<ReviewEntity> reviewEntityDao = new GenericDao<>(sessionFactory);
                if (isNewReview(re, sessionFactory)) {
                    try {
                        reviewEntityDao.create(re);
                        reviewList.add(csvBean.getProdId());

                    } catch (PersistenceException e) {
                        Throwable cause = e.getCause().getCause();
                        // check for missing referenced key exception
                        if (cause instanceof PSQLException && cause.getMessage().matches(
                                "ERROR: insert or update on table \"review\" violates foreign key constraint " +
                                "\"review_prod_id_fkey\"\n(.*)")) {

                            ReadLog.addError(new ReadingError("Review", re.getProdId(), "prodId",
                                                 "No such prod_id in product table. Review not inserted."));

                        } else {
                            e.printStackTrace();
                        }
                    }
                } else {
                    ReadLog.addDuplicate(new ReadingError("Review", re.getProdId(), "Duplicate",
                                                          "Review already in Database."));
                }
            }

            findAggregateReviews(reviewList, sessionFactory);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isNewReview(ReviewEntity review, SessionFactory sessionFactory) {
        ReviewDao reviewDao = new ReviewDao(sessionFactory);
        ReviewEntityPK reviewPK = new ReviewEntityPK();
        reviewPK.setProdId(review.getProdId());
        reviewPK.setUsername(review.getUsername());
        if (reviewDao.findOne(reviewPK) == null) {
            return true;
        }
        return false;
    }

    public void findAggregateReviews(ArrayList<String> reviewList, SessionFactory sessionFactory) {
        Collections.sort(reviewList);
        ArrayList<String> nonDuplicateList = removeDuplicateReviewList(reviewList);
        ReviewDao reviewDao = new ReviewDao(sessionFactory);
        ProductEntity productEntity = new ProductEntity();
        ProductDao productDao = new ProductDao(sessionFactory);
        for (String id : nonDuplicateList) {
            List<ReviewEntity> entitiesList = reviewDao.findByProdId(id);
            productEntity = productDao.findOne(id);
            double rating = 0.0;
            int aggr = 0;
            for (ReviewEntity entity : entitiesList) {
                rating = entity.getRating() + rating;
                aggr++;
            }
            //TODO: 2 decimals after comma?
            rating = Math.round(rating / aggr * 100.0) / 100.0;
            productEntity.setRating(rating);
            productDao.update(productEntity);
        }
    }

    public String formatSummary(CSVBean csvBean) {
        String text = csvBean.getReviewSum();
        Map<String, String> mapFromFile = HashMapFromTextFile();
        for (String key : mapFromFile.keySet()) {
            if (text.contains(key)) {
                text = text.replace(key, mapFromFile.get(key));
            }
        }
        return text;
    }

    public String formatReview(CSVBean csvBean) {
        String text = csvBean.getReview_text();
        Map<String, String> mapFromFile = HashMapFromTextFile();
        for (String key : mapFromFile.keySet()) {
            if (text.contains(key)) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Always close the BufferedReader
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
                ;
            }
        }

        return map;
    }

    public ArrayList removeDuplicateReviewList(ArrayList reviewList) {
        Set<String> set = new LinkedHashSet<>();
        set.addAll(reviewList);
        reviewList.clear();
        reviewList.addAll(set);
        return reviewList;
    }
}

