package database;
import java.sql.*;
import java.util.Scanner;

// The main class for our Point-of-Sale (POS) Self-Checkout System
public class COP3703 {
	// All of our database connection details & constants
	private static final String URL = "jdbc:mysql://139.62.210.180:3306/cop3703_8";
	private static final String USER = "cop3703_8";
	private static final String PASSWORD = "123456789!";

	// Starting point of the application
	public static void main(String[] args) {
		Connection connection = null;
		Scanner scanner = new Scanner(System.in);

		try {
			// Where we establish a connection to the MySQL database
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connected to database.");

			// Flag check to keep the program running on a loop
			boolean running = true;

			// This displays our main menu and handles user's choices
			while (running) {
				// Main menu options for the user
				System.out.println("\nMENU");
				System.out.println("1. View Customers");
				System.out.println("2. Create User Account");
				System.out.println("3. Login to Account");
				System.out.println("4. Update Account Information");
				System.out.println("5. Forgot Password");
				System.out.println("6. Change Password After Login");
				System.out.println("7. Delete User Account");
				System.out.println("8. View Products");
				System.out.println("9. Add Product");
				System.out.println("10. Update Product");
				System.out.println("11. Delete Product");
				System.out.println("12. View Transactions");
				System.out.println("13. Add Transaction");
				System.out.println("14. Delete Transaction");
				System.out.println("15. Change Quantity of Item");
				System.out.println("16. Add Item to Transaction");
				System.out.println("17. View Items in a Transaction");
				System.out.println("18. Remove Item from Transaction");
				System.out.println("19. Exit");
				System.out.print("Enter your choice (1-19): ");

				// Getting the user's input from the menu selection
				int choice = scanner.nextInt();
				scanner.nextLine(); // Read in user input

				// Switch case statement to handle the user's choice
				switch (choice) {
				case 1:
					// Display all customers (some may not have an account)
					viewCustomers(connection);
					break;
				case 2:
					// Add a new user account
					createUserAccount(connection, scanner);
					break;
				case 3:
					// Login to user account
					loginUserAccount(connection, scanner);
					break;
				case 4:
					// Update account information
					updateUserAccountInfo(connection, scanner);
					break;
				case 5:
					// Forgot Password (change password when not logged in)
					changePasswordBeforeLogin(connection, scanner);
					break;
				case 6:
					// Change password while logged in
					changePasswordAfterLogin(connection, scanner);
					break;
				case 7:
					// Delete a user account
					deleteUserAccount(connection, scanner);
					break;
				case 8:
					// Display all products available
					viewProducts(connection);
					break;
				case 9:
					// Add a new product
					addProduct(connection, scanner);
					break;
				case 10:
					// Update an existing product
					updateProduct(connection, scanner);
					break;
				case 11:
					// Delete a product
					deleteProduct(connection, scanner);
					break;
				case 12:
					// Show all transactions
					viewTransactions(connection);
					break;
				case 13:
					// Add a new transaction
					addTransaction(connection, scanner);
					break;
				case 14:
					// Delete a transaction
					deleteTransaction(connection, scanner);
					break;
				case 15:
					// Change the quantity of a transaction's item
					changeQuantity(connection, scanner);
					break;
				case 16:
					// Add an item to the transaction
					addItemToTransaction(connection, scanner);
					break;
				case 17:
					// View the items in a transaction
					viewTransactionItems(connection, scanner);
					break;
				case 18:
					// Remove an item from a transaction
					removeItemFromTransaction(connection, scanner);
					break;
				case 19:
					System.out.println("Exiting...");
					running = false;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					break;
				}
			}
		} catch (SQLException e) {
			// Handles errors related to any connection failures from the database or from a SQL execution
			System.out.println("Connection failed!");
			e.printStackTrace();
		} finally {
			// Makes sure everything is properly closed when the program ends
			if (connection != null) {
				try {
					connection.close();
					System.out.println("Database connection closed.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Closes the scanner
			scanner.close();
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CUSTOMER MANAGEMENT
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// This method will grab and display all customer records from the database
	public static void viewCustomers(Connection connection) {
		// SQL query to select all the customer details
		String query = "SELECT customerID, name, phoneNumber, customerType FROM Customer";
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) { // Executing the query and storing the results

			System.out.println("\nCustomer List:");
			// While loop to go through the results, and print out each customer's details
			while (rs.next()) {
				System.out.format("ID: %d, Name: %s, Phone: %s, Type: %d\n",
						rs.getInt("customerID"),
						rs.getString("name"),
						rs.getString("phoneNumber"),
						rs.getInt("customerType"));
			}
		} catch (SQLException e) {
			// Catches and handles any errors that may occur during the query execution
			System.out.println("Failed to fetch customer data.");
			e.printStackTrace();
		}
	}

	public static void createUserAccount(Connection connection, Scanner scanner) {
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		System.out.print("Enter full name: ");
		String name = scanner.nextLine();
		System.out.print("Enter phone number (xxx-xxx-xxxx): ");
		String phoneNumber = scanner.nextLine();
		System.out.print("Enter email: ");
		String email = scanner.nextLine();

		String query = String.format(
				"INSERT INTO userAccount (username, password, name, phoneNumber, email) VALUES ('%s', '%s', '%s', '%s', '%s')",
				username, password, name, phoneNumber, email);

		try (Statement st = connection.createStatement()) {
			st.executeUpdate(query);
			System.out.println("User account created successfully.");
		} catch (SQLException e) {
			System.out.println("Failed to create user account.");
			e.printStackTrace();
		}
	}

	public static boolean loginUserAccount(Connection connection, Scanner scanner) {
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		String query = String.format(
				"SELECT * FROM userAccount WHERE username = '%s' AND password = '%s'",
				username, password);

		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			if (rs.next()) {
				System.out.println("Login successful!");
				return true;
			} else {
				System.out.println("Invalid username or password.");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Failed to log in.");
			e.printStackTrace();
			return false;
		}
	}

	public static void updateUserAccountInfo(Connection connection, Scanner scanner) {
		System.out.print("Enter username to update: ");
		String username = scanner.nextLine();

		System.out.print("Enter new full name: ");
		String name = scanner.nextLine();
		System.out.print("Enter new phone number (xxx-xxx-xxxx): ");
		String phoneNumber = scanner.nextLine();
		System.out.print("Enter new email: ");
		String email = scanner.nextLine();

		String query = String.format(
				"UPDATE userAccount SET name = '%s', phoneNumber = '%s', email = '%s' WHERE username = '%s'",
				name, phoneNumber, email, username);

		try (Statement st = connection.createStatement()) {
			st.executeUpdate(query);
			System.out.println("User account updated successfully.");
		} catch (SQLException e) {
			System.out.println("Failed to update user account.");
			e.printStackTrace();
		}
	}

	public static boolean changePasswordBeforeLogin(Connection connection, Scanner scanner) {
		System.out.print("Enter username: ");
		String username = scanner.nextLine();

		// Does username exist?
		String query = String.format("SELECT * FROM userAccount WHERE username = '%s'", username);
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			if (rs.next()) {
				System.out.print("Enter new password: ");
				String newPassword = scanner.nextLine();
				String updateQuery = String.format("UPDATE userAccount SET password = '%s' WHERE username = '%s'", newPassword, username);
				st.executeUpdate(updateQuery);
				System.out.println("Password updated successfully.");
				return true;
			} else {
				System.out.println("Username not found.");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Failed to update password.");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean changePasswordAfterLogin(Connection connection, Scanner scanner) {
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		String query = String.format("SELECT password FROM userAccount WHERE username = '%s'", username);

		try (Statement st = connection.createStatement();

				ResultSet rs = st.executeQuery(query)) {
			if (rs.next()) {
				System.out.print("Enter new password: ");
				String newPassword = scanner.nextLine();
				String updateQuery = String.format("UPDATE userAccount SET password = '%s' WHERE username = '%s'", newPassword, username);
				st.executeUpdate(updateQuery);
				System.out.println("Password updated successfully.");
				return true;
			}
			else {
				System.out.println("Username not found.");
				return false;
			}

		} catch (SQLException e) {
			System.out.println("Failed to update password.");
			e.printStackTrace();
			return false;
		}
	}

	// Delete user account
	public static void deleteUserAccount(Connection connection, Scanner scanner) {
		System.out.print("Enter username to delete: ");
		String username = scanner.nextLine();

		String query = String.format("DELETE FROM userAccount WHERE username = '%s'", username);

		try (Statement st = connection.createStatement()) {
			int rowsDeleted = st.executeUpdate(query);
			if (rowsDeleted > 0) {
				System.out.println("User account deleted successfully.");
			} else {
				System.out.println("Username not found.");
			}
		} catch (SQLException e) {
			System.out.println("Failed to delete user account.");
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
				System.out.format("UPC: %s, Name: %s, Price: %.2f\n",
						rs.getString("upcBarcode"),
						rs.getString("productName"),
						rs.getDouble("price"));
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
		scanner.nextLine(); // Consume newline

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
		scanner.nextLine(); // Consume newline

		String query = String.format(
				"UPDATE Item SET productName = '%s', price = %.2f WHERE upcBarcode = '%s'",
				productName, price, upcBarcode);

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
				System.out.format("Transaction ID: %d, Customer ID: %d, Timestamp: %s, Payment Type: %s, Total Amount: %.2f\n",
						rs.getInt("transID"),
						rs.getInt("customerID"),
						rs.getTimestamp("timeStamp"),
						rs.getString("paymentType"),
						rs.getDouble("totalAmount"));
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
				"INSERT INTO Transaction (customerID, timeStamp, paymentType, totalAmount) VALUES (%d, NOW(), '%s', %.2f)",
				customerID, paymentType, totalAmount);

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

	public static void changeQuantity(Connection connection, Scanner scanner) {
		System.out.print("Transaction ID: ");
		String transID = scanner.nextLine();
		System.out.print("UPC Barcode of Item: ");
		String upcBarcode = scanner.nextLine();
		System.out.print("New Quantity: ");
		String quantity = scanner.nextLine();

		try (Statement st = connection.createStatement()){
			String query = "UPDATE CartItem SET quantity = " + quantity + " WHERE transID = '" + transID + "' AND upcBarcode = '" + upcBarcode + "'";
			st.executeUpdate(query);
			System.out.println("Quantity updated successfully.");
		} catch (SQLException e) {
			System.out.println("Error updating quantity: " + e.getMessage());
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

		String query = String.format(
				"SELECT CartItem.transID, CartItem.upcBarcode, Item.productName, CartItem.quantity " +
						"FROM CartItem " +
						"JOIN Item ON CartItem.upcBarcode = Item.upcBarcode " +
						"WHERE CartItem.transID = %d",
						transID);

		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			System.out.println("\nTransaction Items:");
			while (rs.next()) {
				System.out.format("Transaction ID: %d, UPC: %s, Product: %s, Quantity: %d\n",
						rs.getInt("transID"),
						rs.getString("upcBarcode"),
						rs.getString("productName"),
						rs.getInt("quantity"));
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
} // End class.
