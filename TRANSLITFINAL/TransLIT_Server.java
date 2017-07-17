import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

public class TransLIT_Server extends javax.swing.JFrame 
{
	//Two arraylists, one for the users that are connecting to the server, the other for
	ArrayList clientOutputStreams;
    ArrayList<String> onlineUsers;

	public class ClientHandler implements Runnable	
	{
		//Defining the Server/Java Network information 
		BufferedReader in;
		Socket skt;
        PrintWriter clientSend;
        
        //Configuring the reading and writing that the server will eventually make with the Client
		public ClientHandler(Socket clientSocket, PrintWriter user) 
		{
			clientSend = user;
			try 
			{
				skt = clientSocket;
				InputStreamReader isReader = new InputStreamReader(skt.getInputStream());
				in = new BufferedReader(isReader);
			}
			catch (Exception ex) //If there is an error initializing the above commands
			{
				consoleWindow.append("Error beginning StreamReader. \n");
			} 
		}

		public void run() 
		{
            String message; //The message is the data that is coming from the clients
            String[] data; //The array list that is used to split different words in the sentence that a user sends
            
            //The types of data that can be sent from the client
            String connect = "Connect";
            String disconnect = "Disconnect";
            String chat = "Chat";

			try 
			{
				
				while ((message = in.readLine()) != null) //While the message being transferred does not have nothing (a.k.a. it is active)
				{
					consoleWindow.append("Received: " + message + "\n"); //Shows the message in the server text window
					data = message.split(":"); //Split s the message contents to find out what kind of data is being received

					  
					//If the data is a connection message
                    if (data[2].equals(connect)) 
                    {
                    	contactClients((data[0] + ":" + data[1] + ":" + chat)); //Tells all the clients that there has been a connection
                        addPeople(data[0]); //Adds user to the user list
                        usercheck(data[0]); //method to check add "online" users to a database table (onlinelist)
					} 
                    
                    //If the data is a disconnect message
                    else if (data[2].equals(disconnect)) 
                    {
                    	contactClients((data[0] + ":has disconnected." + ":" + chat)); //Tell all the clients that someone has left the server
                        removePeople(data[0]); //Removes the user from the user list
                        deletepeople(data[0]); // "delete" online users from the database of online users (onlinelist)
					} 
                    
                    //If the data is just a regular chat message
                    else if (data[2].equals(chat)) 
                    {
                    	contactClients(message); //Show clients the message
					} 
                    
                    //If none of the above
                    else 
                    {
                    	//Tells the server that it can't find out what the message is for
                    	consoleWindow.append("Cannot understand the data received. \n");
                    }
				} 
			}
			catch (Exception ex) //If there is an error (someone crashes or leaves abruptly)
			{
				//Server notifies itself that a person has lost connection to the server
				consoleWindow.append("Lost a connection. \n");
                ex.printStackTrace();
                clientOutputStreams.remove(clientSend);
			} 
		} 
	} 
	
    public void exitApplication() //exit the application
    {
		try 
		{
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
			Statement clearStatement = connect.createStatement();
    	    String clearQuery = "SELECT * FROM `onlinelist`";
            ResultSet clearResult = clearStatement.executeQuery(clearQuery);
            
            String sql = "TRUNCATE onlinelist";
		    // Execute deletion
		    clearStatement.executeUpdate(sql);
		    // Use DELETE
		    sql = "DELETE FROM onlinelist";
		    // Execute deletion
		    clearStatement.executeUpdate(sql);
            
			sql = "INSERT INTO `onlinelist` (Username)" + "VALUES (?)";
			
			//Prepared statement because it is set variables that we want to add, not just a search method
			//Sending the server a random username because we can't leave the table empty
			//if the table is left empty the client parameters assume that everyone is a duplicate and won't let them on
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, "ewifuhqujdbj875I3RHEFUAHD12837123jjg3$ahauhascOWUIWKDJ"); //random name that no one should be able to register with
			preparedStatement.executeUpdate();
            
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Server will close after 'OK' has been pressed. \n \n If you wish to run the server again, you will have to run the file again.");
	     System.exit(0); //Closing the connection this way because we cannot call the socket variables from this method (since they are private). 
	   
    }
    
	//Call the GUI
	public TransLIT_Server() 
    {
        initialize();
    }
                         
