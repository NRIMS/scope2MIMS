/*
 * Holder_Ref_Data_View.java
 */

package com.nrims.holder_ref_data;

import javax.swing.text.BadLocationException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import com.nrims.holder_data_mgmt.*;


/**
 * The application's main frame.
 * @author bepstein
 */
public class Holder_Ref_Data_View extends FrameView {

    public Holder_Ref_Data_View(SingleFrameApplication app) {
        super(app);

        initComponents();

        // Custom initialization.
        initInternalData();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = Holder_Ref_Data_App.getApplication().getMainFrame();
            aboutBox = new Holder_Ref_Data_AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        Holder_Ref_Data_App.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        coeff_file_label = new javax.swing.JLabel();
        coeff_file_text = new javax.swing.JTextField();
        coeff_file_browse_button = new javax.swing.JButton();
        points_text_file_label = new javax.swing.JLabel();
        coord_file_text = new javax.swing.JTextField();
        coord_file_browse_button = new javax.swing.JButton();
        ref_file_label = new javax.swing.JLabel();
        ref_file_text = new javax.swing.JTextField();
        ref_file_browse_button = new javax.swing.JButton();
        holder_reg_gen_button = new javax.swing.JButton();
        holder_reg_review_button = new javax.swing.JButton();
        data_point_comment_text = new javax.swing.JTextField();
        data_point_comment_label = new javax.swing.JLabel();
        data_point_num_check = new javax.swing.JCheckBox();
        data_point_num_check_label = new javax.swing.JLabel();
        date_text_label = new javax.swing.JLabel();
        date_text = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.nrims.holder_ref_data.Holder_Ref_Data_App.class).getContext().getResourceMap(Holder_Ref_Data_View.class);
        coeff_file_label.setText(resourceMap.getString("coeff_file_label.text")); // NOI18N
        coeff_file_label.setName("coeff_file_label"); // NOI18N

        coeff_file_text.setText(resourceMap.getString("coeff_file_text.text")); // NOI18N
        coeff_file_text.setName("coeff_file_text"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.nrims.holder_ref_data.Holder_Ref_Data_App.class).getContext().getActionMap(Holder_Ref_Data_View.class, this);
        coeff_file_browse_button.setAction(actionMap.get("coeffFileBrowse")); // NOI18N
        coeff_file_browse_button.setText(resourceMap.getString("coeff_file_browse_button.text")); // NOI18N
        coeff_file_browse_button.setName("coeff_file_browse_button"); // NOI18N

        points_text_file_label.setText(resourceMap.getString("points_text_file_label.text")); // NOI18N
        points_text_file_label.setName("points_text_file_label"); // NOI18N

        coord_file_text.setText(resourceMap.getString("coord_file_text.text")); // NOI18N
        coord_file_text.setName("coord_file_text"); // NOI18N

        coord_file_browse_button.setAction(actionMap.get("coordFileBrowse")); // NOI18N
        coord_file_browse_button.setText(resourceMap.getString("coord_file_browse_button.text")); // NOI18N
        coord_file_browse_button.setName("coord_file_browse_button"); // NOI18N

        ref_file_label.setText(resourceMap.getString("ref_file_label.text")); // NOI18N
        ref_file_label.setName("ref_file_label"); // NOI18N

        ref_file_text.setText(resourceMap.getString("ref_file_text.text")); // NOI18N
        ref_file_text.setName("ref_file_text"); // NOI18N

        ref_file_browse_button.setAction(actionMap.get("refFileBrowse")); // NOI18N
        ref_file_browse_button.setText(resourceMap.getString("ref_file_browse_button.text")); // NOI18N
        ref_file_browse_button.setName("ref_file_browse_button"); // NOI18N

        holder_reg_gen_button.setAction(actionMap.get("holderRefGenerateFile")); // NOI18N
        holder_reg_gen_button.setText(resourceMap.getString("holder_reg_gen_button.text")); // NOI18N
        holder_reg_gen_button.setName("holder_reg_gen_button"); // NOI18N

        holder_reg_review_button.setAction(actionMap.get("holderRefReviewFile")); // NOI18N
        holder_reg_review_button.setText(resourceMap.getString("holder_reg_review_button.text")); // NOI18N
        holder_reg_review_button.setName("holder_reg_review_button"); // NOI18N

        data_point_comment_text.setText(resourceMap.getString("data_point_comment_text.text")); // NOI18N
        data_point_comment_text.setAction(actionMap.get("processDataPointComment")); // NOI18N
        data_point_comment_text.setName("data_point_comment_text"); // NOI18N

        data_point_comment_label.setText(resourceMap.getString("data_point_comment_label.text")); // NOI18N
        data_point_comment_label.setName("data_point_comment_label"); // NOI18N

        data_point_num_check.setText(resourceMap.getString("data_point_num_check.text")); // NOI18N
        data_point_num_check.setName("data_point_num_check"); // NOI18N

        data_point_num_check_label.setText(resourceMap.getString("data_point_num_check_label.text")); // NOI18N
        data_point_num_check_label.setName("data_point_num_check_label"); // NOI18N

        date_text_label.setText(resourceMap.getString("date_text_label.text")); // NOI18N
        date_text_label.setName("date_text_label"); // NOI18N

