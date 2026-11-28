package logic;

import java.util.Scanner;

/**
 * ScannerHelper
 * utility class to make sharing the scanner class possible
 */
public class ScannerHelper {

    // class property
    private static Scanner scanner;

    // setter
    public static void SetScanner(Scanner scan) {
        scanner = scan;
    }

    // getter
    public static Scanner GetScanner() {
        return scanner;
    }
}
