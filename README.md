# The New Pulsar Generator (nuPG) 2024 

This is the personal nuPG version I have been working with and developing for the past couple of years.

1. Download the latest SuperCollider from: https://supercollider.github.io/downloads  
2. Install SuperCollider
3. Locate the Quarks folder, it can be retrieved from within SC IDE by calling Platform.userAppSupportDir
4. Create 'downloaded-quarks' if it doesn't exist and copy the Conductor folder from the 'downloaded-quarks' folder in this repository. !!! Could you make sure that you don't download a version of the Conductor from the web !!! The quark is old and no longer supported, a version included here fixes (in a dirty and hacky way) several problems of the original code
4. Run Quarks.gui in SC IDE. Locate 'miSCellaneous_lib' and 'JiTLib_Extensions' quarks and select it to be installed. You may have to select the 'Conductor' quark in the GUI also manually. It will install the version we just copied into your directory. Press recompile class library (top-right corner of Quarks.gui)
5. Locate the 'Extensions' folder, which should be at the same level as the 'downloaded-quarks', if it is not there create it.
6. Copy 'NuPG_2024_release' from this repository
7. Recompile the library, CMD+Shift+L
8. Run 'nuPG_24_startUp.scd' you should end up with a GUI like the one below:

![alt text](https://github.com/marcinpiet/nuPG_1.0/blob/main/nuPG_2024_ScreenShot.png?raw=true)

9. Boot the Server using the 'Server' GUI at the bottom-left corner of the screen. Look at IDE's Post window for messages to see if the Server boots properly. Known problems: sample rate mismatch (fix it in the System Settings/Audio Midi Settings).
10. When the Server is booted, the program is mute, you need to input (draw) data into the 'pulsaret waveform' and 'envelope ' tables (bottom left of the screen). After this navigate to the top left of the screen to '_train control' GUI and press button '[n]' next to 'loop'. You should hear the output!!! 
11. Explore the '_main' window which gives direct control of the parameters. 
12. To activate group control press 'loop' next to '[n]' on '_train control' GUI (top left of the screen). This activates the parameter sequences for three sets of the formant, amplitude, panning and envelope dilation. You need to activate a separate group on '_group control' GUI. 
13. Please refer to the 'nuPG_24_userManual.pdf' for more information  
14. A walkthrough video with above steps can be found here: https://vimeo.com/1004876511?share=copy           
 



