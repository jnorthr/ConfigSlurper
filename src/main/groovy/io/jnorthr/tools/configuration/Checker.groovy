// need imports to parse JSON samples bottom of this script
package io.jnorthr.tools.configuration;

/*
 * Feature to confirm a groovy configuration script file exists or build a simple one if it does not
 */
public class Checker{

    /**
     * These default names point to a groovy script-structured configuration cache where user values are stored between sessions.  
     */
    String configPath     = System.getProperty("user.home") + File.separator;


    /**
     * This name points to a groovy script-structured configuration name of a file where user values are stored between sessions.  
     */    
     String configFile     = "checker.config";

    /**
     * This full file name, including path and filename, points to a groovy script-structured configuration file where user values are stored between sessions.  
     */
    String configFileName = configPath + configFile;

    
    /**
     * The handle on a groovy ConfigSlurper
     */
    ConfigSlurper config = new ConfigSlurper();
    
    
    /**
     * Map-like structure after using handle on a groovy script consumer 
     */
    groovy.util.ConfigObject dataObject;


    // Default groovy script config payload
    String payload = """setup {
  output {
    path = "${configPath}"
    file = "${configFile}"
  }
  input {
    path = "${configPath}"
    file = "${configFile}"
  }
}""".toString();


    /*
     * Default constructor builds/or confirms existence of the default configuration file
     */
    public Checker()
    {
        say "Default constructor Checker.groovy"    
        config = new ConfigSlurper();  

        try{
	        dataObject = config.parse(payload);
	    }
	    catch (Exception e)
	    {
		    println "... parse() exception due to malformed groovy script payload:"+e.message
	    	throw new RuntimeException(e.message) 
	    } // end of catch

        read(configFileName)    
        save(configFileName,payload);
    } // end of default constructor
    
        
    /*
     * Non-default constructor consumes a string name of a configuration file, excluding path, if it exist
     */
    public Checker(String fn)
    {
        configFile = fn;   
        configFileName  = configPath + configFile;
        
        say "Checker.groovy called to confirm file "+configFileName+" exists";    
        config = new ConfigSlurper();  

        try{
	        dataObject = config.parse(payload);
	    }
	    catch (Exception e)
	    {
		    println "... parse() exception due to malformed groovy script payload:"+e.message
	    	throw new RuntimeException(e.message) 
	    } // end of catch

        read(configFileName)    
        save(configFileName,payload);
    } // end of non-default constructor


    // produce a text stream of the current payload in a man-readable format
    String prettyPrint()
    {
        return dataObject.prettyPrint().trim()+'\n';
    } // end of method
        
        
     
    // fill config text area from external file, if it exists, else default to skeleton
    public read(String fn)
    {    
        def fi = new File(fn);
         
        say "... read "+fn;
        if ( fi.exists() )
        {
            say "... config.file "+fn+" exists - getting it's text";
            payload = fi.getText("UTF-8");
	        try{
		        dataObject = config.parse(payload);
	    	}
		    catch (Exception e)
		    {
		    	println "... parse() exception due to malformed groovy script payload:"+e.message
	    		throw new RuntimeException(e.message) 
	    	} // end of catch
	    	
            println "------------------\npayload set to :"
            println payload
            println "------------------"
        }
        else
        {
            say "... groovy config script "+fn+" does not exists, will build it."
            save(fn,payload);
        }
    } // end of read    


    // convenience log method    
    def say(txt) { println txt; }
        
    // ----------------------------------------------------------------------------------

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
        println "... saving script data only";
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
        try{
	        dataObject = config.parse(payload);
	    }
	    catch (Exception e)
	    {
		    println "... parse() exception due to malformed groovy script payload:"+e.message
	    	throw new RuntimeException(e.message) 
	    }
	    
        println "------------------\npayload set to :"
        println payload
        println "------------------"

