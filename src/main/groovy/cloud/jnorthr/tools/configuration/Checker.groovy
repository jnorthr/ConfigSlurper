// need imports to parse JSON samples bottom of this script
package cloud.jnorthr.tools.configuration;

/*
 * Feature to confirm a configuration file exists or build a simple one if it does not
 */
public class Checker{

    /**
     * These default names points to a json structured configuration cache where user values are 
     * stored between sessions.  
     */
    String configPath     = System.getProperty("user.home") + File.separator;
    String configFile     = ".config.json";
    String configFileName = configPath + configFile;
    
    /**
     * The handle on a Json consumer 
     */
    ConfigSlurper config = new ConfigSlurper();
    
    /**
     * Map-like structure after using handle on a Json consumer 
     */
    groovy.util.ConfigObject dataObject;

    // Default JSON payloador JSON payload
    String payload = """setup {
  mail {
    host='smtp.myisp.com'
    auth.user='server'
  }
  input {
    path = '/Users/jimnorthrop/Dropbox/Projects/ConfigSlurper/resources/'
    file = 'MadMax6.txt'
    filename='/Users/jimnorthrop/Dropbox/Projects/ConfigSlurper/resources/MadMax6.txt'
  }
}""".toString();


    /*
     * Default constructor builds/or confirms existence of the default configuration file
     */
    public Checker()
    {
        say "Default constructor Checker.groovy"    
        config = new ConfigSlurper();  
        dataObject = config.parse(payload);

        read(configFileName)    
        save(configFileName,payload);
    } // end of default constructor
    
        
    /*
     * Non-default constructor uses string name of a configuration file, if it exist
     */
    public Checker(String fn)
    {
        configFileName = System.getProperty("user.home") + File.separator  + fn; 
        configFile = fn;   
        say "Checker.groovy called to confirm file "+configFileName+" exists";    
        config = new ConfigSlurper();  
        dataObject = config.parse(payload);

        read(configFileName)    
        save(configFileName,payload);
    } // end of non-default constructor


    // produce a text stream of the current payload in a man-readable format
    String prettyPrint()
    {
        return dataObject.prettyPrint().trim()+'\n';
    } // end of method
        
        
     
    // fill config text area from external file else default to skeleton
    public read(String fn)
    {    
        def fi = new File(fn);
         
        say "... read "+fn;
        if ( fi.exists() )
        {
            say "... config.file "+fn+" exists - getting it's text";
            payload = fi.getText("UTF-8");
            dataObject = config.parse(payload);
            println "------------------\npayload set to :"
            println payload
            println "------------------"
        }
        else
        {
            say "... config.file "+fn+" does not exists, will build it."
            save(fn,payload);
        }
    } // end of read    


    // convenience method    
    def say(txt) { println txt; }
        
    /*
     * Save a string of data to a writer as external UTF-8 file for already loaded file
     */
    public boolean save()
    {
        println "... default saving only";
        save(configFileName,payload);
    } // end of method
        

    /*
     * Save a string of data to a writer as external UTF-8 file for already loaded file
     */
    public boolean save(String data)
    {
        println "... saving data only";
        save(configFileName,data);
    } // end of method
        

    /*
     * Save a string of data to a writer as external UTF-8 file with provided filename 'fn'
     */
    public boolean save(String fn, String data)
    {
        say "... saving "+fn;

        // Or a writer object:
        new File(fn).withWriter('UTF-8') { writer ->
                writer.write(data)
        } // end of write    
        
        payload = data;
        dataObject = config.parse(payload);
        println "------------------\npayload set to :"
        println payload
        println "------------------"

        println "... saved ${data.size()} bytes to "+fn+'\n-----------------------\n';
        return true;
    } // end of method
    
    public getInput(String ky)
    {
        dataObject.with {
            setup.input.get(ky);
        }
    } // end of method


    public boolean putInput(String ky, String va)
    {
        say "putInput("+ky+","+va+")"
        dataObject.with {
            setup.input.put(ky,va);
        }
        payload = prettyPrint();
        say "putInput prettyPrint:"+payload
        save();
    } // end of method

    public boolean put(String ky, String va)
    {
        dataObject.with {
            setup.put(ky,va);
        }
        payload = prettyPrint();
        save();
    } // end of method

        
    // =============================================================================    
    /**
      * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
      * 
      * argument is a list of strings provided as command-line parameters. 
      * 
      * @param  args a list of possibly zero entries from the command line; first arg[0] if present, is
      *         taken as a simple file name of a json-structured configuration file;
      */
    public static void main(String[] args)
    {
        println "Hello from Checker.groovy"
        Checker ck;

        String cn = ".fred.json";
        ck = new Checker(cn);
        println "... file "+ck.configFileName+" contains "+ck.payload.size()+" bytes";
        ck.putInput('path',ck.configPath);
        ck.putInput('file',ck.configFile);
        ck.putInput('filename',ck.configPath+ck.configFile);
        println ck.payload;
        println "\n-----------------------\nprettyPrint:\n"+ck.prettyPrint();
        
/*        
        if (args.size() > 0) 
        { 
            println "\n... checking config.file = "+args[0]+"\n"
            String cfn = args[0];
            ck = new Checker(cfn);
            println "... file "+ck.configFileName+" contains "+ck.payload.size()+" bytes";
            ck.save(ck.configFileName, "Hi kids\n");
        } 
        else 
        { 
             println "... default constructor checking default config.name"
             ck = new Checker(); 
             println "... file "+ck.configFileName+" contains "+ck.payload.size()+" bytes";
             ck.putInput('file','.fred.json');
             println "path:"+ ck.getInput('path')
             ck.putInput('filename',ck.getInput('path') + '.fred.json');
        }
*/
        ck.say "--- the end ---"        
    } // end of main 

} // end of class