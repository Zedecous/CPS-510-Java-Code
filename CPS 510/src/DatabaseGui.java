import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;



public class DatabaseGui extends JFrame {
	
	private JPanel contentPane;
	static Connection conn1 = null;
	/**
	 * Launch the application.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		

            // registers Oracle JDBC driver - though this is no longer required
            // since JDBC 4.0, but added here for backward compatibility
            Class.forName("oracle.jdbc.OracleDriver");


         //   String dbURL1 = "jdbc:oracle:thin:username/password@oracle.scs.ryerson.ca:1521:orcl";  // that is school Oracle database and you can only use it in the labs


             String dbURL1 = "jdbc:oracle:thin:system/20062841@localhost:1521:xe";
			/* This XE or local database that you installed on your laptop. 1521 is the default port for database, change according to what you used during installation.
			xe is the sid, change according to what you setup during installation. */

			conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected with connection #1");
            }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatabaseGui frame = new DatabaseGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}

	/**
	 * Create the frame.
	 */
	public DatabaseGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnCreateDatabase = new JButton("Create Database");
		btnCreateDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createDatabase();
			}
		});
		btnCreateDatabase.setBounds(10, 11, 115, 50);
		contentPane.add(btnCreateDatabase);
		
		JButton btnBuildDatabase = new JButton("Populate Database");
		btnBuildDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateDatabase();
			}
		});
		btnBuildDatabase.setBounds(146, 11, 115, 50);
		contentPane.add(btnBuildDatabase);
		
		JButton btnPopulateDatabase = new JButton("Query Database");
		btnPopulateDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				query();
			}
		});
		btnPopulateDatabase.setBounds(287, 11, 134, 50);
		contentPane.add(btnPopulateDatabase);
		
		JButton btnDropDatabase = new JButton("Drop Database");
		btnDropDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dropTables();
			}
		});
		btnDropDatabase.setBounds(454, 11, 115, 50);
		contentPane.add(btnDropDatabase);
		
		JTextPane OutputtextPane = new JTextPane();
		OutputtextPane.setEditable(false);
		OutputtextPane.setBounds(35, 95, 530, 224);
		contentPane.add(OutputtextPane);
	}
	public void createDatabase() {
		String query = "CREATE TABLE customer (\r\n" + 
				"    userName VARCHAR2(15) PRIMARY KEY,\r\n" + 
				"    firstName VARCHAR2(25) NOT NULL,\r\n" + 
				"    lastName VARCHAR2(25) NOT NULL,\r\n" + 
				"    address VARCHAR2(40) NOT NULL,\r\n" + 
				"    accountPassword VARCHAR2(15) NOT NULL,\r\n" + 
				"    billingInfo VARCHAR2(12)\r\n" + 
				"    )\r\n" + 
				"\r\n" + 
				"CREATE TABLE shoes (\r\n" + 
				"    shoeNumber NUMBER PRIMARY KEY,\r\n" + 
				"    shoeStyle VARCHAR2(15) NOT NULL,\r\n" + 
				"    gender VARCHAR2(10),\r\n" + 
				"    ageGroup VARCHAR2(6) NOT NULL,\r\n" + 
				"    colour VARCHAR2(10) NOT NULL,\r\n" + 
				"    shoeSize NUMBER NOT NULL,\r\n" + 
				"    price DECIMAL(6,2) NOT NULL,\r\n" + 
				"    stock INTEGER NOT NULL,\r\n" + 
				"    used INTEGER NOT NULL\r\n" + 
				"    )\r\n" + 
				"    \r\n" + 
				"CREATE TABLE cart (\r\n" + 
				"    cart_entry NUMBER PRIMARY KEY,\r\n" + 
				"    shoeNumber NUMBER REFERENCES shoes(shoeNumber) ON DELETE CASCADE,\r\n" + 
				"    userName VARCHAR2(15) REFERENCES customer(userName) on DELETE CASCADE,\r\n" + 
				"    numberOfItems NUMBER NOT NULL,\r\n" + 
				"    totalPrice NUMBER NOT NULL\r\n" + 
				"    )\r\n" + 
				"    \r\n" + 
				"\r\n" + 
				"CREATE TABLE shoppingHistory (\r\n" + 
				"    shopping_entry NUMBER PRIMARY KEY,\r\n" + 
				"    shoeNumber NUMBER REFERENCES shoes(shoeNumber) ON DELETE CASCADE,\r\n" + 
				"    userName VARCHAR2(15) REFERENCES customer(userName) ON DELETE CASCADE,\r\n" + 
				"    numberOfItems NUMBER NOT NULL,\r\n" + 
				"    dateOfPurchase DATE NOT NULL\r\n" + 
				"    )\r\n" + 
				"\r\n" + 
				"CREATE TABLE wishList (\r\n" + 
				"    wishList_entry NUMBER PRIMARY KEY,\r\n" + 
				"    userName VARCHAR2(15) REFERENCES customer(userName) ON DELETE CASCADE,\r\n" + 
				"    shoeNumber NUMBER REFERENCES shoes(shoeNumber) ON DELETE CASCADE \r\n" + 
				")\r\n" + 
				"CREATE TABLE purchases (\r\n" + 
				"    userName VARCHAR2(15) REFERENCES customer(userName) ON DELETE CASCADE,\r\n" + 
				"    shoeNumber NUMBER REFERENCES shoes(shoeNumber) ON DELETE CASCADE,\r\n" + 
				"    dateOfPurchase DATE NOT NULL\r\n" + 
				"    )\r\n" + 
				"    \r\n" + 
				"CREATE TABLE pays_for (\r\n" + 
				"    userName VARCHAR2(15) REFERENCES customer(userName) ON DELETE CASCADE,\r\n" + 
				"    cart_entry NUMBER REFERENCES cart(cart_entry) ON DELETE CASCADE\r\n" + 
				"    )\r\n" + 
				"    \r\n" + 
				"CREATE TABLE adds_to (\r\n" + 
				"    userName VARCHAR2(15) REFERENCES customer(userName) ON DELETE CASCADE,\r\n" + 
				"    shoeNumber NUMBER REFERENCES shoes(shoeNumber) ON DELETE CASCADE\r\n" + 
				");";
		try (Statement stmt = conn1.createStatement()) {

			ResultSet rs = stmt.executeQuery(query);

			//If everything was entered correctly, this loop should print each row of data in your TESTJDBC table.
			// And you should see the results as follows:
			// Connected with connection #1
			// ALIS, 67
			// BOB, 345

			while (rs.next()) {
				System.out.println(rs);
			}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
	}
	public void populateDatabase() {
		String query = "INSERT INTO customer (username, firstName, lastName, address, accountPassword, billingInfo) VALUES ('Syd','Sydney','Watenya','200 Church Street','abcd','123456789090' )\r\n" + 
				"INSERT INTO customer (username, firstName, lastName, address, accountPassword, billingInfo) VALUES ('Osm','Osman','Ali','100 Gerrard Street','potato','123456879090' )\r\n" + 
				"INSERT INTO customer (username, firstName, lastName, address, accountPassword, billingInfo) VALUES ('Gdw','Godwin','Watenya','200 Church Street','dcba','123456789090' )\r\n" + 
				"INSERT INTO customer (username, firstName, lastName, address, accountPassword, billingInfo) VALUES ('Swt','Swathi','Badri','164 Church Street','tomato','987698761234' )\r\n" + 
				"INSERT INTO customer (username, firstName, lastName, address, accountPassword, billingInfo) VALUES ('Bob','Bob','Smith','1 Main Avenue','bob','121234345656' )\r\n" + 
				"\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (1230,'Sport','Male','13+','Blue',13 ,59.99,13,0)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (1229,'Sport','Male','13+','Blue',12,59.99,8,0)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (1227,'Sport','Male','13+','Blue',10.5,59.99,1,0)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (1231,'Sport','Female','13+','Pink',7,64.99,15,3)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (957,'Dress','Male','18+','Black',9.5,120.59,7,0)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (820,'Casual','Male','16+','White',10,39.99,13,1)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (821,'Casual','Male','16+','White',10.5,39.99,13,1)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (1334,'Heels','Female','13+','Red',6,59.99,5,0)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (1335,'Heels','Female','13+','Red',6.5,59.99,8,0)\r\n" + 
				"INSERT INTO shoes (shoeNumber, shoeStyle, gender, ageGroup, colour, shoeSize, price, stock, used) VALUES (1336,'Heels','Female','13+','Red',7,59.99,2,0)\r\n" + 
				"\r\n" + 
				"INSERT INTO shoppingHistory (shopping_entry, shoeNumber, userName, numberOfItems, dateOfPurchase) VALUES (1, 820, 'Syd',3,'2019-09-24')\r\n" + 
				"INSERT INTO shoppingHistory (shopping_entry, shoeNumber, userName, numberOfItems, dateOfPurchase) VALUES (2, 1229, 'Syd',2,'2018-12-26')\r\n" + 
				"INSERT INTO shoppingHistory (shopping_entry, shoeNumber, userName, numberOfItems, dateOfPurchase) VALUES (3, 1334, 'Swt',1,'2019-05-20')\r\n" + 
				"INSERT INTO shoppingHistory (shopping_entry, shoeNumber, userName, numberOfItems, dateOfPurchase) VALUES (4, 957, 'Osm',1,'2019-10-01')\r\n" + 
				"\r\n" + 
				"INSERT INTO pays_for (userName, cart_entry) VALUES ('Osm', 3)\r\n" + 
				"INSERT INTO pays_for (userName, cart_entry) VALUES ('Syd', 2)\r\n" + 
				"\r\n" + 
				"INSERT INTO adds_to (userName, shoeNumber) VALUES ('Gdw',957)\r\n" + 
				"INSERT INTO adds_to (userName, shoeNumber) VALUES ('Gdw',1229)\r\n" + 
				"INSERT INTO adds_to (userName, shoeNumber) VALUES ('Syd',957)\r\n" + 
				"INSERT INTO adds_to (userName, shoeNumber) VALUES ('Swt',1334)\r\n" + 
				"INSERT INTO adds_to (userName, shoeNumber) VALUES ('Osm',820)\r\n" + 
				"\r\n" + 
				"INSERT INTO wishList (wishList_entry, userName, shoeNumber) VALUES (1, 'Gdw',957)\r\n" + 
				"INSERT INTO wishList (wishList_entry, userName, shoeNumber) VALUES (2, 'Gdw',1229)\r\n" + 
				"INSERT INTO wishList (wishList_entry, userName, shoeNumber) VALUES (3, 'Syd',957)\r\n" + 
				"INSERT INTO wishList (wishList_entry, userName, shoeNumber) VALUES (4, 'Swt',1334)\r\n" + 
				"INSERT INTO wishList (wishList_entry, userName, shoeNumber) VALUES (5, 'Osm',820)\r\n" + 
				"\r\n" + 
				"INSERT INTO cart (cart_entry, shoeNumber, userName,numberOfItems,totalPrice) VALUES (1, 820,'Gdw',1,39.99)\r\n" + 
				"INSERT INTO cart (cart_entry, shoeNumber, userName,numberOfItems,totalPrice) VALUES (2, 957,'Syd',2,241.19)\r\n" + 
				"INSERT INTO cart (cart_entry, shoeNumber, userName,numberOfItems,totalPrice) VALUES (3, 820,'Osm',1,39.99)\r\n" + 
				"\r\n" + 
				"INSERT INTO purchases(userName,shoeNumber,dateOfPurchase) VALUES ('Gdw',820,'2019-10-02')\r\n" + 
				"INSERT INTO purchases(userName,shoeNumber,dateOfPurchase) VALUES ('Syd',957,'2019-10-03')\r\n" + 
				"INSERT INTO purchases(userName,shoeNumber,dateOfPurchase) VALUES ('Osm',1229,'2019-10-01')";
		try (Statement stmt = conn1.createStatement()) {

			ResultSet rs = stmt.executeQuery(query);

			//If everything was entered correctly, this loop should print each row of data in your TESTJDBC table.
			// And you should see the results as follows:
			// Connected with connection #1
			// ALIS, 67
			// BOB, 345

			while (rs.next()) {
				System.out.println(rs);
			}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
	}
	public void query() {
		String query = "SELECT firstName,lastName FROM customer WHERE lastName = 'Watenya'\r\n" + 
				"SELECT * FROM customer WHERE address LIKE '%Church%'\r\n" + 
				"\r\n" + 
				"SELECT DISTINCT shoeStyle FROM shoes\r\n" + 
				"SELECT DISTINCT shoeNumber FROM shoes\r\n" + 
				"\r\n" + 
				"SELECT username FROM customer WHERE billinginfo='123456789090' AND firstname='Sydney'\r\n" + 
				"\r\n" + 
				"SELECT Min(numberOfItems) AS MinNumberOfItems FROM cart\r\n" + 
				"SELECT COUNT(userName) FROM wishList\r\n" + 
				"\r\n" + 
				"SELECT * from purchases WHERE dateOfPurchase <= '2019-10-02'\r\n" + 
				"\r\n" + 
				"SELECT customer.firstName,cart.userName,shoeNumber FROM customer,cart WHERE numberOfItems = 1 AND cart.userName = customer.userName\r\n" + 
				"SELECT purchases.shoeNumber, customer.firstName FROM purchases, shoes, customer WHERE dateOfPurchase > '2019-09-10' AND purchases.shoeNumber = shoes.shoeNumber AND purchases.userName = customer.userName\r\n" + 
				"SELECT COUNT(userName),shoeNumber FROM wishList GROUP BY shoeNumber";
		try (Statement stmt = conn1.createStatement()) {

			ResultSet rs = stmt.executeQuery(query);

			//If everything was entered correctly, this loop should print each row of data in your TESTJDBC table.
			// And you should see the results as follows:
			// Connected with connection #1
			// ALIS, 67
			// BOB, 345

			while (rs.next()) {
				System.out.println(rs);
			}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
	}
	public void dropTables()
	{
		String query = "DROP TABLE cart;\r\n" + 
				"DROP TABLE shoppingHistory;\r\n" + 
				"DROP TABLE wishList;\r\n" + 
				"DROP TABLE purchases;\r\n" + 
				"DROP TABLE pays_for;\r\n" + 
				"DROP TABLE adds_to;\r\n" + 
				"DROP TABLE customer;\r\n" + 
				"DROP TABLE shoes;";
		try (Statement stmt = conn1.createStatement()) {

			ResultSet rs = stmt.executeQuery(query);

			//If everything was entered correctly, this loop should print each row of data in your TESTJDBC table.
			// And you should see the results as follows:
			// Connected with connection #1
			// ALIS, 67
			// BOB, 345

			while (rs.next()) {
				System.out.println(rs);
			}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
	}
	
}
