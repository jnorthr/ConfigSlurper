package io.jnorthr.tools.configuration;
import io.jnorthr.toolkit.PathFinder;

/*
	See: http://groovy.codehaus.org/ConfigSlurper
    or http://mrhaki.blogspot.fr/2009/08/grassroots-groovy-configuration-with.html

	Groovy ConfigSlurper reads groovy scripts styled like JSON but more flexible; these are different from Java properties files
	or JSON formatted text. The result of a parse() method is a ConfigObject which is a kind of Map.
	
	- log.properties
    see: http://blog.andresteingress.com/2013/10/24/groovy-quick-tip-the-groovy-util-logging-package/
    log.info "running ConfigTool() default constructor"
    //new File('/Volumes/FHD-XS/TextEditor/TextEditor/editordata/log.properties').toURL()



// Had to write as a class as configSlurper won't work as script
// ConfigSlurper is a utility class within Groovy for writing properties file like scripts for performing configuration. 
// Unlike regular Java properties files
// ConfigSlurper scripts support native Java types and are structured like a tree.
// Below is an example of how you could configure Log4j with a ConfigSlurper script:
import groovy.util.ConfigSlurper;
import java.net.*;
import java.util.*;
import groovy.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.awt.*
import javax.swing.JOptionPane;

//import groovy.util.logging.Slf4j
//import groovy.util.logging.*
//import groovy.util.logging.Log4j
//import org.apache.log4j.Level

/*
 * Feature to maintain groovy configuration script files, that they exist and/or build a simple one if it does not
 */
//@Slf4j(value = 'LOGGER')
public class ConfigTool
{
    /**
     * These default names point to a json-structured configuration cache where user values are stored between sessions.  
     */
    String homePath;
    
    /**
     * Skeleton groovy config script to use if no external file exists or cannot be found 
     */
    def defaultScript = """logging {
log4j.appender.stdout = "org.apache.log4j.ConsoleAppender"
log4j.appender."stdout.layout"="org.apache.log4j.PatternLayout"
log4j.rootLogger="error,stdout"
log4j.logger.org.springframework="info,stdout"
log4j.additivity.org.springframework=false
}

path='${homePath}'
// Dot notation.
mail.hostname = 'localhost' 
 
// Scoped closure notation.
mail { 
    // Using Groovy constructs.
    ['user', 'password'].each {
        this."${it}" = 'secret'
    }
}
 
// Environments section.
environments {
    dev {
        mail.hostname = 'local'
    }
    test {
        mail.hostname = 'test'
    }
    prod {
        mail.hostname = 'prod'
    }
}
""".toString();

    /**
     * Indirect method to print to sysout 
     */
    def say = { System.out.&println;  }
    
    
    /**
     * Full name of the groovy script configuration environment file with path 
     */
    String es = homePath+'resources/.environment.config';


    /**
     * File handle using full name of the environment script with path
     */
    File ef = new File(es);


    /**
     * The canonicalPath name of currently open environment file pointed to by 'ef'
     */
    def e = ef.toURI().toURL();

    
    /**
     * Full name of the java properties log file with path
     */
    String us = 'resources/.log.properties';


    /**
     * File handle to full name of the log file configuration settings with path
     */
    File uu = new File(homePath + us)


    /**
     * The canonicalPath name of currently open file pointed to by 'uu'
     */
    def u = uu.toURI().toURL();
    
    
    /**
     * Handle to groovy tool to eat a script and produce a ConfigObject
     * see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
     */
    ConfigSlurper slurper = new ConfigSlurper(); 


    /**
     * Handle to the configObject resulting from parsing the groovy script of properties
     */
    ConfigObject configObject  = slurper.parse(u); // parsed configObject content


    /**
     * canonicalPath name of currently open config file pointed to by config
     */
    String canonicalConfigFileName 
    
    
    /**
     * filename of currently open config file pointed to by config
     */
    String currentConfigFileName 
    

    /**
     * This is logic to get the name of the home folder used by this user.  
     */
    PathFinder pf = new PathFinder();
    

    /*
     * Default constructor builds/or confirms existence of the default configuration file 'resources/.log.properties'
     */
    public ConfigTool()
    { 
		homePath = pf.getHomePath();
		
        // To load this into a readable configObject you can do:
        if (uu.exists())
        {
            configObject = slurper.parse(u);
            currentConfigFileName = uu.canonicalPath
            say "currentConfigFileName="+currentConfigFileName
        }
        else
        {
            showPanel("Missing Config File","${uu.name} does not exist - cannot continue");
            // display the showOptionDialog
            int choice = JOptionPane.showOptionDialog(null, 
                  "Do you want to create ${uun} ?", 
                  "Build it ?", 
                  JOptionPane.YES_NO_OPTION, 
                  JOptionPane.QUESTION_MESSAGE, 
                  null, null, null);

            // interpret the user's choice
            if (choice == JOptionPane.NO_OPTION)
            {
                say "execution terminated due to missing ${nm}"
                assert true == nmh.exists()
            }            
            else
            {
                newConfig(uun)
                configObject = slurper.parse(defaultScript);
            } // end of else
            
        } // end of else        
        
    } // end of constructor
    


