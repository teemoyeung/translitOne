import java.sql.*;
import java.util.logging.*;
import javax.swing.*;

public class changePassword_Frame extends javax.swing.JFrame 
{

    public static void main(String args[]) //the Main Method
    {
    	
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() //When run, open the window
            {
                new changePassword_Frame().setVisible(true);//Display the creation
            }
        });
    }
    
    public changePassword_Frame() 
    {
        initialize();//calling the method that initializes the GUI frame
    }

    private void initialize() //The method that sets the dimensions and what is in the GUI
    {
    	//Variable Declaration
        changeLabel = new javax.swing.JLabel();
        changUserLabel = new javax.swing.JLabel();
        changeCurrentLabel = new javax.swing.JLabel();
        changeNewLabel = new javax.swing.JLabel();
        changeConfirmLabel = new javax.swing.JLabel();
        changeUserInput = new javax.swing.JTextField();
        changeCurrentInput = new javax.swing.JPasswordField();
        changeNewInput = new javax.swing.JPasswordField();
        changeConfirmInput = new javax.swing.JPasswordField();
        btnChangePassword = new javax.swing.JButton();

        //Dispose on close rather than exit, because the applcation should exit if they just close this extra window
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        //Set the title of the JFrame to the text in the quotations
        setTitle("TransLIT- Change Password");

        //Header jLabel Code
        changeLabel.setFont(new java.awt.Font("Tahoma", 0, 24));
        changeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        changeLabel.setText("Change Password");

        //jLabel Code
        changUserLabel.setText("Username:");

        changeCurrentLabel.setText("Current Password:");

        changeNewLabel.setText("New Password:");

        changeConfirmLabel.setText("Confirm New Password:");

        //JButton Code
        btnChangePassword.setText("Change Password");
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                btnChangePasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //Horizontal Layout (Auto - Generated)
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(changeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(changeCurrentLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(changeCurrentInput))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(changUserLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(changeUserInput, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(changeNewLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(changeNewInput))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(changeConfirmLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(changeConfirmInput))
                            .addComponent(btnChangePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(120, Short.MAX_VALUE))
        );
        
        //Vertical Layout (Auto - Generated)
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(changeLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changUserLabel)
                    .addComponent(changeUserInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeCurrentLabel)
                    .addComponent(changeCurrentInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeNewLabel)
                    .addComponent(changeNewInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeConfirmLabel)
                    .addComponent(changeConfirmInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnChangePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack(); //This is needed to set the window at it's required dimensions, so that the user doesn't have to manually resize its
        setLocationRelativeTo(null);//Makes this window open in the exact center of the screen that the user has
    }
    
    //Process that happens when the user presses the submit button
    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	try
    	{
    		//MySQL Connections
    		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
    	   
    		Statement statement = connect.createStatement();
    	    String query = "SELECT * FROM `loginvariables`";
            ResultSet result = statement.executeQuery(query);
            
            //Gets the text from each of the fields
            String username = changeUserInput.getText();
            String currentPassword = new String(changeCurrentInput.getPassword());
            String newPassword = new String(changeNewInput.getPassword());
            String confirmNewPassword = new String(changeConfirmInput.getPassword());
            
            //Set the text editability to false since we do not want the user tampering with them once submitted
            changeUserInput.setEditable(false); 
            changeCurrentInput.setEditable(false);
            changeNewInput.setEditable(false);
            changeConfirmInput.setEditable(false);
            
            //similar to the register frame, this boolean is set to false so that it doesn't skip the looping process and cause problems
            Boolean infoCorrect = false; 
            
            //Int value for when we update the database
            int value = 0;
            
            //While loop that checks to see if the current username and password they entered was correct
            while(result.next()) 
            {
            	if (result.getString(2).equals(username) && result.getString(3).equals(currentPassword))
            	{
            		infoCorrect = true;
            		value = result.getInt(1); //if it is correct, get the id number of the account to properly update that row.
            		break;//break to stop the search since we have what we need
            	}
            	else if (!result.getString(2).equals(username) || !result.getString(3).equals(currentPassword)) //else if 
            	{
            		
                    infoCorrect = false; //keep the true/false thing false
            		
            	}
            }
            
            //if the login information is incorrect, tell the user that their login credentials that their stuff doesn't match 
            if (infoCorrect == false) 
            {
            	JOptionPane.showMessageDialog(null, "Current login creditentials invalid. Please check to make sure that the username and password are correct.");
        		
            	//Allow them to edit the fields again until they submit
            	changeUserInput.setEditable(true);
                changeCurrentInput.setEditable(true);
                changeNewInput.setEditable(true);
                changeConfirmInput.setEditable(true);
            }

            //if the new password does not match the confirmation field
            if(!newPassword.equals(confirmNewPassword))
            {
            	JOptionPane.showMessageDialog(null, "the new passwords do not match. Please type in a matching password.");
            	
            	//Allow them to edit the fields until they submit again
            	changeUserInput.setEditable(true); 
                changeCurrentInput.setEditable(true);
                changeNewInput.setEditable(true);
                changeConfirmInput.setEditable(true);
            }
            
            //if the new password is less than 6 characters long
            else if (newPassword.length() <= 5) 
            {
            	JOptionPane.showMessageDialog(null, "the new password is not long enough! Please type in a longer password.");
            	
            	//Re-enable the fields so that they can fix the information
            	changeUserInput.setEditable(true); 
                changeCurrentInput.setEditable(true);
                changeNewInput.setEditable(true);
                changeConfirmInput.setEditable(true);
    		
            } 
    	
            //If everything is good, it can finally continue and update the request
            else if (newPassword.length() > 5 && newPassword.equals(confirmNewPassword) && infoCorrect == true)
            {
            	try
            	{
            		//Similar statements from above, where it creates another result set in order to not interfere with what the above is doing
            	    Statement statement2 = connect.createStatement();
            		String query2 = "SELECT * FROM `loginvariables`";
            		ResultSet result2 = statement2.executeQuery(query2);
                    int count = 0;
            		if (result2.next())
            		{
            			
            			//Another statement that declares that it should update the certain row
            			String sql = "UPDATE `loginvariables` SET `id`=?,`Username`=?,`Password`=? WHERE `Username` = ?";
            			PreparedStatement preparedStatement = connect.prepareStatement(sql);
            			preparedStatement.setLong(1, value);
            			preparedStatement.setString(2, username);
            			preparedStatement.setString(3, newPassword);
            			
            			//Fourth parameter as without it, it will overwrite all the other rows, so this field specifies which account to change the password with
            			preparedStatement.setString(4, username);
            			preparedStatement.executeUpdate();
            			
            			//Notify the user that the password change has been successfully changed
            			JOptionPane.showMessageDialog(null, "Password has been successfully changed.");
            			dispose();//Close the window
            		}
            		connect.close(); //Close the connection to the MySQL database for safety with errors and such
            	}
            	catch(SQLException ex) //Error Handler for MySQL, will notify the coder in the window of an error... if there is an error 
            	{
            		Logger.getLogger(TransLIT_Client.class.getName()).log(Level.SEVERE, null, ex);
            	}
            }
    	
    	}
    	catch(SQLException ex) //Error Handler for MySQL, will notify the coder in the window of an error... if there is an error
    	{
    		Logger.getLogger(TransLIT_Client.class.getName()).log(Level.SEVERE, null, ex);
    	}
    }
    
    //Variable Declaration outside of initialize method, so that other methods can call them
    //Do not modify pls
    private javax.swing.JLabel changUserLabel;
    private javax.swing.JPasswordField changeConfirmInput;
    private javax.swing.JLabel changeConfirmLabel;
    private javax.swing.JPasswordField changeCurrentInput;
    private javax.swing.JLabel changeCurrentLabel;
    private javax.swing.JLabel changeLabel;
    private javax.swing.JPasswordField changeNewInput;
    private javax.swing.JLabel changeNewLabel;
    private javax.swing.JTextField changeUserInput;
    private javax.swing.JButton btnChangePassword;
}
