package cloud.jnorthr.tools.configuration;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import cloud.jnorthr.tools.configuration.Configurator;
import java.util.logging.Logger;
import spock.lang.*

//import CacheEntry;
//import groovy.transform.Canonical
//import groovy.transform.ToString
//import javax.swing.JFileChooser;

class ConfigurationTestSpec extends Specification 
{
  // fields
  Configurator co;
  	    
  //static Logger log = Logger.getLogger(CacheManagerTestSpock.class.getName());

  // Fixture Methods
  // run before every feature method
  def setup() 
  { 
      boolean flag = false;
  	  co = new Configurator(); 
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
		println "1st Test: Setup Configurator for default path"
 
    when:
        co = new Configurator(); 
 
    then:
		// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	co != null;
    	co.ck.config != null
    	co.ck.dataObject != null
    	co.ck.configFileName.endsWith(".config.json") == true
  } // end of test



  // 2nd Test
  def "2nd Test: Set Configurator initial payload to bad JSON format"() {
    given:
	println "2nd Test: Set Configurator initial payload to bad JSON format"
 
    when:
		co = new Configurator("Fred");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	//def e = thrown(java.io.FileNotFoundException)
        //e.cause == null
		co.configFile.endsWith(".configurator.json") == true 
  }


/*
  // 3rd Test
  def "3rd Test: Ask appMode for 'prod' environment"() {
    given:
		println "3rd Test: Ask appName for 'prod' environment"        

    when:
    	co = new Configurator(); 
		def ss = co.getInputPath()
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
		ss == '/Users/jimnorthrop/'
  } // end of test
*/
  

  // 4th Test
  def "4th Test: Ask app for 'local' mail.host"() {
    given:
		println "4th Test: Ask app for 'local' mail.host"

    when:
		def ss = co.getInputFile()
		println "ss="+ss;
    then:
 		ss.endsWith(".fred.json") == true
  } // end of text
  
/*    
  
  // Fifth Test
  def "5th Test: Setup Configurator to use 'prod' environment"() {
    given:
		println "5th Test: Setup Configurator to use 'prod' environment"
 
    when:
		def ss = co.getInputFileName()
 		println "path:"+co.getInputPath()
    then:
 		ss.endsWith(".fred.json") == true
  } // end of test

  // Sixth Test
  def "6th Test: Setup Configurator to use 'local' environment"() {
    given:
		println "6th Test: Setup Configurator to use 'local' environment"

    when:
		def ss = co.getInputPath()
 
    then:
		ss == "/Users/jimnorthrop/"
  } // end of test


  // Seventh Test
  def "7th Test: Confirm dump() of configObject yields correct values"() {
    given:
    	println "7th Test: Confirm dump() of configObject yields correct values";
 		
    when:
		def ss = co.getInputPath()
 
    then:
		ss == "/Users/jimnorthrop/"
  } // end of test
*/

} // end of class