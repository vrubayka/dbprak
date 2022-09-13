package frontend;

import entities.InventoryEntity;
import entities.ProductEntity;
import entities.ReviewEntity;
import logging.exceptions.AlreadyInDatabaseException;
import logging.exceptions.ProductNotInDatabaseException;
import middle.MenuMapper;
import middle.wrapperClass.CategoryNode;
import middle.wrapperClass.User;
import org.apache.commons.lang3.StringUtils;


import java.time.LocalDate;
import java.util.*;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final MenuMapper mapper = new MenuMapper();

    private final String[] options = {"1 - finish", "2 - getProduct", "3 - getProducts(String pattern)",
                                      "4 - getCategoryTree",
                                      "5 - getProductsByCategoryPath", "6 - getTopProducts",
                                      "7 - getSimilarCheaperProduct", "8 - addNewReview",
                                      "9 - getTrolls", "10 - getOffers"};

    public void printMenu() {
        try {
            System.out.println("DB neu laden?");
            System.out.println("1 - Ja");
            System.out.println("2 - Nein");
            System.out.println("\nEingabe:");
            Integer boolNum = scanner.nextInt();
            scanner.nextLine();
            if (boolNum == 1) {
                mapper.init(true);
                printOptions();
            } else if (boolNum == 2) {
                mapper.init(false);
                printOptions();
            } else throw new InputMismatchException();

        } catch (InputMismatchException ex) {
            System.out.println("Bitte geben Sie eine Zahl zwischen 1 und 2 ein!\n");
            printMenu();
        }
    }

    public void printOptions() {
        try {
            System.out.println("Waehle eine Option:");
            for (String option : options) {
                System.out.println(option);
            }
            System.out.println("\nEingabe:");

            int option;

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
                default:
                    throw new InputMismatchException();
            }

        } catch (InputMismatchException ex) {
            System.out.println("\nBitte geben Sie eine Zahl zwischen 1 und " + options.length + "!\n");
            printOptions();
        }
    }

    private void option1() {
        System.out.println("Option finish ausgewaehlt");
        mapper.finish();
        System.out.println("Tschuess!");
    }

    private void option2() { //getProduct (String id)
        System.out.println("Option getProduct ausgewaehlt");
        System.out.println("Gib die Product-ID ein");
        String prodID = scanner.nextLine();
        try {
            ProductEntity product = mapper.getProduct(prodID);
            System.out.println(product);
            if (product.getCdByProdId() != null) {
                System.out.print(product.getCdByProdId().toString());
            } else if (product.getBookByProdId() != null) {
                System.out.print(product.getBookByProdId().toString());
            } else if (product.getDvdByProdId() != null) {
                System.out.print(product.getDvdByProdId().toString());
            }
        } catch (ProductNotInDatabaseException e) {
            System.out.println(e.getMessage());
            option2();
        }
    }

    private void option3() { //getProducts (String pattern)
        System.out.println("Option getProducts (String pattern)");
        System.out.println("Gib das Pattern ein");
        String pattern = scanner.nextLine();
        for (ProductEntity product : mapper.getProducts(pattern)) {
            System.out.println(product);
        }

    }

    private void option4() { //getCategoryTree
        System.out.println("Option getCategoryTree augewaehlt");
        CategoryNode root = mapper.getCategoryTree();
        printTree(0, root);
        System.out.println("\n\n\nFertig");

    }

    private void option5() { //getProductsByCategoryPath
        System.out.println("Option getProductsByCategoryPath ausgewaehlt");
        System.out.println("Gib den Pfad ein");
        System.out.println("Beispiel:");
        System.out.println("Features/Alle SACDs");
        System.out.println("Eingabe:");
        String pfad = scanner.nextLine();

        List<ProductEntity> liste = mapper.getProductsByCategoryPath(pfad);
        for (ProductEntity product : liste) {
            System.out.println(product.value());
        }
    }

    private void option6() { //getTopProducts
        System.out.println("Option getTopProducts ausgewaehlt");
        System.out.println("Gib die Anzahl der Reviews ein");
        Integer k = scanner.nextInt();
        List<Object[]> liste = mapper.getTopProducts(k);
        for (Object[] product : liste) {
            String prodID = (String) product[0];
            String rating = String.valueOf((double) product[1]);
            System.out.println(prodID + " " + rating);
        }
    }

    private void option7() { //getSimilarCheaperProduct
        System.out.println("Option getSimilarCheaperProduct ausgewaehlt");
        System.out.println("Gib die ProduktID ein");
        String prodID = scanner.nextLine();
        try {
            List<ProductEntity> liste = mapper.getSimilarCheaperProduct(prodID);
            if (liste.isEmpty()) {
                System.out.println("Es gibt keine billigeren ähnlichen Produkte");
            }
            for (ProductEntity produkt : liste) {
                System.out.println(produkt);
            }
        } catch (ProductNotInDatabaseException e) {
            System.out.println(e.getMessage());
            option7();
        }
    }

    private void option8() { //addNewReview
        try {
            System.out.println("Option addNewReview ausgewaehlt");
            System.out.println("1 - existierenden Review zeigen\n2 - neues Review schreiben");
            String auswahl = scanner.nextLine();
            ReviewEntity output;
            if (auswahl.equals("1")) {
                System.out.println("Geben Sie die Produkt-ID ein");
                String prodId = scanner.nextLine();
                System.out.println("Geben Sie den Username ein");
                String username = scanner.nextLine();
                ReviewEntity review = new ReviewEntity();
                review.setUsername(username);
                review.setProdId(prodId);
                output = mapper.addNewReview(review, false);
                if (output == null) {
                    System.out.println("Kein Review gefunden");
                } else System.out.println("\n" + output);
            } else if (auswahl.equals("2")) {
                System.out.println("Geben Sie die Produkt-ID ein");
                String prodId = scanner.nextLine();

                System.out.println("Geben Sie den Username ein");
                String username = scanner.nextLine();

                System.out.println("Geben Sie das Rating ein");
                int rating = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Geben Sie die Zusammenfassung ein");
                String summary = scanner.nextLine();

                java.sql.Date date = null;
                date = (java.sql.Date.valueOf(LocalDate.now()));


                System.out.println("Geben Sie das Review ein");
                String reviewText = scanner.nextLine();


                ReviewEntity review = new ReviewEntity();
                review.setHelpfulRating(0);
                review.setReviewText(reviewText);
                review.setProdId(prodId);
                review.setRating(rating);
                review.setReviewdate(date);
                review.setUsername(username);
                review.setReviewSum(summary);
                mapper.addNewReview(review, true);
                System.out.println("Fertig");
            }
        } catch (AlreadyInDatabaseException | ProductNotInDatabaseException pe) {
            System.out.println(pe.getMessage());
            option8();
        }
    }

    private void option9() { //getTrolls
        System.out.println("Option getTrolls gewaehlt");
        System.out.println("Gib das Rating ein: ");
        Double rating = scanner.nextDouble();
        scanner.nextLine();
        List<User> userList = mapper.getTrolls(rating);
        for (User user : userList) {
            System.out.println(user.getUsername());
        }
    }

    private void option10() {
        System.out.println("Option getOffers gewaehlt");
        System.out.println("Gib die ProduktID ein");
        String prodId = scanner.nextLine();
        List<InventoryEntity> liste;
        liste = mapper.getOffers(prodId);
        for (InventoryEntity inventory : liste) {
            System.out.println(inventory);
        }
        System.out.println("Fertig");

    }

    public void printTree(int x, CategoryNode node) {
        System.out.println(StringUtils.repeat("\t", x) + "|--" + node.getValue());

        for (CategoryNode child : node.getChildCategories()) {
            printTree(x + 1, child);
        }
    }


}
