// need imports for JSON parsing bottom of this script
import groovy.json.*
import static groovy.json.JsonParserType.*

// Configuration script. 
// see: http://mrhaki.blogspot.fr/2009/10/groovy-goodness-using-configslurper.html
def config = new File('/Volumes/DURACELL/backups/sample.config').text

// Helper closure to create a new
// ConfigSlurper for the given environment and
// register servers as section with configuration
// per environment.
def createConfig = { env ->
    def configSlurper = new ConfigSlurper(env)
    configSlurper.registerConditionalBlock('servers', env)
    configSlurper
}

// Create configuration slurper and
// set environment to prod.
def configuration = createConfig('prod').parse(config)

println "prod configuration.mail.host="+configuration.mail.host
assert configuration.mail.host == 'mail.server'
println "prod configuration.appName="+configuration.appName
assert configuration.appName == 'production'


// Create configuration slurper and
// set environment to local.
configuration = createConfig('local').parse(config)

assert configuration.mail.host == 'greenmail'
assert configuration.appName == 'local'

// traits samples; see: http://groovy.codehaus.org/Groovy+2.3+release+notes?nc#Groovy2.3releasenotes-Traits
trait FlyingAbility {
    String fly() { "I'm flying!" }
}
class Bird implements FlyingAbility {}
def b = new Bird()
assert b.fly() == "I'm flying!"

// -------------------------------
// new JSON parser improvements

def parser = new JsonSlurper().setType(LAX)

def conf = parser.parseText '''
    // configuration file
    {
        // no quote for key, single quoted value
        env: 'production'
        # pound-style comment
        'server': 5
    }
'''

println "parser config.env="+conf.env


println "--- the end ---" 