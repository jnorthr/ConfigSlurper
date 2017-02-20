println "--- the start ---"
def say(tx){println tx;}
def single = ~'[ab]test\\d'
assert 'java.util.regex.Pattern' == single.class.name
println "single="+single.class.name
  
def dubble = ~"string\$"
assert dubble instanceof java.util.regex.Pattern
println "dubble="+dubble.class.name
 
// Groovy's String slashy syntax is very useful to
// define patterns, because we don't have to escape
// all those backslashes.
def slashy = ~/slashy \d+ value/
assert slashy instanceof java.util.regex.Pattern
println "slashy="+slashy.class.name
 
// GString adds a negate() method which is mapped
// to the ~ operator.
def negateSlashy = /${'hello'}GString$/.negate()
assert negateSlashy instanceof java.util.regex.Pattern

def s = 'more'
def curlySlashy = ~"$s GString"
assert curlySlashy instanceof java.util.regex.Pattern
println "curlySlashy="+curlySlashy.class.name
 
// Using Pattern.matcher() to create new java.util.regex.Matcher.
// In a next blog post we learn other ways to create
// Matchers in Groovy.
def testPattern = ~'t..t'
assert testPattern.matcher("test").matches()
println "testPattern="+testPattern.class.name
 
// Groovy adds isCase() method to Pattern class.
// Easy for switch and grep statements.
def p = ~/\w+vy/
assert p.isCase('groovy')
println "p of ${p} ="+p.class.name
println "p.isCase('groovy') ? "+p.isCase('groovy')
 
switch ('groovy') {
    case ~/java/: assert false; println "~/java/"; break;
    case ~/gr\w{4}/: println "~/gr\\w{4}/ is groovy"; break;
    default: assert false
}

switch(p)
{
    case "java": println "java"; break;
    case "groovy": println "groovy"; break;
    default: println "hit default p="+p;
}
 
// We can use flags in our expressions. In this sample
// we use the case insensitive flag (?i).
// And the grep method accepts Patterns.
def lang = ~/^(?i)gr.*/
def languages = ['java', 'Groovy', 'gRails','Gradle','Ruby']
assert ['Groovy', 'gRails','Gradle'] == languages.grep(lang)

languages.each{lan-> 
    print "lang="+lan;
    println "  matches?"+lang.matcher(lan).matches()
}

println "\n---------------------\nRemove string pieces?"
// Remove first match of a word with 5 characters.
def xx = ('Remove first match of 5 letter word' - ~/\b\w{5} \b/) 
assert xx == 'Remove match of 5 letter word'
say "xx="+xx; 
 
// Remove first found numbers followed by a whitespace character.
assert ('Line contains 20 characters' - ~/\d+\s+/) == 'Line contains characters'

println "--- the end ---"