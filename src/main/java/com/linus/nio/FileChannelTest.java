package com.linus.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    public static void main (String[] args) throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream (new File ("conf/log4j.properties"));
        FileChannel fileChannel = inputStream.getChannel ();
        FileOutputStream outputStream = new FileOutputStream (new File ("conf/new.properties"));
        FileChannel outFileChannel = outputStream.getChannel ();

        ByteBuffer buffer = ByteBuffer.allocate (1024);
        int num = fileChannel.read (buffer);
        buffer.flip ();
        System.out.println (new String(buffer.array ()).trim ());
        while (buffer.hasRemaining ()) {
            outFileChannel.write (buffer);
        }
    }
}
