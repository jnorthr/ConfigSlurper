package cloud.jnorthr.tools.configuration;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
//import CacheEntry;
//import groovy.transform.Canonical
//import groovy.transform.ToString
import java.util.logging.Logger;
import spock.lang.*
import javax.swing.JFileChooser;

class ConfigutationTestSpec extends Specification 
{
  // fields
  boolean flag = false;

  //static Logger log = Logger.getLogger(CacheManagerTestSpock.class.getName());

  // fixture methods
  // Note: The setupSpec() and cleanupSpec() methods may not reference instance fields.
  def setup() {}          // run before every feature method
  def cleanup() {}        // run after every feature method
  def setupSpec() {}     // run before the first feature method
  def cleanupSpec() {}   // run after the last feature method}

  // feature methods

  def "pushing an element on the stack"() {
  	// blocks go here
  }  

/*
Feature methods are the heart of a specification. They describe the features (properties, aspects) that you expect to find in the system under specification. By convention, feature methods are named with String literals. Try to choose good names for your feature methods, and feel free to use any characters you like!

Conceptually, a feature method consists of four phases:

Set up the feature's fixture
Provide a stimulus to the system under specification
Describe the response expected from the system
Clean up the feature's fixture
Whereas the first and last phases are optional, the stimulus and response phases are always present (except in interacting feature methods), and may occur more than once.

*/
  // First Test
  def "Setup Configurator for default path"() {
    def ch;
    given:
    	// [] is Groovy literal for List and is infered
        ch = new Configurator(); 
 
    when:
	flag = ch.runner('prod');
 
    then:
	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	flag == true;
  }

  // 2nd Test
  def "2nd Test: Set Configurator initial folder to unknown path"() {
    given:
        def ch = new Configurator();
 
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
        def ch = new Configurator();
 
    when:
		boolean  yn = ch.runner('prod')
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	yn == true;
		ch.getVal('appName') == 'production'
  }
  
  // helper methods
} 

