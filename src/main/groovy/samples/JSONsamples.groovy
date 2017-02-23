// http://groovy-lang.org/json.html notes
// see: ECMA-404 JSON interchange syntax PDF in Dropbox
// Dates serilized as string defined in https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date 
// and http://stackoverflow.com/questions/10286204/the-right-json-date-format
// like: 2012-04-23T18:25:43.511Z coz it has millesec.s, sort correctly, follows ISO8601
import groovy.json.JsonSlurper
import groovy.json.JsonOutput;
import groovy.json.*

def ss = '{"person":{"name":"Guillaume","age":33,"pets":["dog","cat"]}}';
def ss2 = '''{"person":{"name":"Guillaume","age":33,"pets":["dog","cat"]}}''';
def ss3 = """{"person":{"name":"Guillaume","age":33,"pets":["dog","cat"]}}""";
def ss4 = """{
"person": {"name":"Guillaume","age":33, "pets":["dog","cat"] }
}
""";

def ss5 = """{
"alias":"jnorthr",
"name":"jim",
"fraction": -123.66
}""";

/*
JSON payload :
1. bracket pair {} with opening {
2. one double quote pair of " surrounding key "person"
3. colon :
4. Value in bracket pair {}
4a. key in single quote pair of "name"
4b. colon :
4c. Value in double quote pair "Guillaume"
4d. if more of map, then a comma char. ,
4e. Key in double quote pair "pets"
4f. colon :
4g. list in surrounding pair of []
4h. list item 1 in double quote pair "dog"
4i. if more of list, then following each item, a comma char. ,
4j. repeat 4h+4i
4k. end list with ]
4l. end of 4. value }
4m. end of 1. payload as }
*/

/*
def slurper = new groovy.json.JsonSlurper()
The JsonParserLax is a special variant of the JsonParserCharArray parser. 
It has similar performance characteristics as JsonFastParser but differs in that 
it isn’t exclusively relying on the ECMA-404 JSON grammar. 
For example it allows for comments, no quote strings etc.
*/

def slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY);
def result = slurper.parseText(ss4)
assert result instanceof Map
assert ss5 instanceof String

assert result.person.name == "Guillaume"
assert result.person.age == 33

result = slurper.parseText(ss5)
assert result.name == "jim"

def tx = JsonOutput.prettyPrint(ss5)
println "tx:\n--------------"
println tx;
println "--------------"

        String jsonString = "{\"obj\": {\"a\": 0, \"z\": 1, \"x\": 2}}";
        JsonSlurper jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        Map map = (Map) jsonSlurper.parseText(jsonString);
        map.each{k, v -> 
            println("%k => ${k} %v=${v}")
        }
        
// does Groovy POGO's too:
println "\nConverts Groovy POGOs to JSON too:"
class Person { String name }

def json6 = JsonOutput.toJson([ new Person(name: 'John'), new Person(name: 'Max') ])

assert json6 == '[{"name":"John"},{"name":"Max"}]'        
println "json6:\n--------------"
println json6;
println "--------------"

tx = JsonOutput.prettyPrint(json6)
println "\npretty Printed:\n------------------------\n"+tx;        
println "--------------"

        println "--- the end ---"