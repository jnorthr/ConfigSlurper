// need imports for JSON parsing bottom of this script
package cloud.jnorthr.tools.configuration;
import groovy.json.*
import static groovy.json.JsonParserType.*
/*
 * Features to consume application configuration variables
 */
public class Configurator{

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
	
	// The directory folder holding the external configuration file, note that .resource folder name is added to this path
	String path = "./resources";
	
	// String holding absolute name of configuration file 
	String fn = "${path}/sample.config";
	
	// Default JSON payload
	def payload = """// Environments section.
environments {
    dev {
        mail.hostname = 'local'
    }
    test {
        mail.hostname = 'test'
    }
    uat {
        mail.hostname = 'uat'
    }
    qa {
        mail.hostname = 'qa'
    }
    live {
        mail.hostname = 'live'
    }
    prod {
        mail.hostname = 'prod'
        appMode = 'production'
    }
}
""";

	
	/*
	 * Default constructor mounts the default configuration payload, pointing to the 'prod' environment
	 */
	public Configurator()
	{
		say "hello from default constructor Configurator.groovy"	
		
		// see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
		// construct basic configObject from above 'payload'
		configText = payload.toString();
		parse();
	} // end of default constructor
	
	
	/*
	 * this constructor mounts the configuration file, if it exist, pointing to the 'prod' environment
	 */
	public Configurator(File fn)
	{
		say "hello from Configurator.groovy using a File"	
		
		configText = fn.text
		configText = payload.toString();
		parse();
	} // end of default constructor
	
	
	/*
	 * Non-default string of filename of external configuration file
	 */
	public Configurator(String fn)
	{
		say "hello from Configurator.groovy loading payload from file "+fn;	
		configText = new File(fn).text;
		parse();
	} // end of non-default constructor

	
	
	// construct specific runtime environment from external file
	public ConfigObject load(String fn)
	{	
		configText = new File(fn).text;	
		return parse();
	} // end of load
	

	// construct specific configObject runtime environment
	public ConfigObject parse()
	{		
		// Create Configurator slurper and set environment to prod.
		return parse(env); // 'prod'
	} // end of runner

	
	// construct specific configObject runtime environment
	public ConfigObject parse(String e)
	{		
		// Create Configurator slurper and set environment to prod.
		configObject = createConfig(e).parse(configText);
		return configObject
	} // end of runner


	// internal method to show content of configObject 	
	String dump() { return "configObject="+configObject.toString(); }


	// convenience method	
	def say(txt) { println txt; }

	/*
	 * Helper closure to create a new ConfigSlurper for the given environment and
	 * register servers as section with Configurator per environment.
	 */
	 def createConfig = { e ->
		say "createConfig for ${e}"
		env = e;
    	def configSlurper = new ConfigSlurper(env);
    	configSlurper.registerConditionalBlock('servers', env)
    	return configSlurper  //object
	} // end of createConfig


	// find value for given key
	public String get(String val)
	{
		return configObject.get(val);
	} // end of method

	
	// take Map and include that in the ConfigObject	
	public boolean overwrite(def m)
	{
		configObject.putAll(m)
	} // end of method

	// take a single key/value and include that in the ConfigObject	
	public boolean put(def k, def v)
	{
		configObject.put(k,v)
	} // end of method
	
	
	// take key and see if it's in our ConfigObject	
	public boolean has(def k)
	{
		configObject.containsKey(k)
	} // end of method
		
		
	// Serialize selected environment to a writer ( not full original payload! )
	// then save to external UTF-8 file with provided filename 'fn'
	public save(String fn)
	{
		say "save to --->"+fn;
		def file3 = new File(fn)
		// Or a writer object:
		file3.withWriter('UTF-8') { writer ->
			configObject.writeTo(writer)
    	}
	} // end of method
		
		
	public static void main(String[] args)
	{
		println "Hello from Configurator.groovy"
		args.eachWithIndex{e,ix-> println "arg${ix}="+e}
		Configurator c;
		if (args.size() > 0) 
		{ 
			println "running with args[0]="+args[0]
			c = new Configurator(args[0]);
		} 
		else 
	    { 
	    	println "running default constructor"
		 	c = new Configurator(); 
		}
		
		c.save("groovy3.config"); // only saves values from chosen environment
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