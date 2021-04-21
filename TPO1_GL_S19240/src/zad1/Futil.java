package zad1;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {

        Path folder = FileSystems.getDefault().getPath(dirName);
        Path file = FileSystems.getDefault().getPath(resultFileName);

        try {

            Files.walkFileTree(folder,new FileVisit(file));

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if(file.toFile().delete()) {

            try {
                if( file.toFile().createNewFile()==true){
                    System.out.println("File was created");
                } else {
                    System.out.println("Something went wrong");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {

            System.out.println("File " + file.toFile().getName() + " can not create");
            try {

                if( file.toFile().createNewFile()==true){
                    System.out.println("File was created");
                } else {
                    System.out.println("Something went wrong");
                }

            } catch (IOException ex){
                ex.printStackTrace();
            }
        }


    }
}
