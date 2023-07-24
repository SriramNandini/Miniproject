import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
public class notepad extends JFrame implements ActionListener {
   
	private JTextArea textArea = new JTextArea("", 0,0);
    private JFileChooser fileChooser = new JFileChooser();
    private String fileName = "Untitled";
    private boolean saved =false;
    private Timer timer;
    private boolean autoSaveMode = false; // new variable to enable/disable autosave mode

    private JTextArea lineNumbers = new JTextArea("", 0, 0);
    private Component frame;
    private  JMenuBar menuBar;
    private JMenu fileMenu;
    
    public notepad() {
      
        this.setSize(800, 600);
        this.setTitle(fileName + " - notepad");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //  this.textArea.setFont(new Font("Arial", Font.PLAIN, 9));
        this.getContentPane().setLayout(new BorderLayout());
       
      
        lineNumbers.setEditable(false);
        lineNumbers.setBackground(Color.LIGHT_GRAY);
        lineNumbers.setPreferredSize(new Dimension(60, Integer.MAX_VALUE));
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
    textArea.setAlignmentY(Component.TOP_ALIGNMENT);
    JScrollPane scrollPane = new JScrollPane(textArea);
        

        // add the scroll pane to the main window
        add(scrollPane);

        scrollPane.setRowHeaderView(lineNumbers);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.getContentPane().add(scrollPane);
        this.createMenuBar();
        this.setVisible(true);
        
        
        // if (autoSaveMode) {
        //     this.timer = new Timer();
        //     this.timer.scheduleAtFixedRate(new TimerTask() {
        //         @Override
        //         public void run() {
        //             saveFile();
        //         }
        //     }, 0, 1 * 60 * 1000);
        // }
    
    ////////////////////////////////////////////////////////////////////////////////



    textArea.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
        
        public void insertUpdate(DocumentEvent e) {
            updateLineNumbers();
        }

        public void removeUpdate(DocumentEvent e) {
            updateLineNumbers();
        }
        public void changedUpdate(DocumentEvent e) {
            updateLineNumbers();
        }
    });
    Font defaultFont = new JTextArea().getFont();
    textArea.setFont(defaultFont);


}

    
    


    
    @SuppressWarnings("deprecation")
	private void createMenuBar() {
        menuBar = new JMenuBar();
         fileMenu = new JMenu("File");
        JMenuItem newFileItem = new JMenuItem("New");
        newFileItem.addActionListener(this);
        fileMenu.add(newFileItem);
        JMenuItem openFileItem = new JMenuItem("Open");
        openFileItem.addActionListener(this);
        fileMenu.add(openFileItem);
        JMenuItem saveFileItem = new JMenuItem("Save");
        saveFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveFileItem.addActionListener(this);
    
        fileMenu.add(saveFileItem);
        JMenuItem saveAsFileItem = new JMenuItem("Save As...");
        saveAsFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        saveAsFileItem.addActionListener(this);
        fileMenu.add(saveAsFileItem);
        // JCheckBoxMenuItem autoSaveItem = new JCheckBoxMenuItem("Autosave Mode");
        // autoSaveItem.addActionListener(this);
        // if(saved)
        // fileMenu.add(autoSaveItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutItem.setText("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        editMenu.add(cutItem);

        JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyItem.setText("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        editMenu.add(copyItem);

        JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteItem.setText("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        editMenu.add(pasteItem);


        // Add Undo option
// Create an undo manager for the text component
UndoManager undoManager = new UndoManager();
textArea.getDocument().addUndoableEditListener(undoManager);

// Add Undo option
JMenuItem undoItem = new JMenuItem("Undo");
undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
undoItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }
});
editMenu.add(undoItem);

// Add Redo option
JMenuItem redoItem = new JMenuItem("Redo");
redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
redoItem.addActionListener(new ActionListener() {
  @Override
    public void actionPerformed(ActionEvent e) {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }
});
editMenu.add(redoItem);

