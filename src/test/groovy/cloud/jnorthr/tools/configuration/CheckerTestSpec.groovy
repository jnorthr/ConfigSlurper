package cloud.jnorthr.tools.configuration;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import cloud.jnorthr.tools.configuration.Configurator;
import java.util.logging.Logger;
import spock.lang.*

class CheckerTestSpec extends Specification 
{
  // fields
  Checker ck;
  
  @Shared
  String configPathName  	    

  // Fixture Methods
  
  // run before every feature method
  def setup() 
  { 
  }          

  // run after every feature method
  def cleanup() {}
  
  // Note: The setupSpec() and cleanupSpec() methods may not reference instance fields.
  def setupSpec() 
  {
      configPathName = System.getProperty("user.home") + File.separator;
  } // run before the first feature method
  
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
  def "1st Test: Setup Checker for default path"() {
    given:
	println "1st Test: Setup Checker for default path"
 
    when:
	ck = new Checker();  
    then:
	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	ck.configFileName == configPathName+".config.json";
  } // end of test



  // 2nd Test
  def "2nd Test: Set Checker initial config filename to .fred.json"() {
    given:
	println "2nd Test: Set Checker initial config filename to .fred.json"
 
    when:
	ck = new Checker(".fred.json");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	ck.configFileName == configPathName+".fred.json";
  }

  // 3rd Test
  def "3rd Test: Use config filename .fred.json to save Hi kids"() {
    given:
	println "3rd Test: Use config filename .fred.json to save Hi kids"
 
    when:
	ck = new Checker(".fred.json");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	ck.configFileName == configPathName+".fred.json";
		true == ck.save(ck.configFileName, "Hi kids\n");
		ck.payload == "Hi kids\n"
		ck.configFileName.endsWith(".fred.json") == true;
  }

} // end of spec
