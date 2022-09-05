package frontend;

import org.hibernate.internal.util.collections.SingletonIterator;

import java.sql.SQLSyntaxErrorException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);

    String[] options = {"1 - init", "2 - finish", "3 - getProduct", "4 - getProducts(String pattern)", "5 - getCategoryTree",
            "6 - getProductsByCategoryPath", "7 - getTopProducts", "8 - getSimilarCheaperProduct", "9 - addNewReview",
            "10 - getTrolls", "11 - getOffers", "12 - Ende"};

    public static void printMenu(String[] options) {
        System.out.println("Waehle eine Option:");
        for (String option : options) {
            System.out.println(option);
        }

        int option;
        while (true) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option){
                    case 1: option1(); break;
                    case 2: option2(); break;
                    case 3: option3(); break;
                    case 4: option4(); break;
                    case 5: option5(); break;
                    case 6: option6(); break;
                    case 7: option7(); break;
                    case 8: option8(); break;
                    case 9: option9(); break;
                    case 10: option10(); break;
                    case 11: option11(); break;
                    case 12: exit(0);
                }

        }
            catch (Exception ex){
                System.out.println("Bitte geben Sie ein Zahl zwischen 1 und " + options.length);
                scanner.next();
            }
    }
}

    private static void option1() {
        System.out.println("Option init gewählt");
        System.out.println("Wollen Sie den Datenbank neu laden");
        System.out.println("1 - JA");
        System.out.println("2 - NEIN");
        Integer option = scanner.nextInt();
        if (option == 1){
            System.out.println("DB wird neu geladen, bitte warten");
            //TODO: cleanDB durchführen
        }
        else {
            System.out.println("DB wird nicht neu geladen");
            //TODO: init ohne cleanDB
        };
    }

    private static void option2() {
        System.out.println("Option finish ausgewaehlt");
        //TODO: db finish
    }

    private static void option3() {
        System.out.println("Option getProduct ausgewaehlt");
        System.out.println("Gib den Product-ID ein");
        String prodID = scanner.next();
        //TODO: prodID uebergben

    }

    private static void option4() { //getProducts (String pattern)
        System.out.println("Option getProucts (String pattern)");
        System.out.println("Gib den Pattern ein");
        String pattern = scanner.next();
        //TODO: pattern an SQL-like Befehl übergeben
    }

    private static void option5() { //getCategoryTree
        System.out.println("Option getCategoryTree augewaehlt");
        //TODO: was soll ermittelt werden
        System.out.println("Gib productID ein");
        String prodID = scanner.next();
    }

    private static void option6() { //getProductsByCategoryPath
        System.out.println("Option getProductsByCategoryPath ausgewaehlt");
        System.out.println("Gib den Pfad ein");
        String pfad = scanner.next();
        //TODO Pfad übergeben und Producten zurueckgeben
    }

    private static void option7() { //getTopProducts
        System.out.println("Option getTopProducts ausgewaehlt");
        System.out.println("Gib den Rating ein");
        Integer rating = scanner.nextInt();
        //TODO: alle Produkte denen Rating >= rating-Wert ist.
    }

    private static void option8() { //getSimilarCheaperProduct
        System.out.println("Option getSimilarCheaperProduct ausgewaehlt");
        System.out.println("Gib ProduktID ein");
        Integer prodID = scanner.nextInt();
        //TODO: prodID aehnliche und billiger Produkte
    }

    private static void option9() { //addNewReview
        System.out.println("Option addNewReview ausgewaehlt");
        System.out.println("Gib ");
    }

    private static void option10() {
        
    }

    private static void option11(){

    }
    

}
