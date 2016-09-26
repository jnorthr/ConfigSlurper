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
	 * A handle to the configSlurper object holding the text from the configuration file
	 */
	def configHandle
	
	/*
	 * default constructor mounts the configuration file, if it exist, pointing to the 'prod' environment
	 */
	public Configurator()
	{
		say "hello from Configurator.groovy"	
		
		// Configurator script for Configurator.groovy
		// see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
		configText = new File(fn).text
		configHandle = createConfig('prod').parse(configText)
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
		configHandle = createConfig('prod').parse(configText)
	} // end of default constructor
	
	
	def say(txt) { println txt; }

	// Helper closure to create a new Configurator.groovy for the given environment and
	// register servers as section with Configurator per environment returning new Configurator object.
	def createConfig = { env ->
		say "createConfig for ${env}"
		environ = env;
    		def configSlurper = new ConfigSlurper(env)
    		configSlurper.registerConditionalBlock('servers', env)
    		configSlurper
	} // end of createConfig

	//
	public boolean runner(String e)
	{
		say "runner environment="+e;
		environ = e;
		// Create Configurator slurper and set environment to prod.
		configHandle = createConfig(e).parse(configText)

		say "prod Configurator.mail.host="+configHandle.mail.host
		assert configHandle.mail.host == 'mail.server'

		say "prod configHandle.appName="+configHandle.appName
		return (configHandle.appName == 'production')?true:false;
	} // end of runner


	public boolean tester1(def e)
	{
		say "tester1 --->"+e;
		// Create Configurator slurper and set environment to local using text in config var.
		configHandle = createConfig(e).parse(configText)

		assert configHandle.mail.host == 'greenmail'
		return (configHandle.appName == 'local')?true:false;
	} // end of runner

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

	public  String getVal(String val)
	{
		return configHandle."${val}";
	} // end of method
	
	public static void main(String[] args)
	{
		println "HELLO from Configurator.groovy"
		args.each{e-> println "arg="+e}
		Configurator c;
		if (args.size() > 0) { c = new Configurator(args[0]) } else { c = new Configurator(); }
		if (c.runner('prod'))
		{
			c.say "Configurator.appName == 'production' ? ="+c.runner('prod');
		} // end of if

		c.say "getting email="+c.getVal('email');
		
		c.say "tester1()="+c.tester1('local');
		c.say "getting email="+c.getVal('email');

		// read from internal JSon payload - not config file
		c.say "tester2()="+c.tester2('env');
		
		c.say "Goodbye from Configurator.groovy"
		c.say "--- the end ---"
	} // end of main 
} // end of class