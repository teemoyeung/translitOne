import java.net.*;
import java.sql.*;
import java.util.logging.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class register_Frame extends javax.swing.JFrame 
{
    public static void main(String args[]) //Main Method
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new register_Frame().setVisible(true); //Display the Creation!
            }
        });
    }

    //Calling the GUI method
    public register_Frame() 
    {
        initialize(); 
    }

    //The GUI Generation Method
    private void initialize() 
    {

    	//Declaring Variables inside the method
    	regTitleLabel = new javax.swing.JLabel();
        regUserLabel = new javax.swing.JLabel();
        regPassLabel = new javax.swing.JLabel();
        regConfirmLabel = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        regUserInput = new javax.swing.JTextField();
        regPassInput = new javax.swing.JPasswordField();
        regConfirmInput = new javax.swing.JPasswordField();

      //Want to dispose it rather than exit, because if it is exit, it will close both this window and the client window
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        //jLabel Header Code
        regTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 24));//Font to differentiate between regular text and a title
        regTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);//Center Register title
        regTitleLabel.setText("Register");

        //jLabel Code
        regUserLabel.setText("Username:");

        regPassLabel.setText("Password:");

        regConfirmLabel.setText("Confirm Password:");

        //Button Code
        btnSubmit.setText("Register");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
            	//Calling the action when something is clicked on the button
                btnSubmitActionPerformed(evt);
            }
        });
        
        //Sets the title of the JFrame
        setTitle("TransLIT- Register");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //Horizontal layout (Auto - Generated)
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(regPassLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(regPassInput))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(regUserLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(regTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(regUserInput, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(regConfirmLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(regConfirmInput, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(36, Short.MAX_VALUE))
            );
        
        	//Vertical layout (Auto - Generated)
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(regTitleLabel)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(regUserLabel)
                        .addComponent(regUserInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(regPassLabel)
                        .addComponent(regPassInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(regConfirmLabel)
                        .addComponent(regConfirmInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGap(46, 46, 46))
            );

            pack(); //Sets default size, so the user does not have to manually set the dimensions of the window
            setLocationRelativeTo(null);//Starts the window in the center of the screen
    }
    
    //The process that takes place when the user hits the submit button
    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) 
    {   
    	try
    	{
    		//Connections to the MySQL Server
    		//Technically this is the third one (as more are below, but this is to check a certain part of the database, learn more about it at the bottom
    		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
    	    Statement statement3 = connect.createStatement();
    	    String query3 = "SELECT * FROM `loginvariables`";
            ResultSet result3 = statement3.executeQuery(query3);
            
            //Gets what the field has written in it.
            String username = regUserInput.getText(); 
            String password = new String(regPassInput.getPassword());
            String verifyPassword = new String(regConfirmInput.getPassword());
            
            //Once the user has pressed connect with a User name, it disables the field so that they can't change their user name.
            regUserInput.setEditable(false); 
            regPassInput.setEditable(false);
            regConfirmInput.setEditable(false);
    	
            //Set this to false as default as we want the database to be checked before it is deemed as a unique user, see the while loop below
            Boolean unique = false;
    	
            //While loop to find out if the username entered is taken already
            while(result3.next()) 
            {
            	String db_User = result3.getString(2); //goes through each row to see the usernames already registered
            	if (db_User.equals(username))
            	{
            		JOptionPane.showMessageDialog(null, "Username Already Taken! Please use a different username.");
            		regUserInput.setEditable(true);
            		regPassInput.setEditable(true);
            		regConfirmInput.setEditable(true);
            		unique = false; //sets this to false, so that it prevents the else if statement at the bottom to not go through
            		break; //breaks the loop before it has a chance to continue all the way through until the end, where the chance of it "being unique" (when it isn't) increases because computers are dumb
            	}
            	else if (!db_User.equals(username)) //if it is unique in the end, set the unique status to true, which is good.
            	{
            		unique = true;
            	}
            }
    	
            //If the Password does not match with the Password Confirmation
            if(!password.equals(verifyPassword))
            {
            	//Notifies the user that the password does not match and allows them to edit their input fields
            	JOptionPane.showMessageDialog(null, "Passwords do not match. Please type in a matching password.");
            	regUserInput.setEditable(true);
            	regPassInput.setEditable(true);
            	regConfirmInput.setEditable(true);
            }
            
            //If the password is too short
            else if (password.length() <= 5) 
            {
            	//Notifies the user that the password is too short and allows them to edit their input fields
            	JOptionPane.showMessageDialog(null, "Password is not long enough! Please type in a longer password.");
            	regUserInput.setEditable(true);
            	regPassInput.setEditable(true);
            	regConfirmInput.setEditable(true);
    		
            } 
    	
            //If everything is good, it can finally attempt to register the account
            else if (password.length() > 5 && password.equals(verifyPassword) && unique == true)
            {
            	try
            	{
            		//Two Statements for two result sets
            	    Statement statement = connect.createStatement();
            	    Statement statement2 = connect.createStatement();
            	    
            	    //Two queries for two different results sets
            		String query = "SELECT * FROM `loginvariables`";
            		String query2 = "SELECT MAX(id) as id FROM `loginvariables`";
            		
            		//Two result sets so that they don't interfere with eachother
            		ResultSet result = statement.executeQuery(query);
            		ResultSet result2 = statement2.executeQuery(query2);
            		
                    int count = 0;
        	
                    //Getting the highest current number, so that we can increase the amount when we try to insert another row
            		//Needed a second result set here so that the information would not be affected and it would be registering (x + 1) amount of people, where x is the highest id
                    while( result2.next())
            		{
            			count = result2.getInt("id"); //Highest id in the database
            		}
            		
            		if (result.next())
            		{
            			//third String command after everything has been sorted out
            			String sql = "INSERT INTO loginvariables (id, Username, Password)" + "VALUES (?, ?, ?)";
            			
            			//Prepared statement because it is set variables that we want to add, not just a search method
            			PreparedStatement preparedStatement = connect.prepareStatement(sql);
            			preparedStatement.setLong(1, count+1);
            			preparedStatement.setString(2, username);
            			preparedStatement.setString(3, password);
            			preparedStatement.executeUpdate();
            			
            			//Confirmation that it has successfully registered the user
            			JOptionPane.showMessageDialog(null, "Account has been registered successfully.");
            			dispose();//closes the register window, without exiting the whole application
            		}
            		connect.close(); //closing the connection to MySQL to be safe and not create any errors
            	}
            	catch(SQLException ex) //Error Handler for the MySQL code, should tell you the problem with the code if something is wrong with it
            	{
            		Logger.getLogger(TransLIT_Client.class.getName()).log(Level.SEVERE, null, ex);
            	}
            }
    	
    	}
    	catch(SQLException ex) //Error Handler for the MySQL code, should tell you the problem with the code if there is something wrong with it.
    	{
    		Logger.getLogger(TransLIT_Client.class.getName()).log(Level.SEVERE, null, ex);
    	}
    }
    	
    //Auto Generated code that allows the coder to use these variables outside of the initializer method, if needed
    //Do not modify pls
    private javax.swing.JButton btnSubmit;
    private javax.swing.JPasswordField regConfirmInput;
    private javax.swing.JLabel regConfirmLabel;
    private javax.swing.JPasswordField regPassInput;
    private javax.swing.JLabel regPassLabel;
    private javax.swing.JLabel regTitleLabel;
    private javax.swing.JTextField regUserInput;
    private javax.swing.JLabel regUserLabel;
}
