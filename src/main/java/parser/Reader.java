package parser;

import org.hibernate.SessionFactory;

public interface Reader {

    public void readFile(String filePath, SessionFactory sessionFactory);

}


