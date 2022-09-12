package frontend;

import entities.*;
import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import middle.MenuMapper;
import middle.wrapperClass.CategoryNode;
import middle.wrapperClass.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.internal.util.collections.SingletonIterator;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final MenuMapper mapper = new MenuMapper();

    private final String[] options = {"1 - finish", "2 - getProduct", "3 - getProducts(String pattern)", "4 - getCategoryTree",
            "5 - getProductsByCategoryPath", "6 - getTopProducts", "7 - getSimilarCheaperProduct", "8 - addNewReview",
            "9 - getTrolls", "10 - getOffers"};

    public void printMenu() {
        System.out.println("DB neu laden?");
        System.out.println("1 - Ja\n2 - Nein\nEingabe:");
        int boolNum = scanner.nextInt();
        mapper.init(boolNum == 1);

        System.out.println("Waehle eine Option:");
        for (String option : options) {
            System.out.println(option);
        }
        System.out.println("\nEingabe:");

        int option;
        //try {
            option = scanner.nextInt();
            scanner.nextLine();
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
        System.out.println("Option finish ausgewaehlt\nTschuess!");
        mapper.finish();
    }

    private void option2() {
        System.out.println("Option getProduct ausgewaehlt\nGib den Product-ID ein");
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
        System.out.println("Option getProucts (String pattern)\nGib den Pattern ein");
        String pattern = scanner.nextLine();
        List<ProductEntity> liste = mapper.getProducts(pattern);
        for(ProductEntity product : liste){
            System.out.println(product);
        }
        System.out.println(liste.size());

    }

    private void option4() { //getCategoryTree
        System.out.println("Option getCategoryTree augewaehlt\nBitte warten Sie...\n");
        CategoryNode root = mapper.getCategoryTree();
        printTree(0, root);
        System.out.println("\n\n\nFertig");

    }

    private void option5() { //getProductsByCategoryPath
        System.out.println("Option getProductsByCategoryPath ausgewaehlt\nGib den Pfad ein\nBeispiel:" +
                "\nFeatures/Alle SACDs\nEingabe:");
        scanner.nextLine();
        String pfad = scanner.nextLine();

        List<ProductEntity> liste = mapper.getProductsByCategoryPath(pfad);
        for (ProductEntity product : liste){
            System.out.println(product.value());
        }
    }

    private void option6() { //getTopProducts
        System.out.println("Option getTopProducts ausgewaehlt\nGib den Rating ein\"");
        Integer rating = scanner.nextInt();
        //TODO: alle Produkte denen Rating >= rating-Wert ist.
    }

    private void option7() { //getSimilarCheaperProduct
        System.out.println("Option getSimilarCheaperProduct ausgewaehlt\nGib ProduktID ein");
        Integer prodID = scanner.nextInt();
        //TODO: prodID aehnliche und billiger Produkte
    }

    private void option8() { //addNewReview
        System.out.println("Option addNewReview ausgewaehlt");
        System.out.println("Gib ");
    }

    private void option9() { //getTrolls
        System.out.println("Option getTrolls gewaehlt");
        System.out.println("Gib den Rating ein: ");
        Double rating = scanner.nextDouble();
        scanner.nextLine();
        List<User> userList = mapper.getTrolls(rating);
        for (User user : userList){
            System.out.println(user.getUsername());
        }
    }

    private void option10() {

    }

    public void printTree(int x, CategoryNode node){
        System.out.println(StringUtils.repeat("\t", x) + "|--" + node.getValue());
        for (CategoryNode child : node.getChildCategories() ){
                printTree(x+1, child);
        }
    }


}
