package io.jnorthr.tools.configuration;
import io.jnorthr.toolkit.PathFinder;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import io.jnorthr.tools.configuration.Configurator;
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
     PathFinder pf = new PathFinder();
     homePath = pf.getHomePath();
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
	  	new File(homePath+".configurator.json").delete()
 
    when:
        co = new Configurator(); 
 
    then:
		// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	co != null;
    	co.ck.config != null
    	co.ck.dataObject != null
    	co.ck.configFileName.endsWith(".checker.config") == true
  } // end of test



  // 2nd Test
  def "2nd Test: Set Configurator constructor to a specific filename"() {
    given:
		println "2nd Test: Set Configurator constructor to a specific filename"
	  	new File(homePath+".configuratorTestSpec2.json").delete()
 
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
		println "3rd Test: Use new Configurator for default path"
	  	new File(homePath+".configurator.json").delete()
 
    when:
        co = new Configurator();
        def sp = co.getInputPath() // Checker fails to register home path if no config file found 1st time, but ok after that coz it's saved 1st time around
		def ss = co.getInputFile()
 		println "... sp="+sp;
 		println "... ss="+ss;
 		
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
		sp == homePath;
 		ss == ".default.config" || ".checker.config"
  } // end of test


  // Forth Test
  def "4th Test: Construct specific config file and populate it"() {
    given: "4th Test: Construct specific config file and populate it"
	  	new File(homePath+".configuratorTest4Spec.json").delete()
		co = new Configurator(".configuratorTest4Spec.json");

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