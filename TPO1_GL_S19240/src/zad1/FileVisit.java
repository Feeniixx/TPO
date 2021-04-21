package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class FileVisit implements java.nio.file.FileVisitor<Path> {

    Charset excEncode  = Charset.forName("Cp1250");
    Charset necessaryEncode = Charset.forName("UTF-8");
    Path resFile;



    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{

        if (!file.toFile().isHidden()) {

            FileChannel fc = FileChannel.open(file);

            int size = (int)fc.size();

            ByteBuffer bf = ByteBuffer.allocate(size);

            fc.read(bf);
            bf .flip();
            fc.close();

            CharBuffer charBuffer = excEncode.decode(bf);
            bf  = necessaryEncode.encode(charBuffer);

            FileChannel channel = FileChannel.open(resFile, StandardOpenOption.APPEND);

            channel.write(bf);
            channel.close();

        }


        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(
            Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(
            Path dir, BasicFileAttributes attrs) {



        if (dir.toFile().isDirectory()){
            return FileVisitResult.CONTINUE;
        }

        else return FileVisitResult.TERMINATE;
    }


    public FileVisit(Path resultFile) {
        resFile = resultFile;
    }


}