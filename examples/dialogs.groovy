/*
 * This Example show the usage of the plugin defined dialog functions
 */
package examples

/* Just a simple alert */
alert "This is an alert"

/* Confirmation ? */
def answer = confirm "Do you really want to do this ?"
if (answer)
    println "You really do this, ouch !!"
else
    println "Whew, wise decision !!"

/* Prompt a string from the UI */
def astring = prompt "What's your favorite pet ?", "I like kitties"
println astring

def afile = promptOpenFile([ title: "My beautiful script", filterExtensions: ["*.txt"], filename: null ])
println "You choose this file : $afile"

def adirectory = promptOpenDirectory([ title: "Another script", filterPath: "*"])
println "This directory is recommended by you : $adirectory"

def yaf = promptSaveFile(options = [ title: "Yet Another File", filterExtensions: ["*.xml"], filename: null ])
println "You choose another file to save your work : $yaf"