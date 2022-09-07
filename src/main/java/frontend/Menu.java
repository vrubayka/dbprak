package frontend;

import entities.BookEntity;
import entities.CdEntity;
import entities.DvdEntity;
import entities.ProductEntity;
import middle.MenuMapper;
import org.hibernate.SessionFactory;
import org.hibernate.internal.util.collections.SingletonIterator;

import java.sql.SQLSyntaxErrorException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {

    private Scanner scanner = new Scanner(System.in);

    private MenuMapper mapper = new MenuMapper();

    private String[] options = {"1 - finish", "2 - getProduct", "3 - getProducts(String pattern)", "4 - getCategoryTree",
            "5 - getProductsByCategoryPath", "6 - getTopProducts", "7 - getSimilarCheaperProduct", "8 - addNewReview",
            "9 - getTrolls", "10 - getOffers"};

    public void printMenu() {
        System.out.println("DB neu laden?");
        System.out.println("1 - Ja");
        System.out.println("2 - Nein");
        System.out.println("\nEingabe:");
        Integer boolNum = scanner.nextInt();
        if (boolNum == 1){
         mapper.init(true);
        }
        else mapper.init(false);

        System.out.println("Waehle eine Option:");
        for (String option : options) {
            System.out.println(option);
        }
        System.out.println("\nEingabe:");

        int option;
        //try {
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    option1();
                    break;
                case 2:
                    option2();
                    break;
                case 3:
                    option3();
                    break;
                case 4:
                    option4();
                    break;
                case 5:
                    option5();
                    break;
                case 6:
                    option6();
                    break;
                case 7:
                    option7();
                    break;
                case 8:
                    option8();
                    break;
                case 9:
                    option9();
                    break;
                case 10:
                    option10();
                    break;
            }

        /*} /*catch (Exception ex) {
            System.out.println("Bitte geben Sie ein Zahl zwischen 1 und " + options.length + "!");
            scanner.next();
        }*/
    }

    private void option1() {
        System.out.println("Option finish ausgewaehlt");
        mapper.finish();
        System.out.println("Tschuess!");
    }

    private void option2() {
        System.out.println("Option getProduct ausgewaehlt");
        System.out.println("Gib den Product-ID ein");
        String prodID = scanner.next();
        ProductEntity product = mapper.getProduct(prodID);
        System.out.println(product);
        if (product.getCdByProdId() != null){
            System.out.print(product.getCdByProdId().toString());
        }
        else if (product.getBookByProdId() != null){
            System.out.print(product.getBookByProdId().toString());
        }
        else if (product.getDvdByProdId() != null) {
            System.out.print(product.getDvdByProdId().toString());
        }
    }

    private void option3() { //getProducts (String pattern)
        System.out.println("Option getProucts (String pattern)");
        System.out.println("Gib den Pattern ein");
        String pattern = scanner.next();
        for(ProductEntity product : mapper.getProducts(pattern)){
            System.out.println(product);
        }

    }

    private void option4() { //getCategoryTree
        System.out.println("Option getCategoryTree augewaehlt");

    }

    private void option5() { //getProductsByCategoryPath
        System.out.println("Option getProductsByCategoryPath ausgewaehlt");
        System.out.println("Gib den Pfad ein");
        String pfad = scanner.next();
        //TODO Pfad übergeben und Producten zurueckgeben
    }

    private void option6() { //getTopProducts
        System.out.println("Option getTopProducts ausgewaehlt");
        System.out.println("Gib den Rating ein");
        Integer rating = scanner.nextInt();
        //TODO: alle Produkte denen Rating >= rating-Wert ist.
    }

    private void option7() { //getSimilarCheaperProduct
        System.out.println("Option getSimilarCheaperProduct ausgewaehlt");
        System.out.println("Gib ProduktID ein");
        Integer prodID = scanner.nextInt();
        //TODO: prodID aehnliche und billiger Produkte
    }

    private void option8() { //addNewReview
        System.out.println("Option addNewReview ausgewaehlt");
        System.out.println("Gib ");
    }

    private void option9() {

    }

    private void option10() {

    }


}
