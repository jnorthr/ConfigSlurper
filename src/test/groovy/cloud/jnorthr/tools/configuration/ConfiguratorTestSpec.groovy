package cloud.jnorthr.tools.configuration;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import cloud.jnorthr.tools.configuration.Configurator;
//import CacheEntry;
//import groovy.transform.Canonical
//import groovy.transform.ToString
import java.util.logging.Logger;
import spock.lang.*
//import javax.swing.JFileChooser;

class ConfigutationTestSpec extends Specification 
{
  // fields
  boolean flag = false;
  Configurator ch;
  	    
  //static Logger log = Logger.getLogger(CacheManagerTestSpock.class.getName());

  // Fixture Methods
  
  // run before every feature method
  def setup() 
  { 
  	  ch = new Configurator(); 
  }          

  // run after every feature method
  def cleanup() {}
  
  // Note: The setupSpec() and cleanupSpec() methods may not reference instance fields.
  def setupSpec() {}     // run before the first feature method
  def cleanupSpec() {}   // run after the last feature method}

/*
Feature methods are the heart of a specification. They describe the features (properties, aspects) that you expect to find in the system under specification. By convention, feature methods are named with String literals. Try to choose good names for your feature methods, and feel free to use any characters you like!

Conceptually, a feature method consists of four phases:

 . Set up the feature's fixture
 . Provide a stimulus to the system under specification
 . Describe the response expected from the system
 . Clean up the feature's fixture
 . Whereas the first and last phases are optional, the stimulus and response phases are always present (except in interacting feature methods), and may occur more than once.

*/

  // Feature Methods

  // First Test
  def "1st Test: Setup Configurator for default path"() {
    given:
 
    when:
		flag = runner('prod');
 
    then:
		// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	flag == true;
  } // end of test


  // 2nd Test
  def "2nd Test: Set Configurator initial folder to unknown path"() {
    given:
 
    when:
		ch = new Configurator("/Fred");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	def e = thrown(java.io.FileNotFoundException)
	    e.cause == null
  }

  // 3rd Test
  def "3rd Test: Ask appName for 'prod' environment"() {
    given:
        
    when:
		boolean  yn = runner('prod')
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	yn == true;
		ch.get('appName') == 'production'
  } // end of test
  

  // 4th Test
  def "4th Test: Ask app for 'local' mail.host"() {
    given:
        
    when:
		boolean  yn = ch.setup('local')
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	yn == true;
 		ch.get('email') == 'james.northrop@orange.fr'
  } // end of text
  
  
  // Fifth Test
  def "5th Test: Setup Configurator to use 'prod' environment"() {
    given:
 
    when:
		flag = runner('prod');
 
    then:
		// Asserts are implicit and not need to be stated.
    	flag == true;
		println "getting email="+ch.get('email');
		ch.get('email') == "james.b.northrop@googlemail.com"
  } // end of test

    
  // Sixth Test
  def "6th Test: Setup Configurator to use 'local' environment"() {
    given:
 		
    when:
		flag = ch.setup('local');
 
    then:
		// Asserts are implicit and not need to be stated.
    	flag == true;
		println "getting email="+ch.get('email');
		ch.get('email') == "james.northrop@orange.fr"
  } // end of test


  // Seventh Test
  def "7th Test: Confirm dump() of configObject yields correct values"() {
    given:
		ch.dump() == "configObject=[data:[domain:cloud.jnorthr.tools, name:jnorthr], mail:[host:mail.server], email:james.b.northrop@googlemail.com, appName:production]";
 		
    when:
		def devMap = ['name':'Roberto', 'framework':'Grails', 'language':'Groovy']
		ch.overwrite(devMap); 
 
    then:
		println ch.dump();
		ch.dump() == "configObject=[data:[domain:cloud.jnorthr.tools, name:jnorthr], mail:[host:mail.server], email:james.b.northrop@googlemail.com, appName:production, name:Roberto, framework:Grails, language:Groovy]"
  } // end of test


  // Eighth Test
  def "8th Test: Put a new 'alias' key entry into the config"() {
  		println "8th Test: Put a new 'alias' key entry into the config"
  		
    given:
		println ch.dump();
		ch.dump() == "configObject=[data:[domain:cloud.jnorthr.tools, name:jnorthr], mail:[host:mail.server], email:james.b.northrop@googlemail.com, appName:production]" 		
    when:
		ch.put('alias','jnorthr');
		 
    then:
		println ch.dump();
		ch.dump() == "configObject=[data:[domain:cloud.jnorthr.tools, name:jnorthr], mail:[host:mail.server], email:james.b.northrop@googlemail.com, appName:production, alias:jnorthr]"
  } // end of test



  // Nineth Test
  def "9th Test: Put a new 'alias' key entry into the config then check HAS()"() {
  		println "9th Test: Put a new 'alias' key entry into the config then check HAS()"
  		
    given:
		println ch.dump();
		ch.dump() == "configObject=[data:[domain:cloud.jnorthr.tools, name:jnorthr], mail:[host:mail.server], email:james.b.northrop@googlemail.com, appName:production]" 		
    when:
		ch.put('alias','jnorthr');
		 
    then:
		ch.has('alias') == true
		ch.get('alias') == 'jnorthr';		
  } // end of test


  
  	// =============================================================
    // Helper Methods
	def say(txt) { println txt; }

	// Holds text found in the external configuration file
	String configText="""// Custom block with setting
// conditional per environment.
servers {
    local {
        mail.host = 'greenmail'
        email = 'james.northrop@orange.fr'
    }

    prod {
        mail.host = 'mail.server'
        email='james.b.northrop@googlemail.com'
    }
}

environments {
    local {
        appName = 'local'
    }
    prod {
        appName = 'production'
    }
}

data{
	domain='cloud.jnorthr.tools'
	name='jnorthr'
}
""";

	// construct runtime environment
	public boolean runner(String e)
	{
		say "runner environment="+e;
		
		// Create Configurator slurper and set environment to prod.
		def configObject = ch.createConfig(e).parse(configText)

		say "prod Configurator.mail.host="+configObject.mail.host
		assert configObject.mail.host == 'mail.server'

		say "prod configObject.appName="+configObject.appName
		configObject.appName == 'production';
	} // end of runner

} 

