
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;


public class contact_Frame extends javax.swing.JFrame 
{
	public static void main(String args[]) //Main Method
	{    
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run() 
            {
                new contact_Frame().setVisible(true); //Displays the creation!
            }
        });
    }

	//Calling the GUI method
    public contact_Frame()
    {
        initialize();
    }

    //The GUI method, where everything is generated
    private void initialize() {

        contactLabel = new javax.swing.JLabel();
        emailInput = new javax.swing.JTextField();
        subjectInput = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageInput = new javax.swing.JTextArea();
        emailLabel = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        subjectLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();

        //jLabel Code
        contactLabel.setFont(new java.awt.Font("Tahoma", 0, 24));
        contactLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        contactLabel.setText("Contact Us");
        
        emailLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        emailLabel.setText("Email:");

        subjectLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subjectLabel.setText("Subject:");

        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageLabel.setText("Message Content:");
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //Sets the title of the JFrame
        setTitle("TransLIT- Contact");

        //jTextArea Code
        messageInput.setColumns(20);
        messageInput.setRows(5);
        jScrollPane1.setViewportView(messageInput);
        
        //jButton Code
        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
            	//Calling the action when something is clicked on the button
                btnSubmitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //Horizontal Layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(emailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(subjectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(contactLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE))
                                    .addComponent(emailInput)
                                    .addComponent(subjectInput)))
                            .addComponent(btnSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(48, 48, 48))))
        );
        
        //Vertical Layout
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(contactLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subjectLabel))
                .addGap(13, 13, 13)
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();//Need this so that the people don't need to resize themselves
        setLocationRelativeTo(null);//Starts the window in the center of the screen
    }
    
    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) 
    { 
    	final String username = "Translit321@gmail.com"; //username
		final String password = "translit321"; //password

		Properties props = new Properties(); 
		//connect to gmail smtp server
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587"); //port is 587

		Session session = Session.getInstance(props,
				//authentications, logins in with given user name and password
				//ALWAYS remember to go on google settings and turn on "allow access for less secure apps."
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
//once logged in, this part is to send the message to the given email
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("Translit321@gmail.com")); // from
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("Translit321@gmail.com")); // to
			message.setSubject(subjectInput.getText()); 
			message.setText("Message from: " + emailInput.getText() + "\n\n" + messageInput.getText());

			Transport.send(message); //send the message
			System.out.println("Done");


		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
           
    }
    
    private javax.swing.JButton btnSubmit;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JTextField emailInput;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea messageInput;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextField subjectInput;
    private javax.swing.JLabel subjectLabel;
}