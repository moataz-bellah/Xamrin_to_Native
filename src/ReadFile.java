import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ReadFile {
    public static String Swiftname;
    public void IosFileWrite(String file)
    {
        try {
            File swiftTojava = new File(Swiftname);
            System.out.println(Swiftname.substring(Swiftname.indexOf('.')+1));

            if (Swiftname.substring(Swiftname.indexOf('.')+1).startsWith("swift"))
            {
                System.out.println(Swiftname.substring(Swiftname.indexOf('.')+1));
                swiftTojava = new File("Android"+Swiftname.substring(0,Swiftname.indexOf('.'))+".java");
            }
            else if (Swiftname.substring(Swiftname.indexOf('.')+1).startsWith("storyboard"))
            {
                swiftTojava = new File("Android"+Swiftname.substring(0,Swiftname.indexOf('.'))+".xml");
            }
            else {
                swiftTojava = new File("Android" + Swiftname);
            }

        if(!swiftTojava.exists()) {

            swiftTojava.createNewFile();
        }
        PrintWriter files = new PrintWriter(swiftTojava);
        files.println(file);
        files.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    public String IosFileRead(String filepath){
        String Javacode = "";
        try {
            File Read = new File(filepath);
            Scanner scan = new Scanner(Read);
            while(scan.hasNext())
            {
                Javacode = Javacode.concat(scan.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Javacode;
    }

    public String chooseFile()
    {
        String path = "";
        JButton open = new JButton();
        JFileChooser choose = new JFileChooser();
        choose.setCurrentDirectory((new java.io.File("/Users/asuigp/Desktop")));
        choose.setDialogTitle("Choose a file");
        if (choose.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
            //
        }
        path =choose.getSelectedFile().getAbsolutePath();
        Swiftname = choose.getSelectedFile().getName();

        return path;
    }
}
