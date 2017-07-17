import java.net.*;
import java.sql.*;
import java.util.logging.*;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.*;



public class TransLIT_Client extends javax.swing.JFrame 
{
	public static void main(String args[]) //Main Method
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() //When the program launches, the client starts in a window
            {
                new TransLIT_Client().setVisible(true);//To display the creation!
            }
        });
    }
	
	//Declaring Variables and Network connections
    String username;
    String password;
    String serverHost = "localhost";
    int Port = 1234;
    Socket skt;
    
    //Read and Write Functions, needed for sending and receiving messages within the client and server
    BufferedReader in;
    PrintWriter out;
    
    //Boolean code for networking purpose, helps alleviate errors with communication to the server and client 
    //Starts false because the user is not connected to the server
    Boolean isConnected = false;
    
    //Array List for the online users area of the text.
    ArrayList<String> userList = new ArrayList<String>();
    
    //Calling the GUI code
    public TransLIT_Client() 
    {
        initialize();
    }
    
    public boolean runOnce (boolean check) {

		return check;
    	
    }

  //Another class, incomingItems, which handles all the data that will be communicated by the people and the server
    public class incomingItems implements Runnable
    {

        public void run() 
        {
            String[] data; //Data is the people and the messages that are communicated between server and clients
            String stream;
            
            //Different types of data that can be received from the server. 
            //We want to separate and sort the different types of data, so that the server can easily tell us if someone is connecting, talking, or disconnecting
            String done = "Done";
            String connect = "Connect";
            String disconnect = "Disconnect";
            String chat = "Chat";
            Boolean test = runOnce(true);
                   
            try 
            {
                while ((stream = in.readLine()) != null && runOnce(true)) //While there is activity on the server
                {
                	data = stream.split(":");  //Take the input/data and split it (based off of the type of data that's going to the server)
                	
                	if (data[2].equals(chat) ) //If what the user inputs is chat, it takes the users name, and separates it with the data (like a chat message) 
                	{
                	
                	// Translation Code. 
                		
                		try //If there is something in the chat box, try sending it to the server, for it to be posted to the chat
                		{        	
                			//Sends the statement to the server, with who said it, and where they said it        	
                			String code = receiveListValueChanged(null); //language code for translation from the list
                			String trimmed =  data[1].trim(); //number of words
                			String website = null; //parsing URL String
      
                			int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length; //number of words inputed
                			String place [] = new String [words]; //placing the words in an Array
                			// conditions for URL Parsing is different for one word and more than one word, so 2 if statements
                			//to accommodate for both situations: 
                			
                			if (code.equals("")) {
                				code = "en";
                			}
                			
                			if (words > 1) { 
                				place = data[1].split("\\s+"); //split every word via a space 
                				String last = place[words - 1]; //last word of the array

                				for (int j = 0; j < place.length; j++) {
                					if (j < place.length - 1) {
                						place[j] = place[j] + "%20"; //url requires a "20%" after every word except the last one for more than one word
                					}
                					if (j == place.length - 1) {
                						place[j] = last; //leave the last word without the "20%" as it is not necessary for URL parsing
                					}

                				}

                				StringBuilder listString = new StringBuilder();
                				for (String s : place)
                					listString.append(s); //put the array back one string. "apply to the end"
                				website="http://transltr.org/api/translate?text=" + listString + "&to=" + code + "&from=en";
                			}

                			if (words == 1) { //if there's only one word, the "20%"is not needed in the URL after every word. 
                				// reading from a URL
                				website="http://transltr.org/api/translate?text=" + data[1] + "&to=" + code + "&from=en";
                			}
                			
                			//Part 2 of the code, basically fixing the formatting of the text
                			BufferedReader infile = new BufferedReader (new InputStreamReader((new URL(website).openConnection()).getInputStream()));
                			String line;
                			line = infile.readLine();   // Scanner uses input.nextLine(); Files use readLine() because you read it
                			
                			while ( line != null){  //check that the line is not at the end before trying to read the next line.
                				String data1 [] = line.split(":"); //Store contents of the file into memory as an ARRAY
                				String wordOne = null;
                				
                				for (int i = data1.length - 1 ; i < data1.length; i++){
                					wordOne = data1[i]; //placing the last part of the array into the string
                				}

                				String dataTwo [] = wordOne.split("}"); //split after "}"
                				String message = null; 

                				for (int i = dataTwo.length - 1; i < dataTwo.length; i++){
                					message = dataTwo[i].replace("\"", " ");
                					out.flush(); //Flushes the stream
                				}
                				
                				chatWindow.append(data[0] + ": " + message + "\n");
                				line=infile.readLine(); 
                			}
                			
                			infile.close();
                		} 
                        catch (Exception ex) //If the server cannot receive the message, this statement enters the chat.
                        {
                            chatWindow.append("Welcome to the server!  \n");
                        }
                        
                		chatWindow.setCaretPosition(chatWindow.getDocument().getLength());        
                    } 
                	else if (data[2].equals(connect)) //If it is a connect method, it adds it to the userArray so that their name can go in the online list, as well as notify other clients that someone has connected.
                    {
                        chatWindow.removeAll();//remove everything from the chat window
                        addPeople(data[0]);//takes whatever the persons first item is (the username) and adds it to the userarray in the addPeople method below
                     
                    } 
                    else if (data[2].equals(disconnect)) //If the data is a disconnect message, send it to the removePeople method below, and notify clients that the person has disconnected
                    {
                        addPeople(data[0]);
                    } 
                    else if (data[2].equals(done))//Finally, if the user and data is finished, do all the user listing below
                    {
                        onlineUsers.setText("");//clearing the user List for now to check to see if the users have updated
                        displayPeople();//display the list (if it is updating or restarting, etc.)
                        userList.clear();//Clear the userList when finished
                    } 
                }
                
           }
           catch(Exception ex) 
           {
        	 //Couldn't think of an error message or anything here, and everything seemed to be fine, but we need the catch for the try statement
           }
        }
    }

    public void readClient() //Method for reading the chat, referenced below in the connect action
    {
         Thread incomingItems= new Thread(new incomingItems()); 
         incomingItems.start(); //Start the incomingItems class
    }

    public void addPeople(String data) //if a person connects, the users name will be added to the array below to be posted in the online users window
    {
         userList.add(data);
     }

    public void removePeople(String data)  //If a person has disconnected, notify people in the chat
    {
         chatWindow.append(data + " has disconnected.\n");
    }

    public void displayPeople() //Display users in the online users area
    {
         String[] uList = new String[(userList.size())];//Get size of array after users are added to it
         userList.toArray(uList);//add names of people to the array
         for (String people:uList) //for every person in the array, do this
         {
             onlineUsers.append(people + "\n"); //Display users in the onlineUsers window of the GUI
         }
     }

    //Disconnect Method for the server, referenced down in the disconnect button action
    public void serverDisconnect() 
    {
    	String disSignal = (userNameInput.getText() + ": :Disconnect"); 
        try
        {
            out.println(disSignal); // Sends server the disconnect signal.
            out.flush(); // Flushes the stream
        } 
        catch (Exception e) //if it cannot send out the disconnect message
        {
            chatWindow.append("Could not send Disconnect message.\n");
        }
    }

    //Disconnect method, referenced down below in the disconnect button action
    public void Disconnected() 
    {
        try 
        {
        	chatWindow.setText(null);
        	chatWindow.append("You are Disconnected from the Server.\n");
            skt.close(); //Closes the connection to the socket
        } 
        catch(Exception ex) //If there is an error disconnecting from the server
        {
        	chatWindow.append("Failed to disconnect from the Server. \n");
        }
        isConnected = false;//Sets this back to false, as it would cause errors if it were still true while the people are still "connected" (when they really aren't) 
        
        //Enables the user to enter the same, or new credentials
        userNameInput.setEditable(true);
        passWordInput.setEditable(true);
        onlineUsers.setText("");
    }
    
    //Method that plays if the user presses the red "x" button (close button) on the jButton
    public void exitApplication()
    {
    	if(isConnected == true)
    	{
    		try 
    		{
    			//Separate statements ripped from the Disconnect() and serverDisconnect Methods as calling them didn't work
    			String disSignal = (userNameInput.getText() + ": :Disconnect"); 
    			out.println(disSignal); // Sends server the disconnect signal.
                out.flush(); // Flushes the stream
    			chatWindow.setText(null);
    			onlineUsers.setText(null); //Sets users "Users Online" list to empty
            	chatWindow.append("You are Disconnected from the Server.\n");
                skt.close(); //Closes the connection to the socket
    			Thread.sleep(1500); //Pauses before closing the window
    			System.exit(0);
    		} 
    		catch (InterruptedException | IOException e) 
    		{
    			e.printStackTrace();
    		}
    	}
    	else if (isConnected == false)
    	{
    		System.exit(0);
    	}
    }
                         
    //The GUI method, where everything is created and initialized
    private void initialize() 
    {
        	//Variables declaration
        	userLabel = new javax.swing.JLabel();
        	onlineLabel = new javax.swing.JLabel();
        	passLabel = new javax.swing.JLabel();
            receiveLabel = new javax.swing.JLabel();
            btnDisconnect = new javax.swing.JButton();
            btnRegister = new javax.swing.JButton();
            btnConnect = new javax.swing.JButton();
            btnSend = new javax.swing.JButton();
            userNameInput = new javax.swing.JTextField();
            passWordInput = new javax.swing.JPasswordField();
            onlineUsers = new javax.swing.JTextArea();
            chatWindow = new javax.swing.JTextArea();
            typingArea = new javax.swing.JTextArea();
            onlineUsersScroll = new javax.swing.JScrollPane();
            chatWindowScroll = new javax.swing.JScrollPane();
            typingAreaScroll = new javax.swing.JScrollPane();
            receiveListScroll = new javax.swing.JScrollPane();
            receiveList = new javax.swing.JList<>();
            
            jMenuBar1 = new javax.swing.JMenuBar();
            jMenu1 = new javax.swing.JMenu();
            jMenuItem1 = new javax.swing.JMenuItem();
            jMenu2 = new javax.swing.JMenu();
            jMenuItem2 = new javax.swing.JMenuItem();
            jMenu3 = new javax.swing.JMenu();
            jMenuItem3 = new javax.swing.JMenuItem();
            jMenu4 = new javax.swing.JMenu();
            jMenuItem4 = new javax.swing.JMenuItem();

            setTitle("TransLIT- The Translating Chat Application | Client");
            
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
            
            //jLabel Code
            userLabel.setText("Username:");
            passLabel.setText("Password:");

            onlineLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            onlineLabel.setText("Users Online");
            
            receiveLabel.setText("Receive As:");
            
            //Button Code
            btnRegister.setText("Register");
            btnRegister.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//Method called when someone presses the register button
                    btnRegisterActionPerformed(evt);
                }
            });
            
            btnConnect.setText("Connect");
            btnConnect.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//Method called for when someone presses the connect button
                    btnConnectActionPerformed(evt);
                }
            });
            
            btnDisconnect.setText("Disconnect");
            btnDisconnect.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//Method for when someone presses the disconnect button
                    btnDisconnectActionPerformed(evt);
                }
            });
            
            btnSend.setText("Send");
            btnSend.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//Method performed when the user presses the send key
                    btnSendActionPerformed(evt);
                }
            });

            //jTextArea Code
            onlineUsers.setEditable(false);
            onlineUsers.setColumns(20);
            onlineUsers.setLineWrap(true);
            onlineUsers.setRows(5);
            onlineUsers.setWrapStyleWord(true);
            onlineUsersScroll.setViewportView(onlineUsers);

            chatWindow.setEditable(false);
            chatWindow.setColumns(20);
            chatWindow.setLineWrap(true);
            chatWindow.setRows(5);
            chatWindow.setWrapStyleWord(true);
            chatWindowScroll.setViewportView(chatWindow);

            typingArea.setColumns(20);
            typingArea.setLineWrap(true);
            typingArea.setRows(5);
            typingArea.setWrapStyleWord(true);
            typingAreaScroll.setViewportView(typingArea);

            //jList Code

            receiveList.setModel(new javax.swing.AbstractListModel<String>() 
            {
    			private static final long serialVersionUID = 1L; //It was giving me errors before it added the serialversionUID thing
    			String[] strings = { "Arabic", "Bosnian", "Bulgarian", "Catalan", "Chinese (S)" , "Chinese (T)", "Croatian", "Czech", "Danish", "Dutch", 
    					 "English", "Estonian", "Finnish", "French", "German", "Greek", "Haitian Creole", "Hebrew", "Hindi", "Hmong Daw", "Hungarian", 
    					 "Indonesian", "Italian", "Japanese", "Kiswahili", "Korean", "Latvian", "Lithuanian", "Malay", "Maltese", "Norwegian", "Persian",
    					 "Polish","Portuguese", "Romanian", "Russian", "Serbian (C)", "Serbian (L)", "Slovak", "Slovenian", "Spanish", "Swedish", "Thai",
    					 "Turkish", "Ukranian", "Urdu", "Vietnamese", "Welsh", "Yucatec Maya"};
                public int getSize() { return strings.length; }
                public String getElementAt(int i) { return strings[i]; }
            });
            receiveListScroll.setViewportView(receiveList);
            receiveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            receiveList.setSelectedIndex(10);
            receiveList.addListSelectionListener(new javax.swing.event.ListSelectionListener() 
            {
                public void valueChanged(javax.swing.event.ListSelectionEvent evt) 
                {
                    receiveListValueChanged(evt);
                }
            });
            
            
            //Menu, menubar, and menuitem code and actions
            jMenu1.setText("Debug");
            jMenuItem1.setText("Add Another Client");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//Add another client action when clicked
                    jMenuItem1ActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItem1);
            jMenuBar1.add(jMenu1);
            setJMenuBar(jMenuBar1);
            
            jMenu2.setText("Account");
            jMenuItem2.setText("Change Password");
            jMenuItem2.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//Change Password action when clicked
                    jMenuItem2ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem2);
            jMenuBar1.add(jMenu2);
            setJMenuBar(jMenuBar1);
            
            jMenu3.setText("About");
            jMenuItem3.setText("About This Program");
            jMenuItem3.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//About this program action when clicked
                    jMenuItem3ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem3);
            jMenuBar1.add(jMenu3);
            setJMenuBar(jMenuBar1);
            
            jMenu4.setText("Contact");
            jMenuItem4.setText("Contact Us");
            jMenuItem4.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                	//Change Password action when clicked
                    jMenuItem4ActionPerformed(evt);
                }
            });
            jMenu4.add(jMenuItem4);
            jMenuBar1.add(jMenu4);
            setJMenuBar(jMenuBar1);
            
            
            //Get content pane, without this the positioning of items wont work
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            
            //Horizontal layout settings (Auto- Generated)
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(receiveListScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(receiveLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(typingAreaScroll)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(chatWindowScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 702, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(userLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(userNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(passLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(passWordInput, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(onlineUsersScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(onlineLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            
            //Vertical layout settings (Auto - Generated)
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(userLabel)
                                .addComponent(userNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(onlineLabel)
                                .addComponent(passLabel)
                                .addComponent(passWordInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRegister)
                                .addComponent(btnDisconnect)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(receiveLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(receiveListScroll))
                            .addComponent(onlineUsersScroll)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chatWindowScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(typingAreaScroll)
                                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
                );
            
            pack();//I've noticed that if this is not here, the window starts really small, and you would have to resize it yourself, this starts the window in the proper dimensions
            setLocationRelativeTo(null);//Code that makes sure that the window starts in the center of the screen
            
   } //end of gui generation
    
    
    int counter = 0; //Initializing counter for the IP banning service
    //Action when the connect button is pressed
    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt)
    {   
    	if (isConnected == false)
    	{
    		
    		try
    		{
    			//MySQL Connections
    			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
    			
    			Statement statement = connect.createStatement();
    			Statement banStatement = connect.createStatement();
    			Statement onlineCheck = connect.createStatement();
    			String query = "SELECT * FROM `loginvariables`";
    			String banConnect = "SELECT * FROM `ipbanlist`";
    			String duplicateConnect = "SELECT * FROM `onlinelist`";
    			ResultSet result = statement.executeQuery(query);
    			ResultSet banResult = banStatement.executeQuery(banConnect);
    			ResultSet onlineResult = onlineCheck.executeQuery(duplicateConnect);
    			
    			//Gets the text from each of the fields
    			String username = userNameInput.getText();
    			String password = new String(passWordInput.getPassword());
    			InetAddress userIP = InetAddress.getLocalHost();//Getting the IP of the machine/user
    			String clientIP = userIP.getHostAddress();//Converting it from InetAddress to String
    			
    			//Set the text edit ability to false since we do not want the user tampering with them once submitted
    			userNameInput.setEditable(false); 
    			passWordInput.setEditable(false);
            
    			//similar to the register frame, this boolean is set to false so that it doesn't skip the looping process and cause problems
    			Boolean infoCorrect = false; 
    			
    			
    			//peter code
    			Boolean duplicate =  true; //assumed that person is already logged on 			
    			
    			//Loop that determines whether or not the attempt is from a banned ip
    			while(banResult.next())
    			{
    				if (banResult.getString(1).equals(clientIP))
    				{
    					JOptionPane.showMessageDialog(null, "You have exceeded the maximum amount of login attempts, you have been IP BANNED.\n\n You cannot login, the program will now close once you press the 'OK'  button. \n\n If you wish to be unbanned, please use the contact window by clicking on the 'contact' text at the top of the menu of this window.");
    					System.exit(0);
    				}
    			}
    			
    			//check for duplicates online
    			while(onlineResult.next())
    			{
    				if (onlineResult.getString(1).equals(username))
    				{
    					duplicate = true;
    					break;
    				}
    				
    				else if (!onlineResult.getString(1).equals(username))
    				{
    					duplicate = false; // no duplicate users!
    				}
    			}
    			
    			//If the two things are empty
				if (userNameInput.getText().equals("") || passWordInput.getPassword().equals(""))//If the input is blank, notify the user
				{
					JOptionPane.showMessageDialog(null, "One or more fields were left blank, please enter the username and/or password where it was left blank. \n \nDon't have an account? Press the register button on the left hand side to create and account.");
				}
            
				else if (!userNameInput.getText().equals("") || !passWordInput.getPassword().equals(""))//If the input is not blank
				{
					//While loop that checks to see if the current username and password they entered was correct
	    			while(result.next()) 
	    			{
	    				if (result.getString(2).equals(username) && result.getString(3).equals(password))
	    				{
	    					infoCorrect = true;
	    					break;//break to stop the search since we have what we need
	    				}
	    				else if (!result.getString(2).equals(username) || !result.getString(3).equals(password)) //else if 
	    				{
	    					infoCorrect = false; //keep the true/false thing false
	            		
	    				}
	    			}
				}
            
    			//if the login information is incorrect, tell the user that their login credentials that their stuff doesn't match 
    			if (infoCorrect == false) 
    			{
    				counter = counter + 1;
    				JOptionPane.showMessageDialog(null, "Current login creditentials invalid. Please check to make sure that the username and password are correct. \n \n This is attempt " + counter + " of 5 attempts. If you exceed 5 attempts, you will be IP BANNED. \n\nBoth Usernames and Passwords are CASE SENSITIVE, please make sure that they are properly capitalized.");
    				//Allow them to edit the fields again until they submit
    				userNameInput.setEditable(true);
    				passWordInput.setEditable(true);
    				if (counter > 5) //If the counter is greater than 5
        			{
    					System.out.println(counter);
        				if (result.next()) //Inserts banned account into the ban list on the mySQL server
        				{
        					String sql = "INSERT INTO ipbanlist (InternetProtocol)" + "VALUES (?)";
        					PreparedStatement preparedStatement = connect.prepareStatement(sql);//Calling where we want to store this
        					preparedStatement.setString(1, clientIP);
        					preparedStatement.executeUpdate();
        					
        				}
        				JOptionPane.showMessageDialog(null, "You have exceeded the maximum amount of login attempts, you have been IP BANNED.\n\n You cannot login, the program will now close once you press the 'OK'  button.");
    					System.exit(0);
        			}
    			}
    			
    			else if (duplicate == true) // if there are duplicate users on the server
    			{
    				JOptionPane.showMessageDialog(null, "You are already logged on the server. \n\n If this is not you, please change your password as soon as possible.");
        			userNameInput.setEditable(true); 
        			passWordInput.setEditable(true);
    			}
    			
    			//If everything is good, it can finally continue and update the request
    			else if (infoCorrect == true && duplicate == false)
    			{
    				counter = 0; //Resets the login attempts
    				try
    				{
    					//Similar statements from above, where it creates another result set in order to not interfere with what the above is doing
    					Statement statement2 = connect.createStatement();
    					String query2 = "SELECT * FROM `loginvariables`";
    					ResultSet result2 = statement2.executeQuery(query2);
    					if (result2.next())
    					{
    						//Server connections, and input and output streams
                    		skt = new Socket(serverHost, Port);
                    		InputStreamReader streamIn = new InputStreamReader(skt.getInputStream());
                    		in = new BufferedReader(streamIn);
                    		out = new PrintWriter(skt.getOutputStream());
                    		
                    		chatWindow.append("Welcome to the Server! \n");
                    		out.println(username + ":has connected.:Connect"); //Posts this to the server and client chat window.
                    		out.flush(); //Flushes the stream
                    		isConnected = true; //Sets to true to notify that the client is in fact connected to a server.
                    	}
    				}
                		catch (Exception ex) 
                    	{
                    		//Error message if the server is offline
                    		chatWindow.append("The client cannot connect to the server! It may be offline. If the problem persists, check your internet settings! \n");
                    		//Keeps the user name field editable, as they still have a chance to change their name before connecting to a server.
                    		userNameInput.setEditable(true);
                    		passWordInput.setEditable(true);
                    	}
                    	readClient();//The read client method, so it starts reading whatever the input of the user is.	
                    }			
            	}
    			//Error Handler for MySQL, will notify the coder in the window of an error... if there is an error
    			//This catch statement is also here if the client cannot get the users IP
            	catch(SQLException | UnknownHostException ex) 
            	{
            		chatWindow.append("The client cannot connect to the ACCOUNT server! It may be offline. If the problem persists, check your internet settings! \n");
            		//Keeps the user name field editable, as they still have a chance to change their name before connecting to a server.
            		userNameInput.setEditable(true);
            		passWordInput.setEditable(true);
            		ex.printStackTrace();
            	} 
    		}
    	else if (isConnected == true)
    	{
    		JOptionPane.showMessageDialog(null, "You are already connected to the server!");
    	}
    }

    //Action that happens if the user presses the disconnect button
    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) 
    {                
    	//Calls for these methods to take action
        serverDisconnect();
        Disconnected();
    }   
    
    //If the user presses the send button in the client
    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) 
    {              
  
    	//Default String, which holds nothing
        String nothing = "";
        if ((typingArea.getText()).equals(nothing))  //If the user presses the button, and nothing is there, this statement activates
        {
        	//Doesn't post it to the chat
            typingArea.setText("");
            typingArea.requestFocus(); //Keep the type focus on the chat, so the user can type something still if they wish
        } 
        else 
        {
            try //If there is something in the chat box, try sending it to the server, for it to be posted to the chat
            {
            	//Sends the statement to the server, with who said it, and where they said it
            	runOnce(true);
            	out.println(userNameInput.getText() + ":" + typingArea.getText() + ":" + "Chat");
            	out.flush(); //Flushes the stream
            } 
            catch (Exception ex) //If the server cannot receive the message, this statement enters the chat.
            {
                chatWindow.append("Message was not sent. Server may not be Responding, or the server is offline. \n");
            }
            typingArea.setText("");
            typingArea.requestFocus();
        }
        typingArea.setText(""); //resets the chat box so that they can continue typing
        typingArea.requestFocus(); //Keeps focus on the typing area, as a user may want to send something
    } 
        
    //Method for if the user clicks on the register button
    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) 
    {                                    
    	//Opens the register window (register_Frame.java)
        register_Frame register = new register_Frame();
        register.setVisible(true);
    }  
    
   public String receiveListValueChanged(ListSelectionEvent evt) 
    {
	  
		// TODO Auto-generated method stub
    	String[] lang = { "Arabic", "Bosnian", "Bulgarian", "Catalan", "Chinese (S)" , "Chinese (T)", "Croatian", "Czech", "Danish", "Dutch", 
				 "English", "Estonian", "Finnish", "French", "German", "Greek", "Haitian Creole", "Hebrew", "Hindi", "Hmong Daw", "Hungarian", 
				 "Indonesian", "Italian", "Japanese", "Kiswahili", "Korean", "Latvian", "Lithuanian", "Malay", "Maltese", "Norwegian", "Persian",
				 "Polish","Portuguese", "Romanian", "Russian", "Serbian (C)", "Serbian (L)", "Slovak", "Slovenian", "Spanish", "Swedish", "Thai",
				 "Turkish", "Ukranian", "Urdu", "Vietnamese", "Welsh", "Yucatec Maya"};
	   
    	String[] langCode = {"ar", "bs", "bg", "ca", "zh-CHS", "zh-CHT", "hr", "cs", "da", "nl", "en", "et", "fi", "fr", "de", "el", "ht", "he", "hi",
        		"mww", "hu", "id", "it", "ja", "sw", "ko", "lv", "lt", "ms", "mt", "no", "fa", "pl", "pt", "ro", "ru", "sr-Cyrl", "sr-Latn", "sk", "sl", "es",
        		"sv", "th", "tr", "uk", "ur","vi", "cy", "yua"};
	   
    	String listNameT = receiveList.getSelectedValue().toString(); //grabs language after the button is pressed
    	int listNum = Arrays.asList(lang).indexOf(listNameT); //compares the language chosen from the array "lang", gets the index of it as an int
    	
    	String Set = langCode[listNum]; //get integer value of langCode.
    	return Set; //returns the language code
	}
    
   
    //JMenu Code

    //Method for if the user clicks "Open Another Client" in the "Debug" Section of the menu at the top.
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) 
    {   
    	//opens another client window (TransLIT_Client.java)
        new TransLIT_Client().setVisible(true);
    }    
    
    //Method for if the user clicks "Change Password" in the "Change Password" Section of the menu at the top.
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) 
    {        
    	//opening the changePassword window (changePassword_Frame.java)
    	changePassword_Frame password = new changePassword_Frame();
        password.setVisible(true);
    }   
    
    //Method for if the user clicks "About This Program" in the "About" Section of the menu at the top.
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) 
    {                
    	//opening the about window (about_Frame.java)
    	about_Frame about = new about_Frame();
    	about.setVisible(true);
    }    
    
    //Method for if the user clicks "Contact" in the "About" Section of the menu at the top.
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) 
    {                
    	//opening the about window (contact_Frame.java)
    	contact_Frame contact = new contact_Frame();
    	contact.setVisible(true);
    }  
    
    //Calling the variables from the initialize method, so that we can use them in other methods and things (since it is a private method)
    //Do not modify these variables pls
    
    //Menubar and menu item Declarations
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;

    //GUI Buttons, Text Areas, etc. Declarations
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel userLabel;
    private javax.swing.JLabel passLabel;
    private javax.swing.JLabel onlineLabel;
    private javax.swing.JLabel receiveLabel;
    private javax.swing.JTextArea chatWindow;
    private javax.swing.JTextArea onlineUsers;
    private javax.swing.JTextArea typingArea;
    private javax.swing.JScrollPane chatWindowScroll;
    private javax.swing.JScrollPane onlineUsersScroll;
    private javax.swing.JScrollPane typingAreaScroll;
    private javax.swing.JTextField userNameInput;
    private javax.swing.JPasswordField passWordInput;
    private javax.swing.JList<String> receiveList;
    private javax.swing.JScrollPane receiveListScroll;             
}