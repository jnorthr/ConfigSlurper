// need imports for JSON parsing bottom of this script
package cloud.jnorthr.tools.configuration;
import groovy.json.*
import static groovy.json.JsonParserType.*

/*
 * Features to consume application configuration variables
 */
public class Configurator{

  /**
    * This name points to a json structured configuration cache where user values are 
    * stored between sessions.  
    */
    String configFile = System.getProperty("user.home") + File.separator  +".configurator.json";

    // Handle to confirm the external configuration file exists
    Checker ck;

    // Holds text found in the external configuration file
    String configText="";
        
    /*
     * A handle to the configSlurper object holding the text from the configuration file.
     *
     * Holds consumed JSON configuration payload from text found in the external configuration file
     */
    ConfigObject configObject;
    
    // The name of the environment within the configuration file that governs following access
    String env="prod";
    
    /*
     * Default constructor mounts the default configuration payload, pointing to the 'prod' environment
     */
    public Configurator()
    {
        say "Default constructor Configurator.groovy"    
        ck = new Checker();
        configFile = ck.configFileName;
        configText = ck.payload;

        // see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
        // construct basic configObject from text in default config.file
        parse();
    } // end of default constructor
    
    
    /*
     * Non-default constructor mounts the configuration file, if it exist, pointing to the 'prod' environment
     */
    public Configurator(File fn)
    {
        say "Configurator.groovy constructor using a File"    
        configFile = fn.getAbsolutePath();
        say "... using ${configFile} file as input"
        
        ck = new Checker(configFile);
        configFile = ck.configFileName
        configText = ck.payload;

        parse();
    } // end of default constructor
    
    
    /*
     * Non-default constructor uses string name of a configuration file, if it exist, 
     * to create a ConfigObject
     */
    public Configurator(String fn)
    {
        say "Configurator.groovy constructor to read payload from file "+fn;    
        ck = new Checker(fn);
        configFile = ck.configFileName;
        configText = ck.payload;
        
        parse();
    } // end of non-default constructor


    /*
     * Construct specific configObject runtime environment for default 'prod' environment 
     */
    public ConfigObject parse()
    {        
        // Create Configurator slurper and set environment to prod.
        return parse(env); // 'prod'
    } // end of runner

    
    /*
     * Construct specific configObject runtime environment for provided environment string value
     */
    public ConfigObject parse(String e)
    {        
        // Create Configurator slurper and set environment to prod.
        say "... parse(${e})"
        env = e;
        configObject = createConfig(e).parse(configText);
        say "... config of ${env} now set to:"+dump();
        return configObject
    } // end of runner


    /*
     * internal method to show content of configObject     
     */
    String dump() { return "... configObject :"+configObject.toString(); }


    // convenience method    
    def say(txt) { println txt; }

    /*
     * Helper closure to create a new ConfigSlurper for the given environment and
     * register servers as section with Configurator per environment.
     */
     def createConfig = { e ->
        say "... createConfig for ${e}"
        env = e;
        /*
         * parses Groovy script into a ConfigObject - a subclass of LinkedHashMap that contains the configuration information.
         */
        def configSlurper = new ConfigSlurper(env);
        configSlurper.registerConditionalBlock('servers', env)
        return configSlurper  //object
    } // end of createConfig


    // find value for given key
    public String get(String key)
    {
        return configObject.get(key);
    } // end of method

    
    // take Map and include that in the ConfigObject    
    public boolean overwrite(def m)
    {
        configObject.putAll(m)
    } // end of method

    // take a single key/value and include that in the ConfigObject    
    public boolean put(m)
    {
        configObject.putAll(m)
        configText = configObject.toString() 
    } // end of method
    
    
    // take key and see if it's in our ConfigObject    
    public boolean has(def k)
    {
        configObject.containsKey(k)
    } // end of method
        
        
    // take each key and printit    
    public see()
    {
        configObject.each{e,v-> println "key:"+e+" v="+v;}
    } // end of method


    /*
     * Uses Checker save method to keep ALL text from payload
     */
    public save()
    {
        ck.save(configText);
    } // end of method
        
        
    /*
     * Serialize selected environment (only) to a writer ( not full original payload! )
     * then save to external UTF-8 file with provided filename 'fn'
     */
    public save(String fn)
    {
        def file3 = new File(fn)
        
        // Or a writer object:
        file3.withWriter('UTF-8') { writer ->
            configObject.writeTo(writer)
        }
        say "... saved to --->"+fn;
    } // end of method


    /*
     * Just save data string to external UTF-8 file with provided full filename 'fn' which must include path info
     */
    public save(String fn, String data)
    {
        ck.save(fn, data)
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
            println "\nloading configs from  args[0]="+args[0]+"\n"
            c = new Configurator(args[0]);
            println "Configurator made\n";
            println c.dump();
            c.parse('local');
            println "parse(local)\n";
            
            println c.dump();
        } 
        else 
        {     
            println "running default constructor"
            c = new Configurator(); 
            println c.dump();
        }

        println "";
        println "has 'data' key ? "+c.has('data');
        println "has 'appName' key ? "+c.has('appName');
        println "has 'hostname' key ? "+c.has('hostname');
        println "has 'appMode' key ? "+c.has('appMode');
        println "has 'email' key ? "+c.has('email');
        if (!c.has('email'))
        {
            c.put(['email':"jim@hotmail.com"]);
        }
        println "";
        println c.dump();
        c.see();
                
        c.save("bigsample.config"); // only saves values from chosen environment
        c.save();
        c.say "Goodbye from Configurator.groovy"
        c.say "--- the end ---"
    } // end of main 

} // end of class



//==============================================================
// spare code
/*
>>
>> import groovy.json.*
>>
>> def options = JsonOutput.options()
>>         .excludeNulls()
>>         .excludeFieldsByName('make', 'country', 'record')
>>         .excludeFieldsByType(Number)
>>         .addConverter(URL) { url -> '"http://groovy-lang.org"' }
>>
>> StringWriter writer = new StringWriter()
>> StreamingJsonBuilder builder = new StreamingJsonBuilder(writer, options)
>>
>> builder.records {
>>     car {
>>         name 'HSV Maloo'
>>         make 'Holden'
>>         year 2006
>>         country 'Australia'
>>         homepage new URL('http://example.org')
>>         record {
>>             type 'speed'
>>             description 'production pickup truck with speed of 271kph'
>>         }
>>     }
>> }
>>
>> assert writer.toString() == '{"records":{"car":{"name":"HSV
>> Maloo","homepage":"http://groovy-lang.org"}}}'



    public boolean tester1(def e)
    {
        say "tester1 --->"+e;
        // Create Configurator slurper and set environment to local using text in config var.
        configObject = createConfig(e).parse(configText)

        assert configObject.mail.host == 'greenmail'
        return (configObject.appMode == 'local')?true:false;
    } // end of runner



    // Try JSON parser
    public  boolean tester2(def env)
    {
        say "tester2 --->"+env;
        // -------------------------------
        // new JSON parser improvements
        def parser = new JsonSlurper().setType(LAX)

        def conf = parser.parseText '''
            // Configurator file
            {
                    // no quote for key, single quoted value
                env: 'production'
                # pound-style comment
                'server': 5
            }
'''
        say "parser config.env="+conf."${env}" // = production from prior json payload
        return (conf."${env}" == 'production')?true:false;
    } // end of method

*/