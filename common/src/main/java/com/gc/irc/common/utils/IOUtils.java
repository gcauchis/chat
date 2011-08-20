package com.gc.irc.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;

/**
 * The Class IOUtils.
 */
public class IOUtils {

	/**
	 * Byte to char.
	 *
	 * @param buffer the buffer
	 * @param size the size
	 * @return the char[]
	 */
	public static char[] byteToChar(byte[] buffer, int size){
		if(size > buffer.length){
			size = buffer.length;
		}
		char[] bufferChar = new char[size];

		for (int i = 0 ; i < size ; i++){
			bufferChar[i] = (char)buffer[i];
		}

		return bufferChar;
	}

	/**
	 * Byte to string.
	 *
	 * @param buffer the buffer
	 * @param size the size
	 * @return the string
	 */
	public static String byteToString(byte[] buffer, int size){
		if(size > buffer.length){
			size = buffer.length;
		}
		return String.valueOf(byteToChar(buffer, size), 0, size);
	}

	/**
	 * Function wich converts a text file into a String
	 * Can be useful to read a configuration file for example.
	 *
	 * @param f The file we want to read
	 * @return A string containing all the content of the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public static String loadFile(File f) throws IOException {
       BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
       StringWriter out = new StringWriter();
       int b;
       while ((b=in.read()) != -1)
           out.write(b);
       out.flush();
       out.close();
       in.close();
       return out.toString();
    }
    
    /**
     * Write a file.
     *
     * @param nameFile File's path.
     * @param content String to write.
     */
	public static void ecritFichier(String nameFile, String content) {
		File file = new File(nameFile);
		
		PrintStream lPrintStream = null;
		FileOutputStream lFos = null;
		try
		{
			lFos = new FileOutputStream(file, false);
			lPrintStream = new PrintStream(new BufferedOutputStream(lFos));
			lPrintStream.println(content);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (lPrintStream != null)
				{
					lPrintStream.flush();
					lPrintStream.close();
				}
				if (lFos != null)
				{
					lFos.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
}