    /*
     * Non-Default constructor builds/or confirms existence of a non-default configuration file
     * place content from external groovy property script into configObject variable
     * flag=true if string has name of url/uri
     *
     * @param  fn  a filename+optional local folder name w/the base location of the config script but without path like 'resources/.log.properties'
     * @param  flag true causes runtime exception if file missing 
     */
    public ConfigTool(String fn, boolean flag)
    {
		homePath = pf.getHomePath();

        String nm = homePath + fn;
        // To load this into a readable config you can do:
        def nmh = new File(nm)
        if (nmh.exists())
        {
            configObject = slurper.parse(nmh.text);
        }
        else
        {
            showPanel("Missing File","${nm} does not exist - cannot continue");

            // display the showOptionDialog
            int choice = JOptionPane.showOptionDialog(null, 
                  "Do you want to create ${nm} ?", 
                  "Build it ?", 
                  JOptionPane.YES_NO_OPTION, 
                  JOptionPane.QUESTION_MESSAGE, 
                  null, null, null);

            // interpret the user's choice
            if (choice == JOptionPane.NO_OPTION)
            {
                say "execution terminated due to missing ${nm}"
                assert true == nmh.exists()
            }            
            else
            {
                newConfig(nm)
                nmh = new File(nm);
                configObject = slurper.parse(defaultScript);
            } // end of else
        } // end of else
        
        currentConfigFileName = nmh.canonicalPath
        say "currentConfigFileName="+currentConfigFileName
        def nmf = nmh.toURI().toURL();
        configObject = slurper.parse(nmf);
    } // end of constructor
    

/*
Special "environments" Configuration

The groovy script ConfigSlurper class has a special constructor other than the default constructor that takes an "environment" parameter. 

This special constructor works in concert with a property setting called environments. This allows a default setting to 
exist in the property file that can be over-written by a setting in a specific environment closure. This allows multiple 
related configurations to be stored in the same file. 

Note: the environments closure is not directly parsable. Without using the special environment constructor the closure is ignored.
*/

    /*
     * Non-Default constructor builds/or confirms existence of a specific environment in the default groovy configuration script file
     *
     * @param  env  identifies the environment to influence which set of settings are in force; like 'live','test', "development" 
     */
    public ConfigTool(String env)  
    { 
		homePath = pf.getHomePath();

        // To load this into a readable config you can do:
        slurper = new ConfigSlurper(env)
        configObject = slurper.parse(e);
        currentConfigFileName = ef.canonicalPath
        say "currentConfigFileName="+currentConfigFileName

        assert configObject.sample.foo == "dev_foo"
        assert configObject.sample.bar == "default_bar"
        
        slurper = new ConfigSlurper("test")
        configObject = slurper.parse(e);
        assert configObject.sample.foo == "default_foo";
        assert configObject.sample.bar == "test_bar"      
    } // end of constructor 


    /*
     *  You can merge ConfigSlurper configs to make a single config object. For example:    
     */
    def mergeConfigs()
    {
        def configObject1 = new ConfigSlurper().parse(u)
        def configObject2 = new ConfigSlurper().parse(e)
        configObject = configObject1.merge(configObject2)    
        currentConfigFileName = uu.canonicalPath
        say "currentConfigFileName="+currentConfigFileName
    } // end of merge


    /*
     * Put a new set of properties into the config using Stringed Map as a parm 
     *     
     * @param  na  configObject key within the current configObject
     * @param  stuffmap    the 'value' to insert with this key into the current configObject
     */
    def setProperty(String na, String stuffmap)
    {
        configObject."$na" = stuffmap
    } // end of put    
    

    /*
     * @see Sample method - work in progress !
     * put a new set of properties into the configObject using Map as a parm      
     *
     * @param  stuffmap    Map of 'values' to insert with this key into the current configObject
     */
    def setProperty(Map stuffmap)
    {
        configObject << ['key':'value','font.size':71.4]
        configObject.putAll( ['key3':'value3','colors.tall':7])
    } // end of put    
    

    /*
     * You can write ConfigSlurper configObjects to files. For example:    "resources/.fred.properties"
     *
     * @param  newConfigFileName String value of full path+filename of new output file
     */
    def newConfig(String newConfigFileName)
    {
        def nh = new File(newConfigFileName); 
        if (nh.exists())
        {
            say "this property file already exists:"+newConfigFileName;
            //currentConfigFileName = nh.canonicalPath
            showPanel("Decline to Over-write","""${newConfigFileName} already exists - Will NOT over-write it,\nso keeping ${currentConfigFileName}\nas properties""".toString());
            return true
        } // end of if

        try
        {	
        	say "... newConfig(${newConfigFileName}) writeTo"
            new File(newConfigFileName).withWriter { writer ->
                configObject.writeTo(writer)
            } // end of write

            //currentConfigFileName = nh.canonicalPath
            //say "newConfig created currentConfigFileName="+currentConfigFileName
            return true
        } 
        catch (IOException iox) 
        { 
            return false; 
        }
    } // end of method



