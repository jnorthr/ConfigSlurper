// Configuration script as String, but can also be URL, file.
package io.jnorthr.tools.*;
import java.io.File;

public class MrHakiSample{

    String configFileName = System.getProperty("user.home") + File.separator  +".config.json";

    String payload = '''setup {
mail.host = 'smtp.myisp.com'
mail.auth.user = 'server'
input.path = '/Users/jimnorthrop/Dropbox/Projects/ConfigSlurper/resources'
input.filename = 'sample.config'
}'''.toString();
 
    ConfigSlurper config = new ConfigSlurper();
    
    groovy.util.ConfigObject dataObject;


    /*
     * Default constructor builds/or confirms existence of the default configuration file
     */
    public MrHakiSample()
    {
        say "Default constructor MrHakiSample.groovy"  
        config = new ConfigSlurper();  
        dataObject = config.parse(payload);

        read(configFileName)    
        save(configFileName,payload);

        //def config = new ConfigSlurper()
        //dataObject = config.parse(payload);
        put("key","value");
        putInput("filename","MadMax77.png");
        
        println "put 2 was ok"
        String ss = dataObject.prettyPrint().trim()+'\n';
        println "------------------------\n"+ ss +"\n--------------------------";

        save(configFileName,ss);
    } // end of default constructor
    
    def say(tx) { println tx; }


    public boolean putInput(String ky, String va)
    {
        dataObject.with {
            setup.input.put(ky,va);
        }
    } // end of method

    public boolean put(String ky, String va)
    {
        dataObject.with {
            setup.put(ky,va);
        }
    } // end of method

     
    // fill config text area from external file else default to skeleton
    public read(String fn)
    {    
        def fi = new File(fn);
         
        if ( fi.exists() )
        {
            say "... config.file "+fn+" exists - getting it's text";
            payload = fi.getText("UTF-8");
            dataObject = config.parse(payload);
            println "------------------\npayload set to :"
            println payload
            println "------------------"
        }
        else
        {
            say "... config.file "+fn+" does not exists, will build it."
            save(fn,payload);
        }
    } // end of read    


    /*
     * Save a string of data to a writer as external UTF-8 file with provided filename 'fn'
     */
    public boolean save(String fn, String data)
    {
        // Or a writer object:
        new File(fn).withWriter('UTF-8') { writer ->
                writer.write(data)
        } // end of write    
        
        payload = data;
        dataObject = config.parse(payload);
        println "------------------\npayload set to :"
        println payload
        println "------------------"

        println "... saved ${data.size()} bytes to "+fn+'\n-----------------------\n';
        return true;
    } // end of method
    
    // =============================================================================    
    /**
      * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
      * 
      * argument is a list of strings provided as command-line parameters. 
      * 
      * @param  args a list of possibly zero entries from the command line; first arg[0] if present, is
      *         taken as a simple file name of a json-structured configuration file;
      */
    public static void main(String[] args)
    {
        println "Hello from MrHakiSample.groovy"
        MrHakiSample mhs = new MrHakiSample();
        
/*
dataObject.put("key","hi kids");
println "put was ok"
println "------------------------\n"+ dataObject.prettyPrint().trim()+"\n--------------------------\nSS : ---------------------";
String ss = dataObject.prettyPrint().trim()+'\n';
println ss;
save(configFileName,ss);
println "--------------------------\n"


read(configFileName)
def config2 = new ConfigSlurper()
groovy.util.ConfigObject dataObject2 = config2.parse(payload);
dataObject2.put("key","Marvin Gaye");
println "------------------------\ndataObject2 :\n"+ dataObject2.prettyPrint().trim()+"-------------------------";
ss = dataObject2.prettyPrint().trim()+'\n';
save(configFileName,ss);
*/

        println "--- the end ---"
    } // end of main 

} // end of class



//==============================================================
// spare code
/*
>>
>> import groovy.json.*
>>
>> def options = JsonOutput.options()
>>         .excludeNulls()
>>         .excludeFieldsByName('make', 'country', 'record')
>>         .excludeFieldsByType(Number)
>>         .addConverter(URL) { url -> '"http://groovy-lang.org"' }
>>
>> StringWriter writer = new StringWriter()
>> StreamingJsonBuilder builder = new StreamingJsonBuilder(writer, options)
>>
>> builder.records {
>>     car {
>>         name 'HSV Maloo'
>>         make 'Holden'
>>         year 2006
>>         country 'Australia'
>>         homepage new URL('http://example.org')
>>         record {
>>             type 'speed'
>>             description 'production pickup truck with speed of 271kph'
>>         }
>>     }
>> }
>>
>> assert writer.toString() == '{"records":{"car":{"name":"HSV
>> Maloo","homepage":"http://groovy-lang.org"}}}'

    // Try JSON parser
    public  boolean tester2(def env)
    {
        say "tester2 --->"+env;
        // -------------------------------
        // new JSON parser improvements
        def parser = new JsonSlurper().setType(LAX)

        def conf = parser.parseText '''
            // Checker file
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

*/