// Add Replace option
JMenuItem replaceItem = new JMenuItem("Replace");
replaceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
replaceItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String searchText = JOptionPane.showInputDialog(null, "Enter text to search:");
        if (searchText != null && !searchText.isEmpty()) {
            String replaceText = JOptionPane.showInputDialog(null, "Enter replacement text:");
            if (replaceText != null) {
                String text = textArea.getText();
                text = text.replace(searchText, replaceText);
                textArea.setText(text);
            }
        }
    }
});
editMenu.add(replaceItem);

    
        JMenuItem speak = new JMenuItem("Speech-to-Text");
       // speak.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        speak.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add code for Speak to Text functionality here
                Robot rb;
                try {
                    rb = new Robot();
                
				rb.keyPress(KeyEvent.VK_WINDOWS);
				rb.keyPress(KeyEvent.VK_H);
				rb.keyRelease(KeyEvent.VK_WINDOWS);
				rb.keyRelease(KeyEvent.VK_H);
                }
                catch (AWTException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        editMenu.add(speak);
        
        // JMenuItem find= new JMenuItem(new DefaultEditorKit.CopyAction());
        // find.setText("find");
       // copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        // editMenu.add(find);

////color
        JMenuItem UpperCase = new JMenuItem();
        UpperCase.setText("UpperCase");
       UpperCase.addActionListener(this);
       editMenu.add(UpperCase);

       JMenuItem lowerCase = new JMenuItem();
       lowerCase.setText("lowerCase");
       lowerCase.addActionListener(this);
      editMenu.add(lowerCase);
      
      JMenuItem capitalizeItem = new JMenuItem();
      capitalizeItem.setText("capitalizeItem");
      capitalizeItem.addActionListener(this);
     editMenu.add(capitalizeItem);
        menuBar.add(editMenu);
        JMenu formatMenu = new JMenu("Format");
        JMenuItem fontSizeItem = new JMenuItem("Font Size");
        // JMenuItem fontFormatItem = new JMenuItem("Font Format");
        fontSizeItem.addActionListener(this);
        // fontFormatItem.addActionListener(this);
        formatMenu.add(fontSizeItem);
        
   
//  formatMenu.add(fontFormatItem);
 JMenuItem fontColorItem = new JMenuItem("Font Color");
fontColorItem.addActionListener(new FontColorAction());
formatMenu.add(fontColorItem);

JMenuItem bgColorItem = new JMenuItem("Background Color");
bgColorItem.addActionListener(new BgColorAction());
formatMenu.add(bgColorItem);
       
       menuBar.add(formatMenu);
       this.setJMenuBar(menuBar);
        
       
    }
    private class FontColorAction extends AbstractAction {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FontColorAction() {
            putValue(Action.NAME, "Font Color");
        }
    
        public void actionPerformed(ActionEvent e) {
            Color color = JColorChooser.showDialog(null, "Choose Font Color", textArea.getForeground());
            if (color != null) {
                textArea.setForeground(color);
            }
        }
    }
    
    private class BgColorAction extends AbstractAction {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public BgColorAction() {
            putValue(Action.NAME, "Background Color");
        }
    
        public void actionPerformed(ActionEvent e) {
            Color color = JColorChooser.showDialog(null, "Choose Background Color", textArea.getBackground());
            if (color != null) {
                textArea.setBackground(color);
            }
        }
    }
    
    private void updateLineNumbers() {
        String text = textArea.getText();
        int lineCount = text.isEmpty() ? 1 : text.split("\n",-2).length;
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            builder.append(i).append("\n");
        }
        lineNumbers.setText(builder.toString());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New")) {
            this.newFile();
        } else if (e.getActionCommand().equals("Open")) {
            this.openFile();
            if(fileMenu.getItemCount()<6){
                saved=true;
                JCheckBoxMenuItem autoSaveItem = new JCheckBoxMenuItem("Autosave Mode");
        autoSaveItem.addActionListener(this);
        fileMenu.add(autoSaveItem);
            }
        } else if (e.getActionCommand().equals("Save")) {
        this.saveFile();
            if(fileMenu.getItemCount()<6){
                JCheckBoxMenuItem autoSaveItem = new JCheckBoxMenuItem("Autosave Mode");
        autoSaveItem.addActionListener(this);
        fileMenu.add(autoSaveItem);
            }
        }
        else if (e.getActionCommand().equals("UpperCase")) {
            this.UpperCase();
        } else if (e.getActionCommand().equals("lowerCase")) {
            this.lowerItem();
        }

        else if (e.getActionCommand().equals("capitalizeItem")) {
            this.capitalizeItem();
        }

        // else if (e.getActionCommand().equals("find")) {
        //     this.find();
        // }
        
        else if (e.getActionCommand().equals("Save As...")) {
            this.saveAsFile();
            if(fileMenu.getItemCount()<6){
                saved=true;
                JCheckBoxMenuItem autoSaveItem = new JCheckBoxMenuItem("Autosave Mode");
        autoSaveItem.addActionListener(this);
        fileMenu.add(autoSaveItem);
            }
        } else if (e.getActionCommand().equals("Autosave Mode")) {
            // Toggle autosave mode on/off when the "Autosave Mode" menu item is selected
            autoSaveMode = !autoSaveMode;
            if (autoSaveMode) {
                // Schedule a timer to periodically save the file if autosave mode is enabled
                           this.timer = new Timer();
                this.timer.scheduleAtFixedRate(new TimerTask() {
                 ;
                    @Override
                   
                  
                    public void run() {
                        saveFile();
                    }
                
                }, 0, 15*1000);
            } else {
                // Cancel the autosave timer if autosave mode is disabled
                this.timer.cancel();
            }
        } else if (e.getActionCommand().equals("Exit")) {
            this.exit();
        }
       else if (e.getActionCommand().equals("Font Size")) {
            int size = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter font size:"));
            Font font = textArea.getFont().deriveFont((float)size);
            Font font1= lineNumbers.getFont().deriveFont((float)size);
            textArea.setFont(font);
            lineNumbers.setFont(font1);
            
        }
        
    }

    
    private void lowerItem() {
        // String text = textArea.getText().toLowerCase();
        // textArea.setText(text);
        String selectedText=textArea.getSelectedText();
        String lowertext=selectedText.toLowerCase();
        textArea.replaceSelection(lowertext);
   
    }
        private void capitalizeItem() {
            String text = textArea.getText();
            StringBuilder result = new StringBuilder();
            boolean capitalizeNext = true;
            for (char ch : text.toCharArray()) {
                if (capitalizeNext && Character.isLetter(ch)) {
                    result.append(Character.toUpperCase(ch));
                    capitalizeNext = false;
                } else {
                    result.append(ch);
                }
                if (ch == '.') {
                    capitalizeNext = true;
                }
            }
            textArea.setText(result.toString());
       }
        
    private void UpperCase() {
        // String text = textArea.getText().toUpperCase();
        // textArea.setText(text);
        String selectedText=textArea.getSelectedText();
        String uppertext=selectedText.toUpperCase();
        textArea.replaceSelection(uppertext);
    }