    /*
     *  You can write ConfigSlurper configObjects to files. 
     *  For example:    "resources/.fred.properties"
     */
    def writeConfig()
    {
        writeConfig(currentConfigFileName)    
    } // end of method


    /*
     *  You can write ConfigSlurper configObjects to files. 
     *  For example:    "resources/.fred.properties"
     *
     * @param  newConfigFileName String value of full path+filename of new output file
     */
    def writeConfig(String filename)
    {        
        //def configObject = new ConfigSlurper().parse(u)
        new File(filename).withWriter { writer ->
             configObject.writeTo(writer)
        } // end of write

        def nh = new File(filename);
        currentConfigFileName = nh.canonicalPath
        say "writeConfig to currentConfigFileName="+currentConfigFileName
    } // end of method



    /*
     *  You can rewrite different ConfigSlurper configObjects back to file. 
     *  this method writes a different configObject to the same file we opened 
     *
     * @param  newConfigFileName String value of full path+filename of new output file
     */
    def rewriteConfig(String rewriteConfigFileName)
    {
        currentConfigFileName  = rewriteConfigFileName
        writeConfig(currentConfigFileName);
        say "rewriteConfig to currentConfigFileName="+currentConfigFileName
    } // end of method


    /*
     *  this method rewrites the same configObject to the same file we opened 
     */
    def rewriteConfig()
    {
        writeConfig(currentConfigFileName);
        say "rewriteConfig to currentConfigFileName="+currentConfigFileName
    } // end of method


    /*
     *  pull in a java property file
     */
    def loader()
    {
        // P.S If file path is not specified, then this properties file will be stored in your project root folder.
        Properties prop = new Properties();
 
        try 
        {
            //load a properties file
            prop.load(new FileInputStream("resources/.config.properties"));
            def nh = new File("config.properties")
            currentConfigFileName = nh.canonicalPath
            say "java props currentConfigFileName="+currentConfigFileName
 
            //get the property value and print it out
            say(prop.getProperty("database"));
            say(prop.getProperty("dbuser"));
            say(prop.getProperty("dbpassword"));
            
            //For non-static method, use this :
            //prop.load(getClass().getClassLoader().getResourceAsStream("resources/.config.properties"););
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } // end of catch
        
    } // end of loader
    

    /*
     *  convenience method to set a property within configObject
     *     
     * @param  k  configObject key within the current config
     * @param  v  an object to insert with this key into the current configObject
     */
    public void set(String k, def v)
    {
        setProperty(k,v);
    } // end of method


    
    /*
     *  convenience method  
     *     
     * @param  k  configObject key within the current config
     * @return value of 'k' key in configObject if ti exists
     */
    public get(String k)
    {
        return configObject."$k";
    } // end of method


    /*
     *  report bad news
     *     
     * @param  title  text of dialog top line - the title line.
     * @param  msg    holds text of information dialog
     */
    public showPanel(String title, String msg)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE );                                           
    } // end of method

    
        
    // =============================================================================    
    /**
      * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
      * 
      * argument is a list of strings provided as command-line parameters. 
      * 
      * @param  args a list of possibly zero entries from the command line; 
      */
    public static void main(String[] args)
    {
        println "-- starting ConfigTool() ---"
            
        println "-- test default config ---"    
        def ct = new ConfigTool();

        println "-- add font ---"    
        def stuffmap = 'courrier'
        ct.set('font.face',stuffmap)

        println "-- add color ---"    
        ct.set('colors.red','#ff0000')
        println "   color now="+ct.get('colors.red');
        
        
        println "\n-- test resources/.fred.config ---"    
        def fn = "resources/.fred.config"
        ct = new ConfigTool(fn,true);
        println "-- set fred to blue --"

        // may blitz ant pre-existing property of same name    
        ct.setProperty('colors.red','#ff0000')
        println "-- write config to ${fn} ---"    
        ct.writeConfig(fn)        

        println "-- end of default testing --"

        println "-- do it again testing --"
        ct = new ConfigTool(fn,true);
        println "-- add fred ---"    
        ct.set('fred','Mertz')

        println "-- set fred to blue --"
        // may blitz ant pre-existing property of same name    
        ct.setProperty('textcolor','#000000')

        println "   fred now="+ct.get('fred');

        println "-- write config to ${fn} ---"    
        ct.writeConfig()        
        println "   fred now="+ct.get('fred');

        println "\n-- load config for 'development' ---"    
        ct = new ConfigTool("development");

        println "\n-- loaded configObject for 'development' ---"    
        println "   fred now="+ct.get('fred');


        println "\n-- test resources/.freetext.config ---"    
        fn = "resources/.freetext.config"
        ct = new ConfigTool(fn,true);
        ct.set('font.face','courier')
        println "   font.face now="+ct.get('font.face');
        ct.writeConfig()        
    

        println "\n-- test resources/.sometext.config ---"    
        fn = ct.homePath+"resources/.sometext.config"
        new File(fn).delete()
        if ( ct.newConfig(fn) )
        {
            ct.set('name','value 1')
            ct.rewriteConfig(fn)                
            ct.set('environment', "[name:bloggs]" )
            ct.rewriteConfig()                
        } // end of if

        println "-- the end ---"    
    } // end of method
    
} // end of class