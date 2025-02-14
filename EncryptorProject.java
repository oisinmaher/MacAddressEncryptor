    /*what i could improve
        there is lots of reduntant code with JPanel, 
        if i had a method for displaying jpanel and the parameters had all the varying info (message, title etc)
        would make code cleaner and easier to read

        Make a copy to clipboard an option

        Motherboards (more specifically NIC) of the same model share the first 6 digits of mac address
        Add option to encrypt and decrpyt messages on motherboard of same models.
    */
    
    import java.awt.Toolkit;
    import java.awt.datatransfer.StringSelection;
    import java.net.*; // for mac
    import java.util.Enumeration; // for mac
    import javax.swing.ImageIcon; // so image icon can be stored in an object that java prefers
    import javax.swing.JOptionPane; // for pop up meesage
    public class EncryptorProject { 
        public static void main(String[] args) throws Exception{
            String macAddress = getMacAddress();
            if(macAddress == null){
                System.out.println("Null Mac Address, not working");
                System.exit(0);
            }
            ImageIcon icon = new ImageIcon("icon.jpg"); // creates image icon object and assigns icon picture
            boolean runApplication = true;
            while(runApplication){
                JOptionPane.showMessageDialog(
                    null,
                    "Welcome to Oisin's Message Enryptor using MAC Address as Key",
                    "Oisin's Encryptor",
                    0,
                    icon
                );
                String[] mainOptions = {"Encrypt", "Decrypt", "Cancel"};
                int choice = JOptionPane.showOptionDialog(
                    null, // null appears in center of screen
                    "Would you like to Encrypt or Decrypt a message?", //display message
                    "Message Encryptor", // title of window
                    JOptionPane.NO_OPTION, // no special button types
                    JOptionPane.NO_OPTION, // no picture
                    icon, // icon
                    mainOptions, // list of options (the array)
                    mainOptions[2] // default selection
                );
                String[] finalOptions = {"Home", "Exit"};
                switch(choice){
                    case 0 -> {
                        String encryptedMessage = userEncrypt();
                        // Assigns the message to an object that makes the String transferable
                        StringSelection transferableMessage = new StringSelection(encryptedMessage);
                        // Assigns the message to the system clipboard
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferableMessage, null);
                        String outputMessage = "Your encrypted message is '" + encryptedMessage + "'\nIt has been copied to your clipboard";
                        int option = JOptionPane.showOptionDialog(
                            null,
                            outputMessage,
                            "Encrypted Message",
                            JOptionPane.NO_OPTION, // no special button types
                            JOptionPane.NO_OPTION, // no picture
                            icon, // icon
                            finalOptions,
                            finalOptions[1]
                        );
                        if(option == 1){
                            runApplication = false;
                        }
                    }
                    case 1 -> {
                        String decryptedMessage = userDecrypt();
                        StringSelection transferableMessage = new StringSelection(decryptedMessage);
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferableMessage, null);
                        String outputMessage = "Your decrypted message is '" + decryptedMessage + "'\nIt has been copied to your clipboard";
                        int option = JOptionPane.showOptionDialog(
                            null,
                            outputMessage,
                            "Decrypted Message",
                            JOptionPane.NO_OPTION, // no special button types
                            JOptionPane.NO_OPTION, // no picture
                            icon, // icon
                            finalOptions,
                            finalOptions[1]
                        );
                        if(option == 1){
                            runApplication = false;
                        }
                    }
                    default -> {
                        System.exit(0);
                    }
                }
            }
        }
        public static String encryptor(String messageToBeEncrypted) throws Exception{
            String macAddress = getMacAddress();
            StringBuilder encryptedMessageBuilder = new StringBuilder();
            int directionOfMac = -1, pointOfMac = 11;
            int directionOfExp = -1, exponent = 4;
            String unformattedEncryption;
            for(int i = 0; i < messageToBeEncrypted.length(); i++){
                unformattedEncryption = (Integer.toString((
                    (int) (messageToBeEncrypted.charAt(i) + ( //Character of message that needs to be converted plus
                    Integer.parseInt(String.valueOf(macAddress.charAt(pointOfMac)), 16) //The Integer value of mac Adress current index
                    * Math.pow(2, exponent)))// multiplied by 2 to the power of current exponent
                ),36)
                );
                if(unformattedEncryption.length()<2){
                    unformattedEncryption = "0" + unformattedEncryption;
                }
                encryptedMessageBuilder.append(unformattedEncryption);
                 
                // mac
                if(!(pointOfMac+directionOfMac >= 0 && pointOfMac+directionOfMac <= 11)){ // checks if the direction is not within bounds
                    directionOfMac*=-1; // if not multiplis direction by -1 (makes -1 to 1 and 1 to -1) changing its course
                }
                pointOfMac+=directionOfMac; // inc/decrements location of mac address
                // exponent
                if(!(exponent+directionOfExp >= 0 && exponent+directionOfExp <= 4)){ // checks if the direction is not within bounds
                    directionOfExp*=-1;// if not multiplis direction by -1 (makes -1 to 1 and 1 to -1) changing its course
                }
                exponent+=directionOfExp;
            }
            return encryptedMessageBuilder.toString();
        }
        public static String decryptor(String messageToBeDecrypted) throws Exception{
            String macAddress = getMacAddress();
            StringBuilder decryptedMessageBuilder = new StringBuilder();
            int directionOfMac = -1, pointOfMac = 11;
            int directionOfExp = -1, exponent = 4;
            for(int i = 0; i < messageToBeDecrypted.length(); i+=2){
                StringBuilder encryptedNumberSB = new StringBuilder();
                //Coverts string to int for each encrypted characters code number
                encryptedNumberSB.append(messageToBeDecrypted.charAt(i));
                encryptedNumberSB.append(messageToBeDecrypted.charAt(i+1));
                
                int encryptedNumber = Integer.parseInt(encryptedNumberSB.toString(), 36);
                // converts the encrypted number of a character back to character form
                decryptedMessageBuilder.append(
                    (char) (encryptedNumber- // the encrypted number subtract
                    Integer.parseInt(String.valueOf(macAddress.charAt(pointOfMac)), 16) //The Integer value of mac Adress current index
                    * Math.pow(2, exponent)// multiplied by 2 to the power of current exponent
                    )
                ); 

                // mac
                if(!(pointOfMac+directionOfMac >= 0 && pointOfMac+directionOfMac <= 11)){ // checks if the direction is not within bounds
                    directionOfMac*=-1; // if not multiplis direction by -1 (makes -1 to 1 and 1 to -1) changing its course
                }
                pointOfMac+=directionOfMac; // inc/decrements location of mac address
                // exponent
                if(!(exponent+directionOfExp >= 0 && exponent+directionOfExp <= 4)){ // checks if the direction is not within bounds
                    directionOfExp*=-1;// if not multiplis direction by -1 (makes -1 to 1 and 1 to -1) changing its course
                }
                exponent+=directionOfExp;
            }
            return decryptedMessageBuilder.toString();
        }
        // gets mac address
        public static String getMacAddress() throws Exception {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if (mac != null) {
                    StringBuilder macAddress = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        macAddress.append(String.format("%02X%s", mac[i], ""));
                    }
                    return macAddress.toString();
                }
            }
            // if error return null
            return null;
        }
        public static String userEncrypt() throws Exception{
            ImageIcon icon = new ImageIcon("icon.jpg"); // creates image icon object and assigns icon picture
            String  messageToBeEncrypted = (String) JOptionPane.showInputDialog(
                null,
                "Enter The String you want to Encrypt",
                "Enter Message",
                0,
                icon,
                null,
                ""
            );
            String chosenString = "You chose '" + messageToBeEncrypted + "'";
            JOptionPane.showOptionDialog(
                null,
                chosenString,
                "Chosen Message",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                icon,
                new String[]{"Encrypt"},
                "Encrypt"
            );
            String encryptedMessage = encryptor(messageToBeEncrypted);
            return encryptedMessage;
        }
        public static String userDecrypt() throws Exception{
            ImageIcon icon = new ImageIcon("icon.jpg"); // creates image icon object and assigns icon picture
            String  messageToBeDecrypted = (String) JOptionPane.showInputDialog(
                null,
                "Enter The String you want to Decrypt",
                "Enter Encrypted Message",
                0,
                icon,
                null,
                ""
            );
            String chosenString = "You chose to decrypt '" + messageToBeDecrypted + "'";
            JOptionPane.showOptionDialog(
                null,
                chosenString,
                "Chosen Message",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                icon,
                new String[]{"Decrypt"},
                "Decrypt"
            );
            String decryptedMessage = decryptor(messageToBeDecrypted);
            return decryptedMessage;
        }
    }
