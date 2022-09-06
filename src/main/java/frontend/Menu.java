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

    private String[] options = {"1 - init", "2 - finish", "3 - getProduct", "4 - getProducts(String pattern)", "5 - getCategoryTree",
            "6 - getProductsByCategoryPath", "7 - getTopProducts", "8 - getSimilarCheaperProduct", "9 - addNewReview",
            "10 - getTrolls", "11 - getOffers"};

    public void printMenu() {
        System.out.println("DB neu laden?");
        System.out.println("1 - Ja");
        System.out.println("2 - Nein");
        Integer boolNum = scanner.nextInt();
        if (boolNum == 1){
         mapper.init(true);
        }
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
                case 11:
                    option11();
                    break;
            }

        /*} /*catch (Exception ex) {
            System.out.println("Bitte geben Sie ein Zahl zwischen 1 und " + options.length + "!");
            scanner.next();
        }*/
    }

    private void option1() {
        System.out.println("Option init gewählt");
        System.out.println("Wollen Sie den Datenbank neu laden");
        System.out.println("1 - JA");
        System.out.println("2 - NEIN");
        Integer option = scanner.nextInt();
        if (option == 1) {
            System.out.println("DB wird neu geladen, bitte warten");
            //TODO: cleanDB durchführen

        } else {
            System.out.println("DB wird nicht neu geladen");
            //TODO: init ohne cleanDB
            mapper.init();
        }
        ;
    }

    private void option2() {
        System.out.println("Option finish ausgewaehlt");
        mapper.finish();
        //TODO: db finish
    }

    private void option3() {
        System.out.println("Option getProduct ausgewaehlt");
        System.out.println("Gib den Product-ID ein");
        String prodID = scanner.next();
        ProductEntity product = mapper.getProduct(prodID);
        CdEntity cdProduct = product.getCdByProdId();
        DvdEntity dvdProduct = product.getDvdByProdId();
        BookEntity bookEntity = product.getBookByProdId();
        System.out.println(cdProduct.getCdId());
        /*String[] lines = {"ASIN: " + product.getProdId(),"Name " + product.getProdName(),
        product.get};*/
        //TODO: Detailinformationen über Produkt ausgeben
    }

    private void option4() { //getProducts (String pattern)
        System.out.println("Option getProucts (String pattern)");
        System.out.println("Gib den Pattern ein");
        String pattern = scanner.next();
        //mapper.getProducts(pattern);
        //TODO: Produkte ausgeben
    }

    private void option5() { //getCategoryTree
        System.out.println("Option getCategoryTree augewaehlt");
        //TODO: was soll ermittelt werden
        System.out.println("Gib productID ein");
        String prodID = scanner.next();
    }

    private void option6() { //getProductsByCategoryPath
        System.out.println("Option getProductsByCategoryPath ausgewaehlt");
        System.out.println("Gib den Pfad ein");
        String pfad = scanner.next();
        //TODO Pfad übergeben und Producten zurueckgeben
    }

    private void option7() { //getTopProducts
        System.out.println("Option getTopProducts ausgewaehlt");
        System.out.println("Gib den Rating ein");
        Integer rating = scanner.nextInt();
        //TODO: alle Produkte denen Rating >= rating-Wert ist.
    }

    private void option8() { //getSimilarCheaperProduct
        System.out.println("Option getSimilarCheaperProduct ausgewaehlt");
        System.out.println("Gib ProduktID ein");
        Integer prodID = scanner.nextInt();
        //TODO: prodID aehnliche und billiger Produkte
    }

    private void option9() { //addNewReview
        System.out.println("Option addNewReview ausgewaehlt");
        System.out.println("Gib ");
    }

    private void option10() {

    }

    private void option11() {

    }


}
