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

 



