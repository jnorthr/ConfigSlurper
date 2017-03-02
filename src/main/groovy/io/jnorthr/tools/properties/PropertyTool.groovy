package io.jnorthr.tools.properties;
import io.jnorthr.toolkit.PathFinder;

/*
 * Feature to maintain java properties files, that they exist and/or build a simple one if it does not
 */
public class PropertyTool
{
    /**
     * This variable holds name of user home directory.  
     */
    String homePath;
    

    /**
     * This variable holds complete name of user property file.  
     */
    String fn = homePath+".config.properties";
        
    /**
     * This variable is a handle to user home folder.  
     */
	PathFinder pf = new PathFinder();

    /**
     * This variable holds handle to a java-formatted Property file.  
     */
    Properties prop = new Properties();

    /*
     * Default constructor builds/or confirms existence of the default configuration file 'resources/.log.properties'
     */
    public PropertyTool()
    { 
        homePath = pf.getHomePath();
        loader();
    } // end of constructor
    
    /*
     * Non-Default constructor builds/or confirms existence of an alternate properties file
     *
     * @param  reason adds a remark to the comments with the property file
     */
    public PropertyTool(String alternateFileName)
    { 
        homePath = pf.getHomePath();
        fn = homePath + alternateFileName;
        loader();
    } // end of constructor

    
    /**
     * Indirect method to print to sysout 
     */
    def say = System.out.&println;
    
    /*
     *  check if a path property exists in a java property file
     */
    boolean hasPath()
    {
        return prop.containsKey("path");    
    } // end of hasPath
    
    
    /*
     *  return a path property, if it exists in a java property file
     */
    String getPath()
    {
        return prop.getProperty("path");   
    } // end of getPath
    
    
    /*
     *  populate a path property, if it exists in a java property file
     *
     * @param  va provides a local file folder name to use as a path, but excludes the actual filename
     */
    def setPath(String va)
    {
        prop.setProperty("path",va); 
        this.update("Updated path 3");  
    } // end of setPath

    
    /*
     *  check if a filename property exists in a java property file
     */
    boolean hasFileName()
    {
        return prop.containsKey("filename");    
    } // end of hasFileName
    
    
    /*
     *  return a filename property, if it exists in a java property file
     */
    String getFileName()
    {
        return prop.getProperty("filename");   
    } // end of getFileName
    
    
    /*
     *  populate a filename property, if it exists in a java property file
     *
     * @param  va provides a local file name to use for the name of the property, but excludes the actual path to the filename
     */
    def setFileName(String va)
    {
        prop.setProperty("filename",va); 
        this.update("Updated filename");  
    } // end of setPath

    
    /*
     *  return a property identified by the key, if it exists in a java property file
     *
     * @param  key provides the identity of a property to return that key's current value
     */
    def getProperty(String key)
    {
        return prop.getProperty(key);   
    } // end of getFileName
    
    
    /*
     *  populate a property, within a java property file
     *
     * @param  key provides the identity of a property to store in this property file
     * @param  va provides an actual object to store for future access by this same key
     */
    def setProperty(String key, String va )
    {
        prop.setProperty(key,va); 
        update("Updated ${key}");  
    } // end of setProperty


    /*
     *  re-write a java property file
     *
     * @param  reason adds a remark to the comments within the property file
     */
    def update(String reason)
    {
        try 
        {            
            File file = new File(fn);
            FileOutputStream fileOut = new FileOutputStream(file);
            prop.store(fileOut, reason);
            fileOut.close();            
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } // end of catch        
    } // end of loader


    /*
     *  pull in a java property file
     */
    def loader()
    {
        try 
        {
            //load a properties file
            prop.load(new FileInputStream(fn));
        } 
        catch (IOException ex) 
        {
            this.setPath(homePath)
            update("Initial Build");
            say "missing property file ${fn} has been created"
        } // end of catch
    } // end of loader


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
        println "-- starting PropertyTool() ---"
            
        println "-- load java properties config ---"    
        def ct = new PropertyTool();

        println "hasPath="+ct.hasPath();
        if (ct.hasPath()) { ct.say "path="+ct.getPath(); }

        ct.setPath(System.getProperty("user.home") + File.separator);
        
        if (ct.hasPath()) { ct.say "now path="+ct.getPath(); }
        
        
        println "hasFileName="+ct.hasFileName();
        if (ct.hasFileName()) { ct.say "filename="+ct.getFileName(); }

        ct.setFileName(".walker.properties");
        if (ct.hasFileName()) { ct.say "now filename="+ct.getFileName(); }
        
        ct = new PropertyTool(".jnorthr.properties");
        if (ct.hasPath()) { ct.say "now path="+ct.getPath(); }

        ct.setProperty("alias","jnorthr");
        def alias = ct.getProperty("alias");  
        println "alias="+alias;
              
        println "-- the end ---"    
    } // end of method
    
} // end of class    