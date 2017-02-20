import groovy.io.FileType
// taken from http://mrhaki.blogspot.com/2016/05/groovy-goodness-creating-files-and.html
def path = "/Users/jimnorthrop/Dropbox/Projects/ConfigSlurper/archive/"

def say(tx){println tx;}
// sample FileTreeBuilder where the node names are the name of a directory to be created (argument is a closure),
// or the name of a file and some contents.
// Notice that with the DSL all file contents is appended to existing file contents.
// We need to delete an existing file first if we don't want to append the contents.
final projectname = 'jnorthr'
 
final File newDir = new File(path+'dsl')
final sample = new File('sparecode.txt').text;

final hello = """package net.jnorthr.tools;
import groovy.io.*
public class ${projectname}
{
    public ${projectname}
    {
        say 'Hello World'
    } // end of 

    def say(tx){println tx;}

    public static void main(String[] args)
    {
    }
}
""".toString() 

// Remove existing dir, so file contents is only set by our FileTreeBuilder DSL,
// otherwise content is added to the existing files.
if (newDir.exists()) {
    newDir.deleteDir()
}
 
newDir.mkdirs()
final FileTreeBuilder dir = new FileTreeBuilder(newDir)
final FileTreeBuilder treeBuilder = new FileTreeBuilder(newDir)
treeBuilder.dir('out') 
treeBuilder.dir('out/fred') 

dir {
    // Node name is the file name, followed by the contents.
    'README.adoc'('''\
        = ${projectname} rocks!
 
        Our ${projectname} features in Groovy are cool.
 
        '''.stripIndent())
 
    'README.adoc'('== A Heading')
 
     // We cannot use a closure argument with this DSL,
     // like with the builder. The DSL assume that a node with a
     // closure is a directory.
     // But we can use the File object argument to set
     // the file contents.
    'README1.adoc'(sample)
}
 
// If name is follwed by closure than a directory
// name is assumed and created.
dir.build {}

// Created directory with subdirectories.
dir.src {
    main{
        groovy {
            net{
                jnorthr{
                    tools{
                        'HelloWorld.groovy'(hello)                    
                    }
                }
            }
        }
        java {}
    }
    test{
        groovy {
            net{
                jnorthr{
                    tools{
                        'HelloWorldSpec.groovy'(hello)                    
                    }
                }
            }
        }
        java {}
    }
    // The name of the node is the directory name.
    docs {
        // And create file in the src/docs directory.
        'manuscript.adoc'('= Building Apps With Grails 3')
    }
}
 
assert new File('dsl/README.adoc').exists()
assert new File('dsl/src/docs/manuscript.adoc').exists()
assert new File('dsl/src/docs/manuscript.adoc').text == '= Building Apps With Grails 3'
say  "--- the start ---"
say  "--- the end ---"