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
		
	// The name of the environment within the configuration file that governs following access
	String environ="";
	
	// The directory folder holding the external configuration file, note that .resource folder name is added to this path
	String path = "./resources";
	
	// String holding absolute name of configuration file 
	String fn = "${path}/sample.config";
	
	/*
	 * A handle to the configSlurper object holding the text from the configuration file.
	 *
	 * Holds consumed JSON configuration payload from text found in the external configuration file
	 */
	ConfigObject configObject;
	
	
	/*
	 * default constructor mounts the configuration file, if it exist, pointing to the 'prod' environment
	 */
	public Configurator()
	{
		say "hello from Configurator.groovy"	
		
		// Configurator script for Configurator.groovy
		// see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
		configText = new File(fn).text
		setup('prod');
	} // end of default constructor
	
	
	/*
	 * Non-default path to the config resource; we add the /resources token to this value
	 */
	public Configurator(String dir)
	{
		say "hello from Configurator.groovy  path "+dir;	
		path=dir+"/resources";
		fn = "${path}/sample.config";

		// Configurator script for Configurator.groovy
		// see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
		configText = new File(fn).text
		setup('prod');
	} // end of non-default constructor
	
	
	
	// construct specific runtime environment
	public boolean setup(String e)
	{		
		// Create Configurator slurper and set environment to prod.
		configObject = createConfig(e).parse(configText)
	} // end of runner


	// internal method to show content of configObject 	
	def dump() { return "configObject="+configObject.toString(); }


	// convenience method	
	def say(txt) { println txt; }

	/*
	 * Helper closure to create a new ConfigSlurper for the given environment and
	 * register servers as section with Configurator per environment.
	 */
	def createConfig = { env ->
		say "createConfig for ${env}"
		environ = env;
    	def configSlurper = new ConfigSlurper(env)
    	configSlurper.registerConditionalBlock('servers', env)
    	return configSlurper  //object
	} // end of createConfig


	public  String get(String val)
	{
		return configObject.get(val);
	} // end of method
	
	// take Map and include that in the ConfigObject	
	public boolean overwrite(def m)
	{
		configObject.putAll(m)
	} // end of method

	// take key/value and include that in the ConfigObject	
	public boolean put(def k, def v)
	{
		configObject.put(k,v)
	} // end of method
	
	// take key and see if it's in our ConfigObject	
	public boolean has(def k)
	{
		configObject.containsKey(k)
	} // end of method
		
	public static void main(String[] args)
	{
		println "HELLO from Configurator.groovy"
		args.each{e-> println "arg="+e}
		Configurator c;
		if (args.size() > 0) { c = new Configurator(args[0]) } else { c = new Configurator(); }
		
		c.say "Goodbye from Configurator.groovy"
		c.say "--- the end ---"
	} // end of main 

} // end of class

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
		return (configObject.appName == 'local')?true:false;
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