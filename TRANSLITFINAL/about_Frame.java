//No imports this time

public class about_Frame extends javax.swing.JFrame 
{
	public static void main(String args[]) //Main Method
	{
	     
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run() 
            {
                new about_Frame().setVisible(true); //Displays the creation!
            }
        });
    }

	//Calling the GUI method
    public about_Frame()
    {
        initialize();
    }

    //The GUI method, where everything is generated
    private void initialize() 
    {
    	//Variable Declaration
        btnOk = new javax.swing.JButton();
        programbyLabel = new javax.swing.JLabel();
        author1Label = new javax.swing.JLabel();
        author2Label = new javax.swing.JLabel();
        aboutLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoArea = new javax.swing.JTextArea();

        //Setting the close operation to dispose as we do not want to completely close the program while the person is using it
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        //title of the JFrame
        setTitle("TransLIT- About This Program");

        //Button code
        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
            	//Method called for when the action is created
                btnOkActionPerformed(evt);
            }
        });

        //Author Label Code
        programbyLabel.setText("Program By:");

        author1Label.setText("Timothy Yeung");

        author2Label.setText("Peter Sirna");

        //Header Label Code
        aboutLabel.setFont(new java.awt.Font("Tahoma", 0, 24));
        aboutLabel.setText("About");

        //Readable information area Code
        infoArea.setEditable(false);
        infoArea.setColumns(20);
        infoArea.setLineWrap(true);
        infoArea.setRows(5);
        infoArea.setCaretPosition(0);
        infoArea.setText("Ever wanted to talk to someone in a different country without having to speak a different language? \n \nThis is TransLIT, the translating application. the idea behind this program is to solve the language barriers between two or more people, allowing them to communicate with eachother at the level of comfort of whatever language they choose and see fit.");
        infoArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(infoArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //Horizontal Layout (auto - generated)
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(142, 142, 142)
                                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(programbyLabel)
                                .addGap(100, 100, 100)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(author2Label)
                                    .addComponent(author1Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(aboutLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 132, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        
        //Vertical Layout (Auto- Generated)
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(aboutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(programbyLabel)
                    .addComponent(author1Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(author2Label)
                .addGap(31, 31, 31)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack(); //To start it at is default size, the user shouldn't have to resize it if it opens, which is what this thing prevents
        setLocationRelativeTo(null); //Makes sure this window starts in the exact center of the screen.
    }
    
    //Once the "Ok" button is pressed, do this
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	//Close the JFrame
    	dispose();
    }
    
    //Auto Generated Code, with the layouts, to ensure that they can be used outside of the initializer method, if needed
    //Do not modify pls
    private javax.swing.JLabel aboutLabel;
    private javax.swing.JLabel author1Label;
    private javax.swing.JLabel author2Label;
    private javax.swing.JButton btnOk;
    private javax.swing.JTextArea infoArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel programbyLabel;
}
