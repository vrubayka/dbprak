import daos.ProductDao;
import entities.*;
import logging.ReadLog;
import middle.IMenuMapper;
import middle.MenuMapper;
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

        IMenuMapper mapper = new MenuMapper();
        mapper.init(false);
        mapper.getProductsByCategoryPath("Features/Alle Sacs/Foo/Baa");

    }
}
