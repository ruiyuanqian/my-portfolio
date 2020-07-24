package com.google.sps.servlets;

import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import com.google.sps.servlets.DataServlet.MemeRecord;

public class FTLSingleton {

    Configuration cfg;
    private static FTLSingleton instance = new FTLSingleton();

    private FTLSingleton(){
        try
        {
            String classesPath = FTLSingleton.class.getResource("/").getPath();

            this.cfg = new Configuration(Configuration.VERSION_2_3_30);

            cfg.setDirectoryForTemplateLoading( new File( classesPath ) );
            cfg.setDefaultEncoding("UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static FTLSingleton getInstance(){
        return instance;
    }

    public void render_staticPart( Writer aWriter , String templateName , String partName )
    {
        try 
        {
            String fullPathName = "./templates/" + templateName + "_" + partName + ".ftl";

            Template aTemplate = this.cfg.getTemplate( fullPathName );
            // An example to load header:
            // Template aTemplate = this.cfg.getTemplate("./templates/allMemes_header.ftl");
            
            Map<String, Object> args = new HashMap<String, Object>();

            aTemplate.process(args, aWriter);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (TemplateException e)
        {
            e.printStackTrace();
        }
    }

    public void render_allMemes_aMeme( Writer aWriter , MemeRecord aMeme)
    {
        try 
        {
            Template aTemplate = this.cfg.getTemplate("./templates/allMemes_aMeme.ftl");
            
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("userEmail", aMeme.userEmail);
            args.put("timestamp", DateFormat.getDateTimeInstance().format(aMeme.timestamp) );
            args.put("comment", aMeme.comment);
            args.put("url", aMeme.url);

            aTemplate.process(args, aWriter);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (TemplateException e)
        {
            e.printStackTrace();
        }
    }

}