private void newFile() {
    if (!saved) {
        int result = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + fileName + "?", "notepad", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (saveFile()) {
                //textArea.setText("");
                fileName = "Untitled";
                saved = true;
                this.setTitle(fileName + " - notepad");
            }
        } else if (result == JOptionPane.NO_OPTION) {
            textArea.setText("");
            fileName = "Untitled";
            saved = true;
            this.setTitle(fileName + " - notepad");
        }
    } else {
        textArea.setText("");
        fileName = "Untitled";
        saved = true;
        this.setTitle(fileName + " - notepad");
    }
}

        private void openFile() {
            if (!saved && textArea.getText().length()>0) {
                int result = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + fileName + "?", "notepad", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    if (!saveFile()) {
                        return;
                    }
                } else if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
        
            // Initialize file chooser
            fileChooser.setDialogTitle("Open File");
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                // Read contents of selected file
                File file = fileChooser.getSelectedFile();
                try {
                    FileReader reader = new FileReader(file);
                    BufferedReader br = new BufferedReader(reader);
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                    }
                    reader.close();
                    textArea.setText(sb.toString()); // Set contents of text area
                    fileName = file.getAbsolutePath();
                    saved = true;
                    this.setTitle(fileName + " - notepad");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error opening file " + file.getName() + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
    // @SuppressWarnings("unused")
	// private void openFileChooser() {
    //     int result = fileChooser.showOpenDialog(this);
    //     if (result == JFileChooser.APPROVE_OPTION) {
    //         File selectedFile = fileChooser.getSelectedFile();
    //         try {
    //             FileReader reader = new FileReader(selectedFile);
    //             textArea.read(reader, null);
    //             reader.close();
    //             fileName = selectedFile.getName();
    //             saved = true;
    //             this.setTitle(fileName + " - notepad");
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }  
    public boolean saveFile() {
        
        if (fileName.equals("Untitled")) {
            return saveAsFile();
        }
       
        else {
          
            try {
                File fileToSave = fileChooser.getSelectedFile();
                fileName = fileToSave.getAbsolutePath();
                FileWriter writer = new FileWriter(fileName);
                writer.write(textArea.getText());
                writer.close();
                saved = true;
                this.setTitle(fileName + " - notepad");
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file " + fileName + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
   
    
private boolean saveAsFile() {
    // Create a file filter that accepts both text and PDF and Java and Html files
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (.txt, *.text), PDF files (.pdf), Java files (.java), HTML files (.html)", "txt", "text", "pdf", "java", "html");

    fileChooser.setFileFilter(filter);
    fileChooser.setCurrentDirectory(new File("/path/to/desired/directory"));
    int returnVal = fileChooser.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        
        fileName = file.getName();
        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
            saved = true;
            this.setTitle(fileName + " - notepad");
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file " + fileName + ":\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        return false;
    }
}
    
 private void exit() {
        if (!saved) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + fileName + "?", "notepad", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (saveFile()) {
                    System.exit(0);
                }
            } else if (result == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        new notepad();
    }
}