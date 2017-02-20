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
  	    
  @Shared
  String homePath
  
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
  // run before the first feature method
  def setupSpec() 
  {
  	homePath = System.getProperty("user.home") + File.separator;     new File(homePath+".configurator.json").delete()
    new File(homePath+".configurator.json").delete()
    new File(homePath+".configuratorTest7Spec.json").delete()
    new File(homePath+".config.json").delete()
    new File(homePath+".configuratorTestSpec2.json").delete()
  }
  
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
  def "2nd Test: Set Configurator constructor to a specific filename"() {
    given:
	println "2nd Test: Set Configurator constructor to a specific filename"
 
    when:
		co = new Configurator(".configuratorTestSpec2.json");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	//def e = thrown(java.io.FileNotFoundException)
        //e.cause == null
		co.configFile.endsWith(".configurator.json") == true 
  }


  // 3rd Test
  def "3rd Test: make Configurator using default constructor"() {
    given: "3rd Test: Confirm default input path is user.home value"        

    when:
		def ss = co.getInputPath()
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
		ss == homePath;
  } // end of test
  

  // 4th Test
  def "4th Test: Check .defaultChecker.json is default filename"() {
    given: "4th Test: Confirm default input file is named as expected"        
    	co = new Configurator(); 
    when:
		def ss = co.getInputFile()
    then:
 		ss == ".config.json"
  } // end of text
  
  
  // Fifth Test
  def "5th Test: Confirm full input file name is as expected"() {
    given: "5th Test: Confirm full input filename is as expected"
 
    when:
        println "... 5th Test: current input path is "+co.getInputPath()
        println "... 5th Test: current input file is "+co.getInputFile()
        println "... 5th Test: current input full filename is "+co.getInputFileName();
		def ss = co.getInputFileName()

    then:
 		ss.endsWith(".config.json") == true

  } // end of test


  // Sixth Test
  def "6th Test: Confirm input path name is as expected"() {
    given: "6th Test: Confirm input path name is as expected"

    when:
        println "... current input path is "+co.getInputPath()
        println "... current input file is "+co.getInputFile()
        println "... current input full filename is "+co.getInputFileName();
		def ss = co.getInputPath()
 
    then:
		ss == homePath
  } // end of test


  // Seventh Test
  def "7th Test: Construct specific config file and populate it"() {
    given: "7th Test: Construct specific config file and populate it"
		co = new Configurator(".configuratorTest7Spec.json");

    when:
		co.ck.putInput('path',"${homePath}Projects/ConfigSlurper/resources/");
        co.ck.putInput('file',".scotch.json");
        co.ck.putInput('filename',co.getInputFileName());
        println "... current input path is "+co.getInputPath()
        println "... current input file is "+co.getInputFile()
        println "... current input full filename is "+co.getInputFileName();
 
    then:
        co.getInputPath()== "${homePath}Projects/ConfigSlurper/resources/";
        co.getInputFile()== ".scotch.json"
  } // end of test

} // end of class