        println "... saved ${data.size()} bytes to "+fn+'\n-----------------------\n';
        return true;
    } // end of method
    
    // ----------------------------------------------------------------------------------
        
        
    // take key and see if it's in our groovy ConfigSlurper ConfigObject.setup.input sub-map    
    public boolean hasInput(def k)
    {
        dataObject.with{
        	setup.input.containsKey(k)
        }
    } // end of method
     
     
    // use 'path' key and get it's value from our ConfigObject.setup.input map    
    public getInputPath()
    {
		return getInput('path');
    } // end of method


    // use 'file' key and get it's value from our ConfigObject.setup.input map    
    public getInputFile()
    {
		return getInput('file');
    } // end of method


    // use two keys to form a resulting whole filename    
    public getInputFileName()
    {
		return getInputPath()+getInputFile();
    } // end of method

     
    // take key and get it's value from our ConfigObject.setup.input map    
    public getInput(String ky)
    {
        dataObject.with {
            setup.input.get(ky);
        }
    } // end of method


    // ----------------------------------------------------------------------------------
        
        
    // take key and see if it's in our groovy ConfigSlurper ConfigObject.setup.output sub-map    
    public boolean hasOutput(def k)
    {
        dataObject.with{
        	setup.output.containsKey(k)
        }
    } // end of method
     
     
    // use 'path' key and get it's value from our ConfigObject.setup.output map    
    public getOutputPath()
    {
		return getOutput('path');
    } // end of method


    // use 'file' key and get it's value from our ConfigObject.setup.output map    
    public getOutputFile()
    {
		return getOutput('file');
    } // end of method


    // use two keys to form a resulting whole filename    
    public getOutputFileName()
    {
		return getOutputPath()+getOutputFile();
    } // end of method

     
    // take key and get it's value from our ConfigObject.setup.output map    
    public getOutput(String ky)
    {
        dataObject.with {
            setup.output.get(ky);
        }
    } // end of method


    // ----------------------------------------------------------------------------------
     
    // take key and get it's value from our ConfigObject.setup map ROOT    
    public get(String ky)
    {
        dataObject.with {
            setup.get(ky);
        }
    } // end of method


	// This method inserts new properties at the ROOT of our ConfigSlurper object tree
    // take key and insert this va into our ConfigObject.setup{} map root - NOT into the 'input' sub-groovy script closure 
    public boolean put(String ky, String va)
    {
        dataObject.with {
            setup.put(ky,va);
        }
        payload = prettyPrint();
        save();
    } // end of method


	// This method inserts new objects, i.e. dates,numbers,etc at the ROOT of our ConfigSlurper object tree
    // take key and insert this va into our ConfigObject.setup{} map root - NOT into the 'input' sub-groovy script closure 
    public boolean put(String ky, Object va)
    {
        dataObject.with {
            setup.put(ky,va);
        }
        payload = prettyPrint();
        save();
    } // end of method


    // ----------------------------------------------------------------------------------
     

    // take key and insert this va into our ConfigObject.setup.input map sub-root    
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


    // take key and insert this va into our ConfigObject.setup.output map sub-root    
    public boolean putOutput(String ky, String va)
    {
        say "putOutput("+ky+","+va+")"
        dataObject.with {
            setup.output.put(ky,va);
        }
        
        payload = prettyPrint();
        say "putOutput prettyPrint:"+payload
        save();
    } // end of method

        
    // =============================================================================    
    /**
      * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
      * 
      * argument is a list of strings provided as command-line parameters. 
      * 
      * @param  args a list of possibly zero entries from the command line; first arg[0] if present, is
      *         taken as a simple file name of a groovy script-structured configuration file;
      */
    public static void main(String[] args)
    {
        println "Hello from Checker.groovy"
        Checker ck;

		// sample test of alternate .config filename
        String cn = ".checkerAlternate.config";
        ck = new Checker(cn);
        println "... file "+ck.configFileName+" contains "+ck.payload.size()+" bytes";
        
        ck.putInput('path',ck.configPath);
        ck.putInput('file',ck.configFile);
        ck.putOutput('path',ck.configPath);
        ck.putOutput('file',ck.configFile);
        
        println "... has setup.input.path ? " + ck.hasInput('path');
        println ck.payload;
        
        println "... getInputPath()    : " + ck.getInputPath()
        println "... getInputFile()    : " + ck.getInputFile()
        println "... getInputFileName(): " + ck.getInputFileName()
        println "... getOutputPath()    : " + ck.getOutputPath()
        println "... getOutputFile()    : " + ck.getOutputFile()
        println "... getOutputFileName(): " + ck.getOutputFileName()
        
        println "\n-----------------------\nprettyPrint:\n"+ck.prettyPrint();
        println "\n-----------------------\n"


		// normal test flow of groovy script.config filename with a single argument from the build.gradle script
        if (args.size() > 0) 
        { 
            println "\n... checking .config = "+args[0]+"\n"
            String cfn = args[0];
            ck = new Checker(cfn);
            println "... file "+ck.configFileName+" contains "+ck.payload.size()+" bytes";
            ck.save(ck.configFileName, "setup { }");
        } 
        else 
        { 
             println "... default constructor checker.config"
             ck = new Checker(); 

             println "... file "+ck.configFileName+" contains "+ck.payload.size()+" bytes";
             
             ck.putInput('file','.default.config');
             println "path:"+ ck.getInput('path')

             ck.putOutput('file','.default.config');
             println "path:"+ ck.getOutput('path')
             
			 // insert key into root of ConfigObject tree
             ck.put("mobile","07855769321"); 
             ck.save();
             
             ck.prettyPrint();
        }
        
        ck.say "--- the end ---"        
    } // end of main 

} // end of class