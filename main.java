package database;

import java.sql.*;
import java.util.Scanner;

public class COP3703 {
    //given constants to connect to database
    private static final String URL = "jdbc:mysql://139.62.210.180:3306/cop3703_8";
    private static final String USER = "cop3703_8";
    private static final String PASSWORD = "123456789!";

    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database.");

            boolean running = true;

            while (running) {
                System.out.println("\nMENU");
                System.out.println("1. View Customers");
                System.out.println("2. Add Customer");
                System.out.println("3. Update Customer");
                System.out.println("4. Delete Customer");
                System.out.println("5. View Products");
                System.out.println("6. Add Product");
                System.out.println("7. Update Product");
                System.out.println("8. Delete Product");
                System.out.println("9. View Transactions");
                System.out.println("10. Add Transaction");
                System.out.println("11. Delete Transaction");
                System.out.println("12. Add Item to Transaction");
                System.out.println("13. View Items in a Transaction");
                System.out.println("14. Remove Item from Transaction");
                System.out.println("15. Exit");
                System.out.print("Enter your choice (1-15): ");

		//store user choice in choice variable
		int choice = scanner.nextInt();
                scanner.nextLine(); //read user input

                //menu switch case structure
		switch (choice) {
                    case 1:
                        viewCustomers(connection);
                        break;
                    case 2:
                        addCustomer(connection, scanner);
                        break;
                    case 3:
                        updateCustomer(connection, scanner);
                        break;
                    case 4:
                        deleteCustomer(connection, scanner);
                        break;
                    case 5:
                        viewProducts(connection);
                        break;
                    case 6:
                        addProduct(connection, scanner);
                        break;
                    case 7:
                        updateProduct(connection, scanner);
                        break;
                    case 8:
                        deleteProduct(connection, scanner);
                        break;
                    case 9:
                        viewTransactions(connection);
                        break;
                    case 10:
                        addTransaction(connection, scanner);
                        break;
                    case 11:
                        deleteTransaction(connection, scanner);
                        break;
                    case 12:
                        addItemToTransaction(connection, scanner);
                        break;
                    case 13:
                        viewTransactionItems(connection, scanner);
                        break;
                    case 14:
                        removeItemFromTransaction(connection, scanner);
                        break;
                    case 15:
                        System.out.println("Exiting...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
	    //close scanner
            scanner.close();
        }
    }

    // CUSTOMER MANAGEMENT
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static void viewCustomers(Connection connection) {
        String query = "SELECT customerID, name, phoneNumber, customerType FROM Customer";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\nCustomer List:");
            while (rs.next()) {
                System.out.format("ID: %d, Name: %s, Phone: %s, Type: %d\n",rs.getInt("customerID"),rs.getString("name"),rs.getString("phoneNumber"),rs.getInt("customerType"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch customer data.");
            e.printStackTrace();
        }
    }

    public static void addCustomer(Connection connection, Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
	    
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
	    
        System.out.print("Enter customer type: ");
        int customerType = scanner.nextInt();
	    
        scanner.nextLine(); 

        String query = String.format("INSERT INTO Customer (name, phoneNumber, customerType) VALUES ('%s', '%s', %d)",name, phoneNumber, customerType);

        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add customer.");
            e.printStackTrace();
        }
    }

    public static void updateCustomer(Connection connection, Scanner scanner) {
        System.out.print("Enter customer ID to update: ");
	    
        int customerID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
	    
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
	    
        System.out.print("Enter new phone number: ");
        String phoneNumber = scanner.nextLine();

        String query = String.format("UPDATE Customer SET name = '%s', phoneNumber = '%s' WHERE customerID = %d", name, phoneNumber, customerID);

        try (Statement st = connection.createStatement()) {
            int rowsAffected = st.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("No customer found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update customer.");
            e.printStackTrace();
        }
    }

    public static void deleteCustomer(Connection connection, Scanner scanner) {
        System.out.print("Enter customer ID to delete: ");
        int customerID = scanner.nextInt();
	    
        scanner.nextLine();

        String query = String.format("DELETE FROM Customer WHERE customerID = %d", customerID);

        try (Statement st = connection.createStatement()) {
            int rowsAffected = st.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("No customer found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete customer.");
            e.printStackTrace();
        }
    }

    // PRODUCT MANAGEMENT
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void viewProducts(Connection connection) {
        String query = "SELECT upcBarcode, productName, price FROM Item";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\nProduct List:");
            while (rs.next()) {
                System.out.format("UPC: %s, Name: %s, Price: %.2f\n", rs.getString("upcBarcode"), rs.getString("productName"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch product data.");
            e.printStackTrace();
        }
    }

    public static void addProduct(Connection connection, Scanner scanner) {
        System.out.print("Enter product UPC: ");
        String upcBarcode = scanner.nextLine();
	    
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
	    
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
	    
        scanner.nextLine(); 

        String query = String.format(
                "INSERT INTO Item (upcBarcode, productName, price) VALUES ('%s', '%s', %.2f)",
                upcBarcode, productName, price);

        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add product.");
            e.printStackTrace();
        }
    }

    public static void updateProduct(Connection connection, Scanner scanner) {
        System.out.print("Enter product UPC to update: ");
        String upcBarcode = scanner.nextLine();
	    
        System.out.print("Enter new product name: ");
        String productName = scanner.nextLine();
	    
        System.out.print("Enter new product price: ");
        double price = scanner.nextDouble();
	    
        scanner.nextLine(); 

        String query = String.format(
                "UPDATE Item SET productName = '%s', price = %.2f WHERE upcBarcode = '%s'", productName, price, upcBarcode);

        try (Statement st = connection.createStatement()) {
            int rowsAffected = st.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("No product found with the given UPC.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update product.");
            e.printStackTrace();
        }
    }

    public static void deleteProduct(Connection connection, Scanner scanner) {
        System.out.print("Enter product UPC to delete: ");
        String upcBarcode = scanner.nextLine();

        String query = String.format("DELETE FROM Item WHERE upcBarcode = '%s'", upcBarcode);

        try (Statement st = connection.createStatement()) {
            int rowsAffected = st.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("No product found with the given UPC.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete product.");
            e.printStackTrace();
        }
    }
    
    
	// TRANSACTION MANAGEMENT
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	public static void viewTransactions(Connection connection) {
		String query = "SELECT transID, customerID, timeStamp, paymentType, totalAmount FROM Transaction";
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {
	
			System.out.println("\nTransaction List:");
			while (rs.next()) {
				System.out.format("Transaction ID: %d, Customer ID: %d, Timestamp: %s, Payment Type: %s, Total Amount: %.2f\n", rs.getInt("transID"), rs.getInt("customerID"), rs.getTimestamp("timeStamp"), rs.getString("paymentType"), rs.getDouble("totalAmount"));
			}
		} 
		catch (SQLException e) {
			System.out.println("Failed to fetch transaction data.");
			e.printStackTrace();
	}
	}
	
	public static void addTransaction(Connection connection, Scanner scanner) {
		System.out.print("Enter Customer ID: ");
		int customerID = scanner.nextInt();
		
		scanner.nextLine(); 
		System.out.print("Enter Payment Type (e.g., Credit, Debit): ");
		String paymentType = scanner.nextLine();
		
		System.out.print("Enter Total Amount: ");
		double totalAmount = scanner.nextDouble();
		
		scanner.nextLine(); 
	
		String query = String.format(
				"INSERT INTO Transaction (customerID, timeStamp, paymentType, totalAmount) VALUES (%d, NOW(), '%s', %.2f)", customerID, paymentType, totalAmount);
	
		try (Statement st = connection.createStatement()) {
				st.executeUpdate(query);
				System.out.println("Transaction added successfully.");
		} 
		catch (SQLException e) {
			System.out.println("Failed to add transaction.");
			e.printStackTrace();
		}
	}
	
	public static void deleteTransaction(Connection connection, Scanner scanner) {
		System.out.print("Enter Transaction ID to delete: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline
	
		String query = String.format("DELETE FROM Transaction WHERE transID = %d", transID);
	
		try (Statement st = connection.createStatement()) {
			int rowsAffected = st.executeUpdate(query);
			if (rowsAffected > 0) {
				System.out.println("Transaction deleted successfully.");
			} 
			else {
				System.out.println("No transaction found with the given ID.");
			}
		} 
		catch (SQLException e) {
			System.out.println("Failed to delete transaction.");
			e.printStackTrace();
		}
	}
	
	//TRANSACTION ITEMS
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void addItemToTransaction(Connection connection, Scanner scanner) {
		System.out.print("Enter Transaction ID: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		System.out.print("Enter Product UPC: ");
		String upcBarcode = scanner.nextLine();
		System.out.print("Enter Quantity: ");
		int quantity = scanner.nextInt();
		scanner.nextLine(); // Consume newline
	
		String query = String.format("INSERT INTO CartItem (transID, upcBarcode, quantity) VALUES (%d, '%s', %d)", transID, upcBarcode, quantity);
	
		try (Statement st = connection.createStatement()) {
			st.executeUpdate(query);
			System.out.println("Item added to transaction successfully.");
		} 
		catch (SQLException e) {
			System.out.println("Failed to add item to transaction.");
			e.printStackTrace();
		}
	}
	
	public static void viewTransactionItems(Connection connection, Scanner scanner) {
		System.out.print("Enter Transaction ID to view items: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline
	
		String query = String.format("SELECT CartItem.transID, CartItem.upcBarcode, Item.productName, CartItem.quantity " +"FROM CartItem " + "JOIN Item ON CartItem.upcBarcode = Item.upcBarcode " + "WHERE CartItem.transID = %d", transID);
	
	try (Statement st = connection.createStatement();
	ResultSet rs = st.executeQuery(query)) {
	
	System.out.println("\nTransaction Items:");
	while (rs.next()) {
	System.out.format("Transaction ID: %d, UPC: %s, Product: %s, Quantity: %d\n", rs.getInt("transID"), rs.getString("upcBarcode"), rs.getString("productName"), rs.getInt("quantity"));
	}
	} catch (SQLException e) {
	System.out.println("Failed to fetch transaction items.");
	e.printStackTrace();
	}
	}
	
	public static void removeItemFromTransaction(Connection connection, Scanner scanner) {
		System.out.print("Enter Transaction ID: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		System.out.print("Enter Product UPC to remove: ");
		String upcBarcode = scanner.nextLine();
	
		String query = String.format("DELETE FROM CartItem WHERE transID = %d AND upcBarcode = '%s'",transID, upcBarcode);
	
		try (Statement st = connection.createStatement()) {
			int rowsAffected = st.executeUpdate(query);
			if (rowsAffected > 0) {
				System.out.println("Item removed from transaction successfully.");
			} 
			else {
				System.out.println("No item found with the given details.");
			}
		} 
		catch (SQLException e) {
			System.out.println("Failed to remove item from transaction.");
			e.printStackTrace();
		}
	}

}

