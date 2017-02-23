// https://examples.javacodegeeks.com/jvm-languages/groovy/groovy-json-example/
package io.jnorthr.tools.json;
//package com.javacodegeeks.example.groovy.json;

import groovy.json.JsonBuilder
import groovy.json.JsonOutput

def jsonBuilder = new JsonBuilder()

jsonBuilder.config
{
    env : "Prod"
    database {
        host "example.com"
        port 3306
        type "MySQL"
        user 'dbUser'
        pass 'dbPass'
        driver 'com.mysql.jdbc.Driver'
    }
    threadPool 10
    useBridge 'Y'
}

println "JSONBuilder Object : " + jsonBuilder
println ""
println "JSON Pretty Printed Config "
println "=========================="
println JsonOutput.prettyPrint(jsonBuilder.toString())
println ""

String outputFile = 'config.json'
def fileWriter = new FileWriter(outputFile)
jsonBuilder.writeTo(fileWriter)
fileWriter.flush() /* to push the data from  buffer to file */
println "Config details are written into the file '${outputFile}'"

println ""
def fileContents = new File(outputFile).text
println "File contents : " + fileContents
println ""
println "File Contents PrettyPrint"
println "========================="
println JsonOutput.prettyPrint(fileContents)
