# Logbot

## About

This IRC bot, written in Java, was created for logbot.com, a project that 
allows channel operators log their IRC channels. 

If you want Logbot to sit in your channel, you need not to run this bot, you
can simply /invite my instance to your channel. For more information to channel
operators, please view our website: [logbot.com](http://logbot.com/).

## License 

Logbot is releaesd under the MIT License, for full license terms, see "License"
in the repositoriy's root folder or [view it online](http://www.opensource.org/licenses/mit-license.php).

## Building

Build with 'ant jar'
Run with 'java -jar logbot.jar {config-file.cfg}'

## Development 
I am not a Java developer by trade, if anyone has suggestions to improve how 
the repo is layed out or anything else about the project, please send me some 
pull requests or get into contact with me if you wish to help out. I understand
now (looking back) I made some poor design decisions early on and the code 
suffers.

I am looking into perhaps in the future of this doing the following:

 * Doing a more plugin-based system and cleaning up module code 
 * Handle the way modules/plugins return lines back to Logbot.main() differently
