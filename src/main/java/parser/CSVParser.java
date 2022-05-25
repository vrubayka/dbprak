package parser;

import com.opencsv.bean.CsvToBeanBuilder;
import daos.GenericDao;
import entities.ReviewEntity;
import org.hibernate.SessionFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class CSVParser {
        public void createReviewEntity(String file, SessionFactory sessionFactory){

        try {
            List<CSVBean> csvBeanList = new CsvToBeanBuilder(new FileReader(file))
                    .withType(CSVBean.class).build().parse();

            for (CSVBean csvBean : csvBeanList) {
                System.out.println(csvBean.getProdId() + "   " + csvBean.getRating() + "   " + csvBean.getUsername());
                ReviewEntity re = new ReviewEntity();
                re.setProdId(csvBean.getProdId());
                re.setRating(csvBean.getRating());
                re.setHelpfulRating(csvBean.getHelpful_rating());
                re.setReviewdate(csvBean.getReviewdate());
                re.setUsername(csvBean.getUsername());
                re.setReviewSum(csvBean.getReviewSum());
                re.setReviewText(csvBean.getReview_text());
                GenericDao<ReviewEntity> reviewEntityDao = new GenericDao<>(sessionFactory);
                reviewEntityDao.create(re);
            }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
              }
        }
}

