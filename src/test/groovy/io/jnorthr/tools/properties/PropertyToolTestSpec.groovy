package io.jnorthr.tools.properties;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import io.jnorthr.tools.configuration.*;
import io.jnorthr.toolkit.PathFinder;

import java.util.logging.Logger;
import spock.lang.*
import java.text.SimpleDateFormat;

class PropertyToolTestSpec extends Specification 
{
  // fields
  PropertyTool pt;
  	    
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
  // Both run before the first feature method
  def setupSpec() 
  {
		PathFinder pf = new PathFinder();
	    homePath = pf.getHomePath();
  }

  
  // run after the last feature method}
  def cleanupSpec() 
  {
  }   


  // Feature Methods
  // First Test
  def "1st Test: PropertyToolTestSpec"() {
    given: "1st Test: Construct default java property file, ck existence and path name"
    	new File(homePath+".config.properties").delete()
		pt = new PropertyTool(".config.properties");

    when:
        println "... PropertyToolTestSpec1 current input path is "+pt.getPath()
        println "... PropertyToolTestSpec1 current input file is "+pt.getFileName()
 
    then:
    	new File(homePath+".config.properties").exists() == true
        pt.getPath()== homePath;
        pt.getFileName()== null;
  } // end of test


  def "2nd Test: PropertyToolTestSpec"() {
    given: "2nd Test: Construct specific java property file and populate it"
    	new File(homePath+".PropertyToolTestSpec.properties").delete()
		pt = new PropertyTool(".PropertyToolTestSpec.properties");

    when:
		pt.setProperty('path',"${homePath}Projects/ConfigSlurper/resources/");
        pt.setProperty('filename',".test2-2.properties");
        println "... PropertyToolTestSpec2 current input path is "+pt.getPath()
        println "... PropertyToolTestSpec2 current input file is "+pt.getFileName()
 
    then:
    	new File(homePath+".PropertyToolTestSpec.properties").exists() == true
        pt.getPath()== "${homePath}Projects/ConfigSlurper/resources/";
        pt.getFileName()== ".test2-2.properties"
  } // end of test


  def "3rd Test: PropertyToolTestSpec"() {
    given: "3rd Test: Populate mobile phone in java property file"
    	new File(homePath+".config.properties").delete()
		pt = new PropertyTool(".config.properties");

    when:
        pt.setProperty('cell',"004478557654321");
        println "... PropertyToolTestSpec3 current input path is "+pt.getPath()
        println "... PropertyToolTestSpec3 current input file is "+pt.getFileName()
 
    then:
    	new File(homePath+".config.properties").exists() == true
        pt.getProperty('cell')== "004478557654321"
  } // end of test


  def "4th Test: PropertyToolTestSpec"() {
    given: "4th Test: Populate mobile phone in java property file"
    	new File(homePath+".config.properties").delete();
		pt = new PropertyTool();

    when:
        pt.setProperty('cell',"00447654321");
 
    then:
        pt.getProperty('cell')== "00447654321"
  } // end of test


  def "5th Test: PropertyToolTestSpec"() {
    given: "5th Test: Put date field in java property file"
    	new File(homePath+".config.properties").delete()
		pt = new PropertyTool();
		
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
        TimeZone tz = TimeZone.getTimeZone( "UTC" );
        df.setTimeZone( tz );

    when:
    	def dt = new Date();        

        String output = df.format( dt );
		println "5th Test: Put date field ${output} in java property file"
        pt.setProperty('date',"${output}");
 
    then:
        pt.getProperty('date')== "${output}";
  } // end of test


} // end of class  // Spock Set of Tests
