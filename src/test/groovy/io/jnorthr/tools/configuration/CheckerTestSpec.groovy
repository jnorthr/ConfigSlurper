package io.jnorthr.tools.configuration;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import io.jnorthr.tools.configuration.*;
import java.util.logging.Logger;
import spock.lang.*
import java.lang.*;

class CheckerTestSpec extends Specification 
{
  // fields
  Checker ck;
  
  @Shared
  String homePath  	    

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
     homePath = System.getProperty("user.home") + File.separator;

/* fix for windows
     String os = System.getProperty('os.name').toLowerCase();
     def i = os.indexOf('windows');
     if (i > -1)
     {
     	println "os="+os+ " and i="+i;
 		//homePath = homePath.replaceAll(/\\/, '\/')
		String token ="";
		for (char ch: homePath.toCharArray()) 
		{
			print ch;
			token+=ch; 
			print Character.getNumericValue(ch);
			print ' ';
		}

		println "\nand token=${token} and homePath="+homePath;
     } 
*/
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
	  	new File(homePath+"checker.config").delete()
 
    when:
		ck = new Checker();  
    then:
		// Asserts are implicit and not need to be stated.
    	true == new File(homePath+"checker.config").exists()
    	// Change "==" to "!=" and see what's happening!
    	ck.configFileName == homePath+"checker.config";
  } // end of test



  // 2nd Test
  def "2nd Test: Set Checker initial config filename to .checkerTestSpec2.config"() {
    given:
		println "2nd Test: Set Checker initial config filename to .checkerTestSpec2"
	  	new File(homePath+".checkerTestSpec2.config").delete()
 
    when:
		ck = new Checker(".checkerTestSpec2.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	true == new File(homePath+".checkerTestSpec2.config").exists()
    	ck.configFileName == homePath+".checkerTestSpec2.config";
  }

  // 3rd Test
  def "3rd Test: Use config filename .checkerTestSpec3 to save Hi kids"() {
    given:
		println "3rd Test: Use config filename .checkerTestSpec3 to save Hi kids"
	  	new File(homePath+".checkerTestSpec3.config").delete()
 
    when:
		ck = new Checker(".checkerTestSpec3.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	true == new File(homePath+".checkerTestSpec3.config").exists()
    	// Change "==" to "!=" and see what's happening!
    	ck.configFileName == homePath+".checkerTestSpec3.config";
		true == ck.save(ck.configFileName, "setup { }");
		ck.payload == "setup { }"
		ck.configFileName.endsWith(".checkerTestSpec3.config") == true;
  }

} // end of spec