        date_text.setText(resourceMap.getString("date_text.text")); // NOI18N
        date_text.setName("date_text"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(coeff_file_label, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(points_text_file_label)
                                    .addComponent(data_point_comment_label))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(coord_file_text)
                                    .addComponent(data_point_comment_text)
                                    .addComponent(coeff_file_text, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(data_point_num_check_label)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(data_point_num_check))
                                    .addComponent(date_text, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(ref_file_label, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ref_file_text)))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(holder_reg_review_button)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(coeff_file_browse_button, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(coord_file_browse_button, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ref_file_browse_button, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(holder_reg_gen_button))))
                    .addComponent(date_text_label))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coeff_file_label, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coeff_file_text, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coeff_file_browse_button))
                .addGap(29, 29, 29)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(points_text_file_label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coord_file_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coord_file_browse_button))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(data_point_comment_label)
                    .addComponent(data_point_comment_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(data_point_num_check_label)
                    .addComponent(data_point_num_check))
                .addGap(17, 17, 17)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(date_text_label)
                    .addComponent(date_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(77, 77, 77)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ref_file_label)
                    .addComponent(ref_file_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ref_file_browse_button)
                    .addComponent(holder_reg_gen_button))
                .addGap(18, 18, 18)
                .addComponent(holder_reg_review_button)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 628, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void coeffFileBrowse() {
        JFileChooser fc = new JFileChooser();

        fc.showOpenDialog( getFrame() );
        coeff_file_text.setText( fc.getSelectedFile().getPath() );
    }

    @Action
    public void coordFileBrowse() {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog( getFrame() );
        coord_file_text.setText( fc.getSelectedFile().getPath() );
    }

    @Action
    public void refFileBrowse() {
        HolderDataFile hdf;
        RefPointList rpl;
        JFileChooser fc = new JFileChooser();
        fc.showSaveDialog( getFrame() );
        ref_file_text.setText( fc.getSelectedFile().getPath() );

        /* Allowing ref file review if it exists. */
        if ( (new File( ref_file_text.getText() )).exists() )
        {
            if ( dpfp == null )
                dpfp = new DataPointFileProcessor();

            dpfp.setHolderPointFilePath( ref_file_text.getText() );
            rpl = new RefPointList();

            hdf = new HolderDataFile( ref_file_text.getText(),
                false,
                rpl);

            hdf.readFileIn();
            hdf.close();
            dpfp.setRefPointList( hdf.getRefPointList() );

            holder_reg_review_button.setEnabled( true );
        }
         
    }

    @Action
    public void holderRefGenerateFile() {
        if ( dpfp == null )
            dpfp = new DataPointFileProcessor(
                    coeff_file_text.getText(),
                    coord_file_text.getText(),
                    ref_file_text.getText()
                    );
        else {
            dpfp.setCoeffFilePath( coeff_file_text.getText() );
            dpfp.setStagePointFilePath( coord_file_text.getText() );
            dpfp.setHolderPointFilePath( ref_file_text.getText() );
        }

        // Create the reference point list
        dpfp.processTransform(); 
        RefPointList rpl = dpfp.getRefPointList();
        // Set the comment for each point
        for (int i=0; i<rpl.getNumRefPoints(); i++)
            rpl.getRefPoint(i).setComment(getCommentFor(i));
        // Write the file
        HolderDataFile hdf = new HolderDataFile(dpfp.getHolderPointFilePath(), true, dpfp.getRefPointList());
        hdf.writeFileOut();
        hdf.close();
        
        /* Enabling review */
        holder_reg_review_button.setEnabled( true );
    }

    @Action
    public void holderRefReviewFile() {
        RefFileContentReviewFrame rfcrf = new RefFileContentReviewFrame( dpfp );
        rfcrf.setVisible( true );
    }

    /**
     * Get the comment string currently in the text field.
     * @return the comment string
     */
    public String getCurrentComment() {
        return getCommentFor(-1);
    }

    /**
     * Create the comment for a given reference point.
     *
     * Takes the comment text from the text field (including the index for the
     * point, if the check box is checked), and creates a comment string.
     * 
     * @param i the index of the reference point
     * @return the comment string for the given index
     */
    public String getCommentFor(int i) {
        String comment = "";
        int len = data_point_comment_text.getDocument().getLength();
        try { comment = data_point_comment_text.getDocument().getText(0, len); }
        catch (BadLocationException ex) {}
        if (data_point_num_check.isSelected() && i >= 0)
            comment = (i + 1) + " " + comment;
        return comment;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton coeff_file_browse_button;
    private javax.swing.JLabel coeff_file_label;
    private javax.swing.JTextField coeff_file_text;
    private javax.swing.JButton coord_file_browse_button;
    private javax.swing.JTextField coord_file_text;
    private javax.swing.JLabel data_point_comment_label;
    private javax.swing.JTextField data_point_comment_text;
    private javax.swing.JCheckBox data_point_num_check;
    private javax.swing.JLabel data_point_num_check_label;
    private javax.swing.JTextField date_text;
    private javax.swing.JLabel date_text_label;
    private javax.swing.JButton holder_reg_gen_button;
    private javax.swing.JButton holder_reg_review_button;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel points_text_file_label;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton ref_file_browse_button;
    private javax.swing.JLabel ref_file_label;
    private javax.swing.JTextField ref_file_text;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    /* custom private variables */
    private DataPointFileProcessor dpfp;

    /* Private methods */
    private void initInternalData()
    {
        dpfp = new DataPointFileProcessor();
        holder_reg_review_button.setEnabled( false );
        data_point_comment_text.setText( 
                dpfp.getRefPointList().getDefaultRefPointComment()
                );
    }

    @Action
    public void processDataPointComment()
    {

        dpfp.getRefPointList().setDefaultRefPointComment(getCurrentComment());
        
    }
}
