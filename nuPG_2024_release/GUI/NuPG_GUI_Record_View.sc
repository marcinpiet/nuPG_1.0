NuPG_GUI_Record_View {

	var <>window;
	var <>fileName;
	var <>recFolder;
	var <>timeStamp;
	var <>fullRecordingPath;
	var <>numChan = 2;

	draw {|dimensions, n = 1|
		var layout;
		var recordingButton;
		var formatList, formatMenu;
		var directoryButton;
		var fileName;

		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		//var sliderRecordPlayer = NuPG_Slider_Recorder_Palyer;
		//sliderRecordPlayer.data = data.data_progressSlider;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window("_record", dimensions, resizable: false);
		window.userCanClose_(0);
		//window.alwaysOnTop_(true);
		window.view.background_(guiDefinitions.bAndKGreen);
		window.layout_(layout = GridLayout.new() );
		layout.hSpacing_(3);
		layout.vSpacing_(3);
		layout.margins_([5, 5, 5, 5]);

		//objects definition
		//////////////////////////////////////////////////////////////////////////
		//recording
		fileName = {"nuPg"};
		recFolder = { thisProcess.platform.recordingsDir }; //default recording folder

		fullRecordingPath = {
			recFolder.value +/+ fileName.value ++
			"_" ++ Date.getDate.format("(%d.%m.%Y %H:%M)") ++
			"." ++ Server.default.recHeaderFormat};

		recordingButton = guiDefinitions.nuPGButton([
			[ "RECORD", Color.white, Color.grey],
			[ "STOP", Color.black, Color.new255(250, 100, 90)]], 20, 70);

		recordingButton.action_{|butt|
			switch(butt.value,
				0, {Server.default.stopRecording},
				1, {Server.default.record(fullRecordingPath.value)}
			)
		};

		formatList = ["aiff", "wav", "caf"];
		formatMenu = guiDefinitions.nuPGMenu(formatList, 0, 50);
		formatMenu.action_{|menu|
			Server.default.recHeaderFormat = formatList[menu.value];
			("RECORDING FORMAT SET TO: " ++ formatList[menu.value].asString).postln;
			Server.default.prepareForRecord(fullRecordingPath.value)
		};

		directoryButton = guiDefinitions.nuPGButton([[ "_DIR"]], 20, 35);
		directoryButton.action_({
			var getPath;
			FileDialog({|path| getPath = path[0];
				recFolder = path[0];
				("NEW ROCORDING DIRECTORY SET TO:" ++ path[0].asString).postln;
			},
			fileMode: 2);
		});

		fileName = guiDefinitions.nuPGTextField("nuPG", 20, 100);
		fileName.action_({|field|
			fileName = field;
			("FILE NAME CHANGED TO: " ++ field.value.asString).postln;
		});

		//////////////////////////////////////////////////////////////////////////
		//place objects on view
		layout.addSpanning(formatMenu, row: 0, column: 0);
		layout.addSpanning(directoryButton, row: 0, column: 1);
		layout.addSpanning(fileName, row: 0, column: 2);
		layout.addSpanning(recordingButton, row: 0, column: 3);

		^window.front;

	}

}