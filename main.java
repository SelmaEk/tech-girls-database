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

				// Switch-case statement to handle the user's choices
				switch (choice) {
				case 1:
					// Display all customers information (some may not have an account)
					viewCustomers(connection);
					break;
				case 2:
					// Creating a new user account
					createUserAccount(connection, scanner);
					break;
				case 3:
					// Log in to an already existing user account
					loginUserAccount(connection, scanner);
					break;
				case 4:
					// Update account information
					updateUserAccountInfo(connection, scanner);
					break;
				case 5:
					// Reset password without logging in
					changePasswordBeforeLogin(connection, scanner);
					break;
				case 6:
					// Reset password while logged in
					changePasswordAfterLogin(connection, scanner);
					break;
				case 7:
					// Delete an already existing user account
					deleteUserAccount(connection, scanner);
					break;
				case 8:
					// Display all products available
					viewProducts(connection);
					break;
				case 9:
					// Adding a new product
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
					// Delete an exisiting transaction
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
					// Program is exiting and will stop the main loop
					System.out.println("Exiting...");
					running = false;
					break;
				default:
					// This is a default case to handle any invalid input
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
		String query = "SELECT username, name, phoneNumber, email FROM userAccount";
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) { // Executing the query and storing the results

			System.out.println("\nCustomer List:");
			// While loop to go through the results, and print out each customer's details
			while (rs.next()) {
				System.out.format("Username: %d, Name: %s, Phone: %s, Email: %d\n",
						rs.getInt("username"),
						rs.getString("name"),
						rs.getString("phoneNumber"),
						rs.getInt("email"));
			}
		} catch (SQLException e) {
			// Catches and handles any errors that may occur during the query execution
			System.out.println("Failed to fetch customer data.");
			e.printStackTrace();
		}
	}
	
	// Method to create a new user account
	public static void createUserAccount(Connection connection, Scanner scanner) {
		// Prompts user for account information
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

		// SQL query to insert the new user data
		String query = String.format(
				"INSERT INTO userAccount (username, password, name, phoneNumber, email) VALUES ('%s', '%s', '%s', '%s', '%s')",
				username, password, name, phoneNumber, email);

		try (Statement st = connection.createStatement()) {
			// Execute the query to insert the account
			st.executeUpdate(query);
			System.out.println("User account created successfully.");
		} catch (SQLException e) {
			// Handles errors during account creation
			System.out.println("Failed to create user account.");
			e.printStackTrace();
		}
	}

	// Method to log in to an already existing user account
	public static boolean loginUserAccount(Connection connection, Scanner scanner) {
		// Prompts the user for login credentials
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		// SQL query to confirm the login details
		String query = String.format(
				"SELECT * FROM userAccount WHERE username = '%s' AND password = '%s'",
				username, password);

		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			//Check if the matching record exists
			if (rs.next()) {
				System.out.println("Login successful!");
				return true;
			} else {
				System.out.println("Invalid username or password.");
				return false;
			}
		} catch (SQLException e) {
			// Handles any errors during the login
			System.out.println("Failed to log in.");
			e.printStackTrace();
			return false;
		}
	}

	// A method that updates the user account information
	public static void updateUserAccountInfo(Connection connection, Scanner scanner) {
		// Prompts the user for the username to update
		System.out.print("Enter username to update: ");
		String username = scanner.nextLine();

		// Prompts for the new account details
		System.out.print("Enter new full name: ");
		String name = scanner.nextLine();
		System.out.print("Enter new phone number (xxx-xxx-xxxx): ");
		String phoneNumber = scanner.nextLine();
		System.out.print("Enter new email: ");
		String email = scanner.nextLine();

		// SQL query to update the new user account details
		String query = String.format(
				"UPDATE userAccount SET name = '%s', phoneNumber = '%s', email = '%s' WHERE username = '%s'",
				name, phoneNumber, email, username);

		try (Statement st = connection.createStatement()) {
			// Executes the query to update the account
			st.executeUpdate(query);
			System.out.println("User account updated successfully.");
		} catch (SQLException e) {
			// Catches and handles any errors during an account update
			System.out.println("Failed to update user account.");
			e.printStackTrace();
		}
	}

	// Method to change a user's password without having to log in
	public static boolean changePasswordBeforeLogin(Connection connection, Scanner scanner) {
		//Prompts the user to enter their username
		System.out.print("Enter username: ");
		String username = scanner.nextLine();

		// SQL query to check if the username exists in the database
		String query = String.format("SELECT * FROM userAccount WHERE username = '%s'", username);
		
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			// Check if the query returns any results
			if (rs.next()) {
				// Prompt the user to enter a new password
				System.out.print("Enter new password: ");
				String newPassword = scanner.nextLine();

				// SQL query to update the user's new password
				String updateQuery = String.format("UPDATE userAccount SET password = '%s' WHERE username = '%s'", newPassword, username);
				st.executeUpdate(updateQuery);
				System.out.println("Password updated successfully.");
				return true; // Indicates a success in updating the password
			} else {
				// If no matching username is found, it returns false
				System.out.println("Username not found.");
				return false;
			}
		} catch (SQLException e) {
			// Handles the SQL exceptions and provides feedback to user
			System.out.println("Failed to update password.");
			e.printStackTrace();
			return false;
		}
	}
	
	// Method to change a user's password while being logged in
	public static boolean changePasswordAfterLogin(Connection connection, Scanner scanner) {
		// Prompt the user for their username and current password
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		// SQL query to get the current password for validation
		String query = String.format("SELECT password FROM userAccount WHERE username = '%s'", username);

		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			// Checks if the query returns any results
			if (rs.next()) {
				// Prompts the user to enter new password
				System.out.print("Enter new password: ");
				String newPassword = scanner.nextLine();

				// SQL query to update and execute the password in the database
				String updateQuery = String.format("UPDATE userAccount SET password = '%s' WHERE username = '%s'", newPassword, username);
				st.executeUpdate(updateQuery);
				System.out.println("Password updated successfully.");
				return true;
			}
			else {
				// If no matching username is found, return false
				System.out.println("Username not found.");
				return false;
			}

		} catch (SQLException e) {
			// Handles the SQL exceptions and gives feedback to user
			System.out.println("Failed to update password.");
			e.printStackTrace();
			return false;
		}
	}

	// Method to delete user account
	public static void deleteUserAccount(Connection connection, Scanner scanner) {
		// Prompt the user to enter the username of the account
		System.out.print("Enter username to delete: ");
		String username = scanner.nextLine();
		
		// Query to delete the user account
		String query = String.format("DELETE FROM userAccount WHERE username = '%s'", username);

		try (Statement st = connection.createStatement()) {
			// Executes the delete query and checks the number of rows affected
			int rowsDeleted = st.executeUpdate(query);

			// If atleast one row was deleted, confirm success
			if (rowsDeleted > 0) {
				System.out.println("User account deleted successfully.");
			} else {
				// If no matching username is found, notify user
				System.out.println("Username not found.");
			}
		} catch (SQLException e) {
			// Handles SQL exceptions
			System.out.println("Failed to delete user account.");
			e.printStackTrace();
		}
	}

	// PRODUCT MANAGEMENT
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void viewProducts(Connection connection) {
		// SQL query to view all product details
		String query = "SELECT upcBarcode, productName, price FROM Item";
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			System.out.println("\nProduct List:");

			// Loop through the results and show the product records
			while (rs.next()) {
				System.out.format("UPC: %s, Name: %s, Price: %.2f\n",
						rs.getString("upcBarcode"),
						rs.getString("productName"),
						rs.getDouble("price"));
			}
		} catch (SQLException e) {
			// Handle errors during data retrieval
			System.out.println("Failed to fetch product data.");
			e.printStackTrace();
		}
	}
	
	// Method to add a new product to the database
	public static void addProduct(Connection connection, Scanner scanner) {
		// Prompt user for product information
		System.out.print("Enter product UPC: ");
		String upcBarcode = scanner.nextLine();
		System.out.print("Enter product name: ");
		String productName = scanner.nextLine();
		System.out.print("Enter product price: ");
		double price = scanner.nextDouble();
		scanner.nextLine(); // Consume newline

		// SQL query to insert the the new product 
		String query = String.format(
				"INSERT INTO Item (upcBarcode, productName, price) VALUES ('%s', '%s', %.2f)",
				upcBarcode, productName, price);

		try (Statement st = connection.createStatement()) {
			// Executing the query to insert the product
			st.executeUpdate(query);
			System.out.println("Product added successfully.");
		} catch (SQLException e) {
			// Handles errors when adding the new product
			System.out.println("Failed to add product.");
			e.printStackTrace();
		}
	}
	
	// Method to update details of an already existing product
	public static void updateProduct(Connection connection, Scanner scanner) {
		// Prompt the user for the products new details
		System.out.print("Enter product UPC to update: ");
		String upcBarcode = scanner.nextLine();
		System.out.print("Enter new product name: ");
		String productName = scanner.nextLine();
		System.out.print("Enter new product price: ");
		double price = scanner.nextDouble();
		scanner.nextLine(); // Consume newline

		//SQL query to update the product
		String query = String.format(
				"UPDATE Item SET productName = '%s', price = %.2f WHERE upcBarcode = '%s'",
				productName, price, upcBarcode);

		try (Statement st = connection.createStatement()) {
			// Executes the query and checks if any rows were affected
			int rowsAffected = st.executeUpdate(query);
			if (rowsAffected > 0) {
				System.out.println("Product updated successfully.");
			} else {
				System.out.println("No product found with the given UPC.");
			}
		} catch (SQLException e) {
			// Handles errors when updating the product information
			System.out.println("Failed to update product.");
			e.printStackTrace();
		}
	}

	// This method deletes a already existing product from the database
	public static void deleteProduct(Connection connection, Scanner scanner) {
		// Prompts the user to enter the product UPC to delete
		System.out.print("Enter product UPC to delete: ");
		String upcBarcode = scanner.nextLine();

		// SQL query to delete the product
		String query = String.format("DELETE FROM Item WHERE upcBarcode = '%s'", upcBarcode);

		try (Statement st = connection.createStatement()) {
			// Executes the query and checks if any rows were affected to see if it was successful or not
			int rowsAffected = st.executeUpdate(query);
			if (rowsAffected > 0) {
				System.out.println("Product deleted successfully.");
			} else {
				System.out.println("No product found with the given UPC.");
			}
		} catch (SQLException e) {
			// Handles any errors when trying to delete product information
			System.out.println("Failed to delete product.");
			e.printStackTrace();
		}
	}


	// TRANSACTION MANAGEMENT
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Method to view all transactions
	public static void viewTransactions(Connection connection) {
		// SQL query to select all transaction records
		String query = "SELECT transID, customerID, timeStamp, paymentType, totalAmount FROM Transaction";

		// Creates a statement object to execute the query and store results
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			System.out.println("\nTransaction List:");
			// Loops through the results and prints each transaction record
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
			// Handles SQL related errors 
			System.out.println("Failed to fetch transaction data.");
			e.printStackTrace();
		}
	}
	
	// Method to add a new transaction
	public static void addTransaction(Connection connection, Scanner scanner) {
		// Prompts the user for transaction details
		System.out.print("Enter Customer ID: ");
		int customerID = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter Payment Type (e.g., Credit, Debit): ");
		String paymentType = scanner.nextLine();
		System.out.print("Enter Total Amount: ");
		double totalAmount = scanner.nextDouble();
		scanner.nextLine();

		// SQL query to insert the new transaction
		String query = String.format(
				"INSERT INTO Transaction (customerID, timeStamp, paymentType, totalAmount) VALUES (%d, NOW(), '%s', %.2f)",
				customerID, paymentType, totalAmount);

		// Executes the query to insert transaction
		try (Statement st = connection.createStatement()) {
			st.executeUpdate(query);
			System.out.println("Transaction added successfully.");
		} 
		catch (SQLException e) {
			// Catches and handles SQL related errors
			System.out.println("Failed to add transaction.");
			e.printStackTrace();
		}
	}

	// Method to delete a transaction from database
	public static void deleteTransaction(Connection connection, Scanner scanner) {
		// Prompt the user for the transaction ID to delete
		System.out.print("Enter Transaction ID to delete: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		// SQL query to delete the transaction with the given transaction ID
		String query = String.format("DELETE FROM Transaction WHERE transID = %d", transID);

		// Query execution & checks the rows affected to check if query was successful
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
			// Handles errors
			System.out.println("Failed to delete transaction.");
			e.printStackTrace();
		}
	}

	// Method to change quantity of a selected product item
	public static void changeQuantity(Connection connection, Scanner scanner) {
		// Prompts the user for transaction and item info
		System.out.print("Transaction ID: ");
		String transID = scanner.nextLine();
		System.out.print("UPC Barcode of Item: ");
		String upcBarcode = scanner.nextLine();
		System.out.print("New Quantity: ");
		String quantity = scanner.nextLine();

		// SQL query to update and execute quantity of item
		try (Statement st = connection.createStatement()){
			String query = "UPDATE CartItem SET quantity = " + quantity + " WHERE transID = '" + transID + "' AND upcBarcode = '" + upcBarcode + "'";
			st.executeUpdate(query);
			System.out.println("Quantity updated successfully.");
		} catch (SQLException e) {
			// Handles any errors
			System.out.println("Error updating quantity: " + e.getMessage());
		}
	}

	//TRANSACTION ITEMS
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Method to add an item to an already existing transaction
	public static void addItemToTransaction(Connection connection, Scanner scanner) {
		// Prompts the user for the transaction ID and product info
		System.out.print("Enter Transaction ID: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		System.out.print("Enter Product UPC: ");
		String upcBarcode = scanner.nextLine();
		System.out.print("Enter Quantity: ");
		int quantity = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		// SQL query to insert the item into the transaction
		String query = String.format("INSERT INTO CartItem (transID, upcBarcode, quantity) VALUES (%d, '%s', %d)", transID, upcBarcode, quantity);

		// Executes the query
		try (Statement st = connection.createStatement()) {
			st.executeUpdate(query);
			System.out.println("Item added to transaction successfully.");
		} 
		catch (SQLException e) {
			// Catches and handles any errors 
			System.out.println("Failed to add item to transaction.");
			e.printStackTrace();
		}
	}

	// Method to view all items in a specific transaction
	public static void viewTransactionItems(Connection connection, Scanner scanner) {
		// Prompts user for the transaction ID the user wants to view
		System.out.print("Enter Transaction ID to view items: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		// SQL query to fetch all items associated with transaction ID
		String query = String.format(
				"SELECT CartItem.transID, CartItem.upcBarcode, Item.productName, CartItem.quantity " +
						"FROM CartItem " +
						"JOIN Item ON CartItem.upcBarcode = Item.upcBarcode " +
						"WHERE CartItem.transID = %d",
						transID);

		// Executes the query
		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			System.out.println("\nTransaction Items:");
			// While loop to go through the results and display each item
			while (rs.next()) {
				System.out.format("Transaction ID: %d, UPC: %s, Product: %s, Quantity: %d\n",
						rs.getInt("transID"),
						rs.getString("upcBarcode"),
						rs.getString("productName"),
						rs.getInt("quantity"));
			}
		} catch (SQLException e) {
			// Handles SQL related errors 
			System.out.println("Failed to fetch transaction items.");
			e.printStackTrace();
		}
	}

	// Method to remove item from transaction
	public static void removeItemFromTransaction(Connection connection, Scanner scanner) {
		// Prompts the user for the transaction ID and product info
		System.out.print("Enter Transaction ID: ");
		int transID = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		System.out.print("Enter Product UPC to remove: ");
		String upcBarcode = scanner.nextLine();

		// SQL query to delete the item from the transaction
		String query = String.format("DELETE FROM CartItem WHERE transID = %d AND upcBarcode = '%s'",transID, upcBarcode);

		// Executes query and checks rows affected to see if the execution was successful
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
			// Handles any errors that may occur
			System.out.println("Failed to remove item from transaction.");
			e.printStackTrace();
		}
	}
} // End class.
