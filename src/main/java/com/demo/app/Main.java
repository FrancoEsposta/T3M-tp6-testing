package com.demo.app;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String DB_PASSWORD = "admin123";
    public List<String> logs = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        UserService service = new UserService();
        OrderCalculator calc = new OrderCalculator();

        String user = args.length > 0 ? args[0] : "admin";
        String pass = args.length > 1 ? args[1] : "1234";

        if (service.login(user, pass)) {
            System.out.println("Bienvenido " + user);
        } else {
            System.out.println("Login fallido");
        }

        double total = calc.calculate("PREMIUM", 3, 150.0);
        System.out.println("Total: " + total);

        String file = args.length > 2 ? args[2] : "report.txt";
        System.out.println(service.readReport(file));

        String extra = null;
        if (args.length > 3) {
            extra = args[3];
        }
        System.out.println(extra.length());
    }
}
