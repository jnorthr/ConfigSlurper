import groovy.io.FileType
def path = "/Users/jimnorthrop/Dropbox/Projects/GParsGroovyTests/src/test/groovy"
def fi
println "--- the start ---"
def say(tx){println tx;}
def omit(fi) {boolean b = (fi.name.startsWith('.'))?true:false;}

say "Groovy File logic comes from http://mrhaki.blogspot.com/2016/05/groovy-goodness-creating-files-and.html and http://mrhaki.blogspot.com/2010/04/groovy-goodness-working-on-files-or.html"
say ""
def currentDir = new File(path)
def dirs = []
currentDir.eachFileRecurse FileType.FILES, {
    def fn = it
    if (!omit(fn))
    {
        dirs << it.getCanonicalPath()  //getAbsolutePath()
    }
}
dirs.each{ say it; }
say "-------"
def found = []
currentDir.eachFileMatch(FileType.ANY, ~/^.*/) {
   found << it.getCanonicalPath()
}
found.each{ say "found:"+it; }
println "--- the end ---"