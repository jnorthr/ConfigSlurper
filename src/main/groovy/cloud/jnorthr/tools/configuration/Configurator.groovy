// need imports for JSON parsing bottom of this script
package cloud.jnorthr.tools.configuration;
import groovy.json.*
import static groovy.json.JsonParserType.*

/*
 * Features to consume application configuration variables
 */
public class Configurator{

    String configFile = System.getProperty("user.home") + File.separator  +".configurator.json";

    // Handle to confirm the external configuration file exists
    Checker ck;

    /*
     * Default constructor mounts the default configuration payload, pointing to the 'prod' environment
     * see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
     * to construct basic configObject from text in default config.file
     */
    public Configurator()
    {
        say "Default constructor Configurator.groovy"    
        ck = new Checker();
    } // end of default constructor
    
    
    /*
     * Non-default constructor mounts the configuration file, if it exist, pointing to the 'prod' environment
     */
    public Configurator(File fn)
    {
        say "Configurator.groovy constructor using a File"    
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
        ck = new Checker(fn);
    } // end of non-default constructor


    // convenience method    
    def say(txt) { println txt; }
    
        
    /*
     * Asks checker to return current value of the input path
     */
    public getInputPath()
    {
		ck.getInput('path')
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
		ck.getInputFileName();
	} // end of method
		        
    // =============================================================================    
    /**
     * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
     * 
     * argument is a list of strings provided as command-line parameters. 
     * Value 1 can be simple name of config.file without path, i.e. .fred.json
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
        } 
        else 
        {     
            println "... running default constructor"
            c = new Configurator(); 
            c.ck.putInput('file','.fred.json');
            println "... current input path is "+c.getInputPath()
            println "... current input file is "+c.getInputFile()
            println "... current input full filename is "+c.getInputFileName()
        }

        println "";
        c.say "... Goodbye from Configurator.groovy"
        c.say "--- the end ---"
    } // end of main 

} // end of class