    private void initialize() 
    {

    	//Variable Declaration
        consoleWindowScroll = new javax.swing.JScrollPane();
        consoleWindow = new javax.swing.JTextArea();
        btnStart = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();

        //Set to exit on close if needed, since it is a separate program that needs to run by i
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowListener exitApp = new WindowAdapter() 
        {
        	@Override
            public void windowClosing(WindowEvent e) 
            {
        		int exitattempt = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the Application?",  "Are You Sure?", JOptionPane.YES_NO_OPTION);

        		if(exitattempt == JOptionPane.YES_OPTION)//If yes is pressed
        		{
        		    exitApplication();//Call the "exitApplication" Method

        		} 
        		else if (exitattempt == JOptionPane.NO_OPTION) //If no is pressed
        		{
        		    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //do nothing
        		}  
            }
        };
        addWindowListener(exitApp);
        
        //Set the title of the JFrame
        setTitle("TransLIT - The Translating Chat Application | Server");

        
        //Text Area Code
        consoleWindow.setColumns(20);
        consoleWindow.setEditable(false);
        consoleWindow.setLineWrap(true);
        consoleWindow.setRows(5);
        consoleWindowScroll.setViewportView(consoleWindow);
        
        
        //Button Code 
        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                startButtonActionPerformed(evt);
            }
        });

        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //Horizontal Layout (Auto - Generated)
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnStop, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                    .addComponent(consoleWindowScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        
        //Vertical Layout (Auto - Generated)
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(consoleWindowScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStart)
                    .addComponent(btnStop))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack(); //Without this, the GUI starts super tiny, and you would manually have to resize it. This forces the GUI to start at it's proper dimensions.
        setLocationRelativeTo(null); //To start the GUI in the center of the screen
    }                       

    //If the server user presses the start button
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {                                            
        Thread starter = new Thread(new startServer());
        starter.start();
        consoleWindow.append("Server started. \n");
    }                                           

    //If the server user presses the stop button
    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {                                           
        contactClients("Server: Stopping. All users will be disconnected.\n:Chat");
        consoleWindow.append("Server stopping . . . \n");
        int exitattempt = JOptionPane.showConfirmDialog(null, "Are you sure you want to shutdown the server?",  "Are You Sure?", JOptionPane.YES_NO_OPTION);

		if(exitattempt == JOptionPane.YES_OPTION)//If yes is pressed
		{
			try 
			{
				Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
				Statement clearStatement = connect.createStatement();
	    	    String clearQuery = "SELECT * FROM `onlinelist`";
	            ResultSet clearResult = clearStatement.executeQuery(clearQuery);
	            
	            String sql = "TRUNCATE onlinelist";
			    // Execute deletion
			    clearStatement.executeUpdate(sql);
			    // Use DELETE
			    sql = "DELETE FROM onlinelist";
			    // Execute deletion
			    clearStatement.executeUpdate(sql);
	            
    			sql = "INSERT INTO `onlinelist` (Username)" + "VALUES (?)";
    			
    			//Prepared statement because it is set variables that we want to add, not just a search method
    			//Sending the server a random username because we can't leave the table empty
    			//if the table is left empty the client parameters assume that everyone is a duplicate and won't let them on
    			PreparedStatement preparedStatement = connect.prepareStatement(sql);
    			preparedStatement.setString(1, "ewifuhqujdbj875I3RHEFUAHD12837123jjg3$ahauhascOWUIWKDJ"); //random name that no one should be able to register with
    			preparedStatement.executeUpdate();
	            
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
    	   
			 JOptionPane.showMessageDialog(null, "Server will close after 'OK' has been pressed. \n \n If you wish to run the server again, you will have to run the file again.");
		     System.exit(0); //Closing the connection this way because we cannot call the socket variables from this method (since they are private). 

		} 
		else if (exitattempt == JOptionPane.NO_OPTION) //If no is pressed
		{
		    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	        consoleWindow.append("Stop Request Cancelled. Server is still running \n");
		}  
       
    }                                          

	public static void main(String args[]) //Main Method
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new TransLIT_Server().setVisible(true); //Display the Creation!
            }
        });
    }

    //Class Start server, which handles the server while it is running
    public class startServer implements Runnable 
    {
        public void run() 
        {
        	//Defining what the arraylists actually are 
        	clientOutputStreams = new ArrayList();
        	onlineUsers = new ArrayList();  
        	try 
        	{
        		ServerSocket serverSocket = new ServerSocket(1234); //Set up the server socket
        		while (true) 
        		{
        			//enable client connections, prepare to recieve and send things
        			Socket clientSocket = serverSocket.accept();
        			PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        			clientOutputStreams.add(out);

        			Thread listener = new Thread(new ClientHandler(clientSocket, out));
        			listener.start();
        			
        			//notify the server when someone has made a connection with the server
        			consoleWindow.append("User has made a connection. \n");
        		}
        	} 
        	catch (Exception ex) //If the server is already on or some other error
        	{
        		consoleWindow.append("Error making a connection. \n");
        	}
        }
    }

    //Add a connected account to the servers onlineUsers list and update the people connected 
    
    public void usercheck (String data) { //method to check add "online" users to a database table (onlinelist)
    
    	try{
    		consoleWindow.append("USER HAS BEEN ADDED TO DATABASE \n");
    		
    		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
    	    Statement statement = connect.createStatement();
    	    String query = "SELECT * FROM `loginvariables`"; //select from the database (login variables) of registered users.
            ResultSet result = statement.executeQuery(query);
            
			
    		if (result.next()) {
    			String sql = "INSERT INTO `onlinelist` (Username)" + "VALUES (?)"; //statement to add "online" users to a database
    			
    			//Prepared statement because it is set variables that we want to add, not just a search method
    			PreparedStatement preparedStatement = connect.prepareStatement(sql);
    			preparedStatement.setString(1, data);
    			preparedStatement.executeUpdate();
    		}
    		//connect.close();
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
     	
    	
    }
    
    public void deletepeople (String data) { //"delete" online users from the database of online users (online users)
    	try{
    		
    		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
    	    Statement statement = connect.createStatement();
    	    String query = "SELECT * FROM `onlinelist`"; //selecting from the table "online users" 
            ResultSet result = statement.executeQuery(query);
            
    		while (result.next()) {
    		
    				String input = "DELETE FROM onlinelist WHERE Username = ?"; //SQL to remove users once they disconnect or close the tabs. 
    				PreparedStatement preparedStatement = connect.prepareStatement(input);
    				preparedStatement.setString(1, data);
    				preparedStatement.executeUpdate();
    				consoleWindow.append("USER HAS BEEN SUCCESFULLY REMOVED FROM THE DATABASE \n");
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} 	
    }
    
    public void addPeople (String data) 
    {
      	String message;
    	String add = ": :Connect";
    	String done = "Server: :Done";
    	String name = data;
 
        consoleWindow.append( name + " added to the UserList \n"); //Server tells itself that a user has been added to the online user list
        onlineUsers.add(name);
        String[] uList = new String[(onlineUsers.size())]; //Gets size of the onlineUsers array at the beginning of this java program
        onlineUsers.toArray(uList);

        for (String people:uList)  //For every person in the list
        {
        	message = (people + add); //Overwrite the message for a moment
            contactClients(message); //Tell all the connected clients
        }
        contactClients(done);


	}

    //Remove a connected accounts name off of a list
	public void removePeople (String data) 
	{
		String message;
		String add = ": :Connect";
		String done = "Server: :Done";
		String name = data;
		onlineUsers.remove(name);
		String[] uList = new String[(onlineUsers.size())];
		onlineUsers.toArray(uList);
		for (String people:uList) 
		{
			message = (people + add); //Message overwritten for a brief moment
			contactClients(message); //Tells the rest of the clients
        }
        contactClients(done);
	}

	//Contact all the clients connected to the server method
    public void contactClients(String message) 
    {
    	//Iterator is needed to cycle through all the items in the client"OutputStreams arraylist
    	//It will do a specific thing for each person or item (in this case people)
        Iterator it = clientOutputStreams.iterator();
        
        //the message is sent to this method and the server reads it and sends it on it's way for everyone to see
        while (it.hasNext()) 
        {
        	try 
        	{
        		PrintWriter out = (PrintWriter) it.next();
        		//Sends message out to each individual client
        		out.println(message);
        		//Server telling itself that it is sending this information out
        		consoleWindow.append("Sending: " + message + "\n");
                out.flush();//Flushes the Stream
                //Sets the position of the message to the bottom of the scroll so you can see what is going on
                consoleWindow.setCaretPosition(consoleWindow.getDocument().getLength());
        	}
        	catch (Exception ex) //If the server cannot send information to the clients
        	{
        		consoleWindow.append("Error Contacting All Clients. \n");
        	} 
        }
     }
     
    
 
    //Declarign variables, since other methods need to use them, not just the GUI creation method
    //Do not modify pls
    private javax.swing.JScrollPane consoleWindowScroll;
    private javax.swing.JTextArea consoleWindow;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
}