package com.cars;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Collection<Car> cars = new ArrayList<>();
        // Map to keep track of repair costs for each car (key by VIN)
        Map<String, Double> repairCosts = new HashMap<>();

        System.out.println("👋 Hello! What is your name?");
        String userName = input.nextLine();

        System.out.println("🚗 Welcome, " + userName + "! Let's get started...\n");

        int menuOption;

        do {
            System.out.println("📋 What would you like to do, " + userName + "?");
            System.out.println("(1) 📝 Capture vehicle details");
            System.out.println("(2) 📄 View vehicle report");
            System.out.println("(3) 🛠️ Add repair costs to a vehicle");
            System.out.println("(4) ❌ Exit app");
            menuOption = input.nextInt();
            input.nextLine(); // consume newline

            if (menuOption == 1) {
                Car carObj = new Car();
                int year, mileage;
                String make, model, vinNum, plateNum = "";

                System.out.println("Enter vehicle make:");
                make = input.nextLine();

                System.out.println("Enter vehicle model:");
                model = input.nextLine();

                System.out.println("Enter VIN (17 characters):");
                vinNum = input.nextLine();
                while (vinNum.length() != 17) {
                    System.out.println("⚠️ VIN must be exactly 17 characters. Try again:");
                    vinNum = input.nextLine();
                }

                System.out.println("Please enter (1) for old format license plate number or (2) for new format:");
                int plateChoice = input.nextInt();
                input.nextLine();

                if (plateChoice == 1) {
                    System.out.println("Enter old format plate number (e.g. AAA555GP):");
                    plateNum = input.nextLine();
                    while (!plateNum.matches("[A-Z]{3}[0-9]{3}[A-Z]{2}")) {
                        System.out.println("⚠️ Invalid old format plate. Try again (e.g. ABC123GP):");
                        plateNum = input.nextLine();
                    }
                } else if (plateChoice == 2) {
                    System.out.println("Enter new format plate number (e.g. AA66BB GP):");
                    plateNum = input.nextLine();
                    while (!plateNum.matches("[A-Z]{2}[0-9]{2}[A-Z]{2}\\s[A-Z]{2}")) {
                        System.out.println("⚠️ Invalid new format plate. Try again (e.g. AA66BB GP):");
                        plateNum = input.nextLine();
                    }
                }

                System.out.println("Enter mileage (numbers only, no commas or 'km'):");
                while (!input.hasNextInt()) {
                    System.out.println("⚠️ Invalid input. Please enter mileage as a number without commas or letters (e.g., 85000):");
                    input.next();
                }
                mileage = input.nextInt();

                System.out.println("Enter year:");
                while (!input.hasNextInt()) {
                    System.out.println("⚠️ Invalid input. Please enter year as a number (e.g., 2020):");
                    input.next();
                }
                year = input.nextInt();
                input.nextLine();

                carObj.setMake(make);
                carObj.setModel(model);
                carObj.setVin(vinNum);
                carObj.setPlateNumber(plateNum);
                carObj.setMillage(mileage);
                carObj.setYear(year);

                cars.add(carObj);

                System.out.println("✅ Vehicle successfully added, " + userName + "!");

                // Show vehicle health status
                System.out.println("🩺 Vehicle health status: " + evaluateHealth(year, mileage));
                System.out.println();

            } else if (menuOption == 2) {
                if (cars.isEmpty()) {
                    System.out.println("🚫 There are no cars captured yet, " + userName + ".");
                } else {
                    System.out.println("***********************");
                    System.out.println("🚘 VEHICLE REPORT");
                    System.out.println("***********************");
                    for (Car car : cars) {
                        System.out.println("MAKE: " + car.getMake());
                        System.out.println("MODEL: " + car.getModel());
                        System.out.println("PLATE: " + car.getPlateNumber());
                        System.out.println("VIN: " + car.getVin());
                        System.out.println("YEAR: " + car.getYear());
                        System.out.println("MILEAGE: " + car.getMillage());

                        // Show repair costs if available
                        double cost = repairCosts.getOrDefault(car.getVin(), 0.0);
                        System.out.printf("ESTIMATED REPAIR COST: R%.2f\n", cost);
                        System.out.println("---------------------");
                    }
                }

            } else if (menuOption == 3) {
                if (cars.isEmpty()) {
                    System.out.println("🚫 No vehicles captured yet to add repair costs.");
                } else {
                    System.out.println("Enter VIN of the vehicle you want to add repair costs to:");
                    String searchVin = input.nextLine();

                    Car foundCar = null;
                    for (Car car : cars) {
                        if (car.getVin().equalsIgnoreCase(searchVin)) {
                            foundCar = car;
                            break;
                        }
                    }

                    if (foundCar == null) {
                        System.out.println("❌ Vehicle with VIN '" + searchVin + "' not found.");
                    } else {
                        System.out.println("Enter repair cost amount (in Rands):");
                        while (!input.hasNextDouble()) {
                            System.out.println("⚠️ Invalid input. Please enter a valid number for cost (e.g., 2500.75):");
                            input.next();
                        }
                        double cost = input.nextDouble();
                        input.nextLine();

                        // Add repair cost to existing or start new
                        double prevCost = repairCosts.getOrDefault(searchVin, 0.0);
                        repairCosts.put(searchVin, prevCost + cost);

                        System.out.printf("✅ Added R%.2f repair cost to vehicle VIN %s.\n\n", cost, searchVin);
                    }
                }

            } else if (menuOption == 4) {
                System.out.println("Are you sure you want to exit? (y/n)");
                String confirmExit = input.nextLine();
                if (confirmExit.equalsIgnoreCase("y")) {
                    break;
                } else {
                    menuOption = 0; // prevent exit
                }
            } else {
                System.out.println("⚠️ Invalid option. Please select 1, 2, 3, or 4.");
            }

        } while (menuOption != 4);

        System.out.println("👋 Thank you for using the app, " + userName + "! See you next time.");
    }

    // Vehicle health evaluation based on year and mileage
    private static String evaluateHealth(int year, int mileage) {
        int currentYear = java.time.Year.now().getValue();
        int age = currentYear - year;

        if (age <= 3 && mileage < 50000) {
            return "Excellent condition 🚗💨";
        } else if (age <= 7 && mileage < 120000) {
            return "Good condition 👍";
        } else if (age <= 12 && mileage < 200000) {
            return "Fair condition ⚠️";
        } else {
            return "Needs maintenance 🛠️";
        }
    }
}
