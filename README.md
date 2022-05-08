# the New Pulsar Generator (nuPG) 1.0 

at the moment works on Mac OS, Linux and Windows versions are possible in the future

1. Download SuperCollider 3.12 from: https://supercollider.github.io/releases/2021/08/02/supercollider-3.12.0
2. Install SuperCollider
3. Locate Quarks folder, it can be retreived from within SC IDE by calling Platform.userAppSupportDir
4. Create 'downloaded-quarks' if it doesn't exist and copy there Conductor folder from 'downloaded-quarks' folder in this repository. Make sure that you don't download a version of the Conductor from the web. The quark is old and no longer supported, a version included here fixes (in a dirty and hacky way) several problems of the original code
4. Run Quarks.gui in SC IDE. Locate 'miSCellaneous_lib' and 'JiTLib_Extensions' quarks and select it to be installed. You may have to manually select 'Conductor' quark in the GUI also. It will install the version we just copied into your directory. Press recompile class library (top-right corner of Quarks.gui)
5. Locate 'Extensions' folder, should be at the same level as 'downloaded-quarks', if it is not there create it.
6. Copy 'NuPG_1.0' from this repository
7. Recompile the library, CMD+Shift+L
8. Run 'nuPG_1.0_startUp.scd' you should end up with a GUI like the one below:

![alt text](https://github.com/marcinpiet/nuPG_1.0/blob/main/nuPG_1.0_ScreenShot.png?raw=true)

9. Boot the Server using 'Server' GUI at the bottom-left corner of the screen. Look at IDE's Post window for messages to see if Server boot's properly. Known problems: sample rate mismatch (fix it in the System Settings/Audio Midi Settings).
10. When Server is booted, the program is mute, you need to input (draw) data into 'pulsaret' and 'envelope' tables (middle of the screen). After this press button '[>]' next to '1' on 'TRAINS++PRESETS' GUI (top of the screen). You should hear the output!!! 
11. Explore 'MAIN' window which gives a direct control of the parameters. 
12. To activate 'GRAPHS' control press '[s]' next to '[>]' on 'TRAINS++PRESETS' GUI (top of the screen). This activates graphs reading task. You need to activate separate graph tasks on 'GRAPHS' window. Each of five graphs has a '[>]' button in the upper left corner - this activetes them. 
13. Control the speed of graph reading using a slider nect to 'GRAPHS' window. Low values = slow, High values = fast. 
14. This is work in progress (endless) ;)          
 



