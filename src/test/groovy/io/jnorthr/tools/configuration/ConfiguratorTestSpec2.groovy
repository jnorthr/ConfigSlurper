package io.jnorthr.tools.configuration;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import io.jnorthr.tools.configuration.Configurator;
import java.util.logging.Logger;
import spock.lang.*
import java.text.SimpleDateFormat;
import io.jnorthr.toolkit.PathFinder;

class ConfigurationTestSpec2 extends Specification 
{
  // fields
  Configurator co;
  	    
  @Shared
  String homePath
  
  // Fixture Methods
  // run before every feature method
  def setup() 
  { 
      boolean flag = false;
  }          

  // run after every feature method
  def cleanup() {}

  // Note: The setupSpec() and cleanupSpec() methods may not reference instance fields.
  // Both run before the first feature method
  def setupSpec() 
  {
	    PathFinder pf = new PathFinder();
	    homePath = pf.getHomePath();

    	new File(homePath+".config.json").delete()
    	new File(homePath+".configurator.json").delete()
  }
  
  // run after the last feature method}
  def cleanupSpec() 
  {
  }   

  // Feature Methods
  // First Test
  def "1st Test: ConfigurationTestSpec2"() {
    given: "1st Test: Construct specific config file, ck existence and path name"
        println "1st Test: Construct specific config file, ck existence and path name"
    	new File(homePath+".configuratorTestSpec2Test1.json").delete()
		co = new Configurator(".configuratorTestSpec2Test1.json");

    when:
        println "... current input path is "+co.getInputPath()
        println "... current input file is "+co.getInputFile()
        println "... current input full filename is "+co.getInputFileName();
 
    then:
    	new File(homePath+".configuratorTestSpec2Test1.json").exists() == true
        co.getInputPath()== homePath;
        co.getInputFile()== ".configuratorTestSpec2Test1.json"
  } // end of test

  def "2nd Test: ConfigurationTestSpec2"() {
    given: "2nd Test: Construct specific config file and populate it"
    	new File(homePath+".configuratorTestSpec2Test2.json").delete()
		co = new Configurator(".configuratorTestSpec2Test2.json");

    when:
		co.ck.putInput('path',"${homePath}Projects/ConfigSlurper/resources/");
        co.ck.putInput('file',".test2-2.json");
        println "... current input path is "+co.getInputPath()
        println "... current input file is "+co.getInputFile()
        println "... current input full filename is "+co.getInputFileName();
 
    then:
    	new File(homePath+".configuratorTestSpec2Test2.json").exists() == true
        co.getInputPath()== "${homePath}Projects/ConfigSlurper/resources/";
        co.getInputFile()== ".test2-2.json"
		co.getInputFileName() == "${homePath}Projects/ConfigSlurper/resources/.test2-2.json"
  } // end of test

  def "3rd Test: ConfigurationTestSpec2"() {
    given: "3rd Test: Populate mobile phone in standard config file"
		co = new Configurator();

    when:
        co.ck.put('cell',"004478557654321");
        println "... 3rd: current input path is "+co.getInputPath()
        println "... 3rd: current input file is "+co.getInputFile()
        println "... 3rd: current input full filename is "+co.getInputFileName();
        println "... 3rd: cell number in ROOT is "+co.ck.get('cell');
 
    then:
    	new File(homePath+".checker.config").exists() == true
        co.ck.get('cell')== "004478557654321"
  } // end of test


  def "4th Test: ConfigurationTestSpec2"() {
    given: "4th Test: Put date field in standard config file"
		co = new Configurator();
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
        TimeZone tz = TimeZone.getTimeZone( "UTC" );
        df.setTimeZone( tz );

    when:
    	def dt = new Date();        

        String output = df.format( dt );
        println "ConfigurationTestSpec2 Test 4:"+output;
        co.ck.put('date',"${output}");
        co.ck.put('flag',true);
 
    then:
        co.get('flag')== true;
        co.get('date')== "${output}";
  } // end of test


} // end of class  // Second Set of Tests
