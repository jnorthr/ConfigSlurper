// sample JSON parser
import groovy.json.*

  // replace windows \ values in homePath with /
  def replacement = 
  {
  	// Change \\ to /
    if (it == '\\') 
    {
        '/'
    }
    // Do not transform
    else {
        null
    }
  } // end of replacement


def  homePath = System.getProperty("user.home") + File.separator;
     homePath = homePath.collectReplacements(replacement);

def fn = "${homePath}bookmarks-2013-04-30.json"
def txt = new File(fn).text
println txt;
println "--------------------\n"

def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText(txt)

assert object instanceof Map
println "--------------------\n"
println "title="+object.title
println "id   ="+object.id
println "dateAdded="+object.dateAdded
println "lastModified="+object.lastModified
println "type   ="+object.type
println "root   ="+object.root

println "--------------------\n"

assert object.id == 1

println '--- the end ---'
