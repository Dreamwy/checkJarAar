package com.dreamwy.unityutil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class CheckUtil
{

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        List<String> messages = getConfig(args);
        if(messages.size() == 0) {
        	System.out.println("didn't find the same aar or jar");
        }
        for (String string : messages)
        {
            System.err.println(string);
        }
    }

    public static List<String> getClassNamesFromJar(String path) throws IOException
    {
        List<String> classNames = new ArrayList<String>();
        ZipFile zipFile = new ZipFile(new File(path));
        for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
        {
        	ZipEntry entry = (ZipEntry)e.nextElement();
        	if(entry.getName().endsWith(".jar")) {
        		InputStream  is = zipFile.getInputStream(entry);
        		ZipInputStream zis  = new ZipInputStream(is);
        		for(ZipEntry zentry = zis.getNextEntry();zentry != null;zentry=zis.getNextEntry()) {
        			if (!zentry.isDirectory() && zentry.getName().endsWith(".class"))
                    {
                        String className = zentry.getName().replace('/', '.');
                        classNames.add(className.substring(0, className.length() - ".class".length()));
                    }
        		}
        		is.close();
        		zis.close();
        	}
            if (!entry.isDirectory() && entry.getName().endsWith(".class"))
            {
                String className = entry.getName().replace('/', '.');
                classNames.add(className.substring(0, className.length() - ".class".length()));
            }
            
        }
        zipFile.close();

        return classNames;
    }
    
   
    
    
    public static List<String> getConfig(String... files)
    {
        List<String> messages = new ArrayList<String>();
        
        if(files == null || files.length < 2)
            return messages;
        
        HashMap<String, String> hashMap = new HashMap<String, String>();
        
        for (String file : files)
        {
            try
            {
                List<String> classNames = getClassNamesFromJar(file);

                for (String name : classNames)
                {
                    if(hashMap.containsKey(name))
                    {
                        messages.add(name + "\n" + file + "\n" + hashMap.get(name));
                        break;
                    }
                        
                    hashMap.put(name, file);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        return messages;
    }
}