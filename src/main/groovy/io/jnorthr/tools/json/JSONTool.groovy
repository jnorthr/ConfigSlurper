package io.jnorthr.tools.json;
import groovy.json.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput;
import static groovy.json.JsonParserType.LAX as RELAX
import static groovy.json.JsonParserType.INDEX_OVERLAY
// http://grails.asia/groovy-xmlslurper-examples-for-parsing-xml[Groovy Code Fragments]
/*
http://grails.asia/groovy-create-xml-document-examples[CML Docs Create]
http://grails.asia/groovy-map-tutorial[Map Tutorial]
Map Replace Values
sampleMap['color'] = 'Red'
sampleMap.'shape' = 'Rectangle'
sampleMap.weight = 200
sampleMap.put('thickness', 5)

sampleMap.remove('color') // delete from Map
sampleMap.get('color') // lookup
map1.keySet() // get keys as a list[]
map1.values() // get values as a List[]
*/

/*
 * Feature to maintain JSON formatted text files, that they exist and/or build a simple one if it does not
 */
public class JSONTool
{
    /**
     * This variable holds name of user home directory.  
     */
    final static String homePath = System.getProperty("user.home") + File.separator;


    /**
     * This variable holds complete name of JSON-formatted text file.  
     */
    String fn = homePath+".config.json";
        
	Map json;
	
    /*
     * Default constructor builds/or confirms existence of the default json file
     */
    public JSONTool()
    { 
        loader();
    } // end of constructor
    
    
    /*
     * Non-Default constructor builds/or confirms existence of an alternate json file
     *
     * @param  alternateFileName has name of JSON-styled text file
     */
    public JSONTool(String alternateFileName)
    { 
        fn = homePath + alternateFileName;
        loader();
    } // end of constructor
    
    
    /**
     * Indirect method to print to sysout 
     */
    def say = System.out.&println;

    
    /*
     *  check if a json property exists in a json-formatted text file structure
     */
    boolean has(String key)
    {
        return (json.get(key))?true:false;    
    } // end of hasPath
    
    
    
    /*
     * Return a property identified by the key, if it exists in a json-formatted text file structure
     *
     * @param  key provides the identity of a property to return that key's current value
     */
    def get(String key)
    {
        return json.get(key);   
    } // end of getFileName
    
    
    /*
     *  Populate a property, within a json-formatted text file structure
     *
     * @param  key provides the identity of a property to store in this json-formatted text file structure
     * @param  va provides an actual object to store for future access by this same key
     */
    def set(String key, String va )
    {
        json.put[key] = va; 
        update("Updated ${key}");  
    } // end of setProperty


    /*
     *  re-write a json-formatted text file structure
     *
     * @param  reason adds a remark to the comments within the json-formatted text file structure
     */
    def update(String reason)
    {
        try 
        {            
            File file = new File(fn);
            FileOutputStream fileOut = new FileOutputStream(file);
            json.store(fileOut, reason);
            fileOut.close();            
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } // end of catch        
    } // end of loader


    /*
     *  pull in a json-formatted text file
     */
    def loader()
    {
        try 
        {
            //load a json-formatted text file structure
            def jsonText = new FileInputStream(fn).text;
            //json = new JsonSlurper().setType(RELAX).parseText(jsonText)
            def parser = new JsonSlurper().setType(JsonParserType.LAX)
      		json = (Map)parser.parseText(jsonText)
      		assert json instanceof Map
	        println "--- json instanceof Map ---"
        } 
        catch (IOException ex) 
        {
            update("Initial Build");
            say "missing json file ${fn} has been created"
        } // end of catch
        
        println "--- end of loader() ---"
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
        println "-- starting JSONTool() ---"
            
        println "-- load JSON-formatted text file ---"    
        def jt = new JSONTool();

        println "has="+jt.has("girl");
        if (jt.has("path")) { jt.say "path="+jt.get("path"); }

        jt.set("path",System.getProperty("user.home") + File.separator);
        
        if (jt.has("path")) { jt.say "now path="+jt.get("path"); }
        
        println "has filename="+jt.has("filename");
        if (jt.has("filename")) { jt.say "filename="+jt.get("filename"); }

        jt.set("filename",".jsonTool.json");
        if (jt.has("filename")) { jt.say "now filename="+jt.get("filename"); }
        
        jt = new JSONTool(".jnorthr.json");
        if (jt.has("path")) { jt.say "now path="+jt.get("path"); }

        jt.set("alias","jnorthr");
        def alias = jt.get("alias");  
        println "alias="+alias;
              
        println "-- the end ---"    
    } // end of method
    
} // end of class    