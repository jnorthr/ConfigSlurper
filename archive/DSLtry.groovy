GroovyRoom = 0;
enum Task { move, add, subtract,show }
enum Target { GroovyRoomX, OfficeSpace }

show = { println it }
square_root = { Math.sqrt(it) }

greeting = {"Hi ${it}"}
print = { println it }

def please(action) {
  [the: { what ->
    [of: { n -> action(what(n)) }]
  }]
}

def move(action) {
  [to: { what ->
    [of: { n -> action(what(n)) }]
  }]
}

def add(Integer hours) {
    ['to': { client ->
        client=client+=hours
    }]
}

// equivalent to: please(show).the(square_root).of(100)
please show the square_root of 100
please show the greeting of 'kids'
// ==> 10.0

add 2 to GroovyRoom
add 9 to GroovyRoom
println "GroovyRoom="+GroovyRoom;
println "--- the end ---"
// http://mrhaki.blogspot.fr/2011/05/groovy-goodness-command-chain.html  More examples