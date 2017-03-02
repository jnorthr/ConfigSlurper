// need imports for JSON parsing bottom of this script
package io.jnorthr.tools.configuration;
import groovy.json.*
import static groovy.json.JsonParserType.*
import io.jnorthr.toolkit.*;

/*
 * Features to consume application configuration variables
 */
public class Configurator{

    String homePath;
    
    String configFile = homePath +".configurator.json";

    // Handle to confirm the external configuration file exists
    Checker ck;


    /**
     * This is logic to get the name of the home folder used by this user.  
     */
    PathFinder pf = new PathFinder();
    

    /*
     * Default constructor mounts the default configuration payload, pointing to the 'prod' environment
     * see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
     * to construct basic configObject from text in default config.file
     */
    public Configurator()
    {
        say "Default constructor Configurator.groovy"    
        homePath = pf.getHomePath();
		
        ck = new Checker();
    } // end of default constructor
    
    
    /*
     * Non-default constructor mounts the configuration file, if it exist, pointing to the 'prod' environment
     */
    public Configurator(File fn)
    {
        say "Configurator.groovy constructor using a File"  
		homePath = pf.getHomePath();
		          
        String configFile = fn.getAbsolutePath();
        say "... using ${configFile} file as input"
        
        ck = new Checker(configFile);
    } // end of default constructor
    
    
    /*
     * Non-default constructor uses string name of a configuration file, if it exist, 
     * to create a ConfigObject
     */
    public Configurator(String fn)
    {
        say "Configurator.groovy constructor to read payload from file "+fn;    
		homePath = pf.getHomePath();
		
        ck = new Checker(fn);
    } // end of non-default constructor


    // convenience method    
    def say(txt) { println txt; }
    
    /*
     * Asks checker to return current value of given key
     */
    public get(String key)
    {
		ck.get(key)
	} // end of method
        
    // =============================================================================    

    /*
     * Asks checker to return current value of the input path
     */
    public getInputPath()
    {
		ck.getInput('path');
	} // end of method
	
	
    /*
     * Asks checker to return current value of the input file
     */
    public getInputFile()
    {
		ck.getInput('file')
	} // end of method
	
	
    /*
     * Asks checker to return current value of the input full filename
     */
    public getInputFileName()
    {
		return ck.getInput('path')+ck.getInput('file')
	} // end of method
	
    // =============================================================================    

    /*
     * Asks checker to return current value of the output path
     */
    public getOutputPath()
    {
		ck.getOutput('path')
	} // end of method
	
	
    /*
     * Asks checker to return current value of the output file
     */
    public getOutputFile()
    {
		ck.getOutput('file')
	} // end of method
	
	
    /*
     * Asks checker to return current value of the output full filename
     */
    public getOutputFileName()
    {
		return ck.getOutput('path')+ck.getOutput('file')
	} // end of method
	

    /*
     * Asks checker to store current value for given key
     */
    public putInput(String key, String va)
    {
		ck.putInput(key,va);
	} // end of method
		
		        
    // =============================================================================    
    /**
     * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
     * 
     * argument is a list of strings provided as command-line parameters. 
     * Value 1 can be simple name of config.file without path, i.e. .abcd.json
     * 
     * @param  args a list of possibly zero entries from the command line
     */
    public static void main(String[] args)
    {
        println "Hello from Configurator.groovy"

        args.eachWithIndex{e,ix-> println "arg${ix}="+e}
        
        Configurator c;
        if (args.size() > 0) 
        { 
            println "... loading configs from  args[0]="+args[0]+"\n"
            c = new Configurator(args[0]);
            println "Configurator made\n";
            println "... current input path is "+c.getInputPath()
            println "... current input file is "+c.getInputFile()
            println "... current input full filename is "+c.getInputFileName();
            
            c.ck.putOutput('file','.configuratorNonDefault.json');
            println "... current output path is "+c.getOutputPath()
            println "... current output file is "+c.getOutputFile()
            println "... current output full filename is "+c.getOutputFileName()
        } 

        else 
        {     
            println "... running default constructor"
            c = new Configurator(); 
            
            c.ck.putInput('file','.configuratorDefault.json');
            println "... current input path is "+c.getInputPath()
            println "... current input file is "+c.getInputFile()
            println "... current input full filename is "+c.getInputFileName()
            
            c.ck.putOutput('file','.configuratorDefault.json');
            println "... current output path is "+c.getOutputPath()
            println "... current output file is "+c.getOutputFile()
            println "... current output full filename is "+c.getOutputFileName()
        }

        println "";
        c.say "... Goodbye from Configurator.groovy"
        c.say "--- the end ---"
    } // end of main 

} // end of class
