NuPG_GUI_Frequency_Editor {

		var <>window;
	    var <>stack;
     	var <>tablePath, <>defaultTablePath;
	    var <>multisliders;
	    var <>presetNumberBox, <>targetPresetNumberBox;
	    var <>menu;
	    var <>loadFolder, <>defFolder, <>splitFile;
	    var <> dataToPDF;
	    var <>interpolationSlider;
		var <>recButtons;

	build {|colorScheme = 0, data, buffer, recordTasks, playbackTasks|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		//retreive file names from a folder
		var files = {|tablePath| ["/*.wav", "/*.aiff"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
		var fileNames = files.value(defaultTablePath).collect{|i| PathName(i).fileName};
		var dataPlotter = NuPG_Plotter.new;
		var copyData = NuPG_DataCopy;
		var conductors = [\con_1, \con_2, \con_3];



		window = Window.new("FREQUENCY EDITOR",
			Rect.fromArray(defs.nuPGDimensions[41]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//add copy/paste functionality
		3.collect{|i|
			view[i].keyDownAction_({arg view,char,modifiers,unicode,keycode;
				//copy
				if(keycode == 8,

					{copyData.copyData(data.data_frequency[i].input);
						"copy data".postln},
					{});
                //paste
				if(keycode == 35,
					{   //update the CV
						data.data_frequency[i].input = copyData.pasteData;
						//update the buffer
						buffer[i][2].sendCollection(copyData.pasteData);
						"paste data".postln},
					{})
			})
		};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]).spacing_(1))};

		//4 slots for 3 stacks
		slots = 3.collect{ 4.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 4.collect { GridLayout.new().margins_([3,3,3,3]) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 4.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };

		//MULTISLIDERS
		multisliders = 3.collect{};
		//menus
		menu = 3.collect{};
		//load folder
		loadFolder = 3.collect{};
		defFolder = 3.collect{};
		splitFile = 3.collect{};
		//save data as PDF
		dataToPDF =  3.collect{};
		//preset
		presetNumberBox = 3.collect{};
	    targetPresetNumberBox = 3.collect{};
		interpolationSlider = 3.collect{};
		//slider record button
		recButtons = 3.collect{};

		//put objects into slots]

		3.collect{|i|
			     //slot 0 = multislider
				//multisliders
				multisliders[i] = defs.nuPGMultislider(colorScheme);
			    multisliders[i].action_{|ms|
				data.data_frequency[i].value = ms.value;
				buffer[i][2].sendCollection(ms.value);
			};
				slotGrid[i][0].addSpanning(multisliders[i], 0, 0, columnSpan: 2);

			//slot 1 = save, load, menu
			2.collect{|l|
				var states = [["open"],["save"]];
				var actions = [
					//open action
					{Dialog.openPanel({ arg path;
						var size, file, temp, array;
						file = SoundFile.new;
						file.openRead(path);
						temp = FloatArray.newClear(4096);
						file.readData(temp);
						array = temp.asArray.resamp1(2048).copy;
						array = array.linlin(-1.0, 1.0, 0.0, 1.0);
						data.data_frequency[i].value_(array);
						buffer[i][2].sendCollection(array);
					},{"cancelled".postln}
					)},
					//save action
					//opens by default the TABLES directory of the app
					{Dialog.savePanel({ arg path, recHeaderFormat = "wav";

						path = tablePath;

						buffer[i][2].write(
							path: path++"."++recHeaderFormat,
							headerFormat: "wav",
							sampleFormat: "int24",
							numFrames: 2048);
					},{"cancelled".postln}, path: tablePath
					)}
				];

				slotGrid[i][1].addSpanning(defs.nuPGButton([states[l]], 20, 50)
					.mouseDownAction_(actions[l])
					, 0, 0 + l);

			};

			menu[i] = defs.nuPGMenu(defState: 1);
			slotGrid[i][1].addSpanning(menu[i], 0, 2);
			menu[i].items = [];
			menu[i].items = fileNames;
			tablePath = defaultTablePath;

			menu[i].action_({|item|
				var size, dataFile, file, temp, array;
				dataFile = tablePath ++ fileNames[menu[i].value];
				file = SoundFile.new;
				file.openRead(dataFile);
				temp = FloatArray.newClear(4096);
				file.readData(temp);
				array = temp.asArray.resamp1(2048).copy;
				array = array.linlin(-1.0, 1.0, 0.0, 1.0);

				data.data_frequency[i].value = array;
				buffer[i][2].sendCollection(array);

				fileNames[menu[i].value].postln;
			});

			loadFolder[i] = defs.nuPGButton([["folder"]], 20, 50)
			.action_({
				FileDialog({ |path|
					postln("Selected file:" + path);
					postln("File type is:" + File.type(path));
					//update the tablepath
					tablePath =  path ++ "/";
					files = {|tablePath| ["/*.wav", "/*.aiff"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
	                files.value(tablePath).postln;
					fileNames = files.value(tablePath).collect{|i| PathName(i).fileName};
                    //update the menu items
					menu[i].items = fileNames;
				},
				fileMode: 0,
				stripResult: true);
			});
			slotGrid[i][1].addSpanning(loadFolder[i], 0, 3);

			defFolder[i] = defs.nuPGButton([["defFolder"]], 20, 50)
			.action_({
					//postln("Selected file:" + path);
					//postln("File type is:" + File.type(path));
					//update the tablepath
					tablePath =  defaultTablePath;
					files = {|tablePath| ["/*.wav", "/*.aiff"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
	                files.value(tablePath).postln;
					fileNames = files.value(tablePath).collect{|i| PathName(i).fileName};
                    //update the menu items
					menu[i].items = fileNames;
			});
			slotGrid[i][1].addSpanning(defFolder[i], 0, 4);

			splitFile[i] = defs.nuPGButton([["splitFile"]], 20, 50)
			.action_({
				var env, fileName, slicesFolder, dataFile, file, fileToFloatArray, sliceFile, sliceEnv;
					FileDialog({ |path|
					postln("Selected file:" + path);
					//postln("File type is:" + File.type(path));
					dataFile = path;
				    dataFile.postln;
					//get file into floatarray
					file = SoundFile.openRead(path);
					fileToFloatArray = FloatArray.newClear(file.numFrames * file.numChannels);
					file.readData(fileToFloatArray);
					//get file's name and create a folder for slices
					fileName = PathName(path).fileName;
					slicesFolder = File.makeDir(path ++ "_SLICES");
					//function for slicing
					env = {|array, startFrame, stopFrame, attD = 0.005, relD = 0.01|
						var sus ;
						attD = (attD*Server.local.sampleRate).trunc.asInteger ;
						relD = (relD*Server.local.sampleRate).trunc.asInteger ;
						sus = stopFrame-startFrame-attD-relD;
						Env.new([0,1,1,0], [attD, sus, relD], [-3,\linear,3])
						.asSignal(stopFrame-startFrame).collect{|i| i}
						*
						array[startFrame..stopFrame]
					};

					fileToFloatArray.clump(2048).size.collect{|k|
						var sliceFile = SoundFile.new.headerFormat_("WAV").sampleFormat_("int24").numChannels_(1) ;
						var sliceEnv = env.(fileToFloatArray, 0 + (k*2048),  2048 + (k*2048)) // between 1 and 2 secs
						.as(FloatArray);
						//var subFolder = File.mkdir(~analysisPath ++ ~newFolder);
						//data.plot; //UNCOMMENT TO PLOT ALL THE NEW FILES - IT ADDS CALCULATION TIME
						sliceFile.openWrite(slicesFolder ++ "/" ++ "slice_" ++ "_" ++ k ++".wav") ;
						sliceFile.writeData(sliceEnv);
						sliceFile.close ;
					};

					//update the tablepath
					tablePath =  slicesFolder ++ "/";
			        tablePath.postln;
					files = {|tablePath| ["/*.wav", "/*.aiff"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
	                files.value(tablePath).postln;
					fileNames = files.value(tablePath).collect{|i| PathName(i).fileName};
                    //update the menu items
					menu[i].items = fileNames;
				},
				fileMode: 0,
                stripResult: true);



			});
			slotGrid[i][1].addSpanning(splitFile[i], 0, 5);

			dataToPDF[i] = defs.nuPGButton([["toPDF"]], 20, 50)
			.action_({dataPlotter.saveData(data.data_frequency[i].value)});

			slotGrid[i][1].addSpanning(dataToPDF[i], 0, 6);


			//SLOT 2 - norm, reverse, invert

			4.collect{|l|
				var states = ["norm", "R", "I", "F"];
			var action = [
					//normalize to -1, 1
					{
						var array;

						array = data.data_frequency[i].value.deepCopy;
						array = array.normalize;
						data.data_frequency[i].value = [0.0,1.0].asSpec.map(array);
						buffer[i][2].sendCollection(array);
					},
					//reverse
					{
						var array;

						array = data.data_frequency[i].value.deepCopy;
						array = array.reverse;
						data.data_frequency[i].value = array;
						buffer[i][2].sendCollection(array);
					},
					//invert
					{
						var array;

						array = data.data_frequency[i].value.deepCopy;
						array = array.invert;
						data.data_frequency[i].value = array;
						buffer[i][2].sendCollection(array);
					},
					//flop
					{
						var array;

						array = data.data_frequency[i].value.deepCopy;
						array = array.linlin(0.0, 1.0, 0.0, -1.0);
						data.data_frequency[i].value = array.linlin(-1.0, 0.0, 0.0,1.0);
						buffer[i][2].sendCollection(array);
					}
				];


				slotGrid[i][2].addSpanning(defs.nuPGButton([[states[l]]], 20, 50)
					.mouseDownAction_(action[l]), 0, l);
			};

		   //SLOT 3 = LOCAL PRESET
				//load, save, +, - <>
			6.collect{|k|
				var actions = [
							{data.conductor[conductors[i].asSymbol][\con_freq].save},
							{data.conductor[conductors[i].asSymbol][\con_freq].load},
							//add preset
							{data.conductor[conductors[i].asSymbol][\con_freq].preset.addPreset;
								data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value =
								data.conductor[conductors[i].asSymbol][\con_freq].preset.presets.size - 1},
							//remove preset
							{data.conductor[conductors[i].asSymbol][\con_freq].preset.removePreset(
								data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value);
							data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value =
							data.conductor[conductors[i].asSymbol][\con_freq].preset.presets.size - 1},
							//previous preset
							{data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value =
							data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value - 1},
							//next preset
							{data.conductor[conductors[i].asSymbol][\con_freq].preset.set(
								data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value + 1);
							data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value =
							data.conductor[conductors[i].asSymbol][\con_freq].preset.presetCV.value + 1}];
					var states = [["LD"], ["SV"], ["+"], ["-"], ["<"], [">"]];

				slotGrid[i][3].addSpanning(defs.nuPGButton(
					[states[k]], 25, 30).action_({
					var copy;
					actions[k].value;

				}),
				0, k,  align: \top)
			};


			presetNumberBox[i] = defs.nuPGNumberBox(25, 30);

			slotGrid[i][3].addSpanning(presetNumberBox[i],
				0, 6, align: \top);

			//slider
			interpolationSlider[i] = defs.nuPGSlider(25, 180,
				backgroundColor: Color.gray(0.9));
			interpolationSlider[i].mouseUpAction_({if( recButtons[i].value == 1, {recButtons[i].valueAction = 2},{});});

			slotGrid[i][3].addSpanning(interpolationSlider[i],0, 7,columnSpan: 4, align: \top);


			//slider record, play buttons
			recButtons[i] = defs.nuPGButton(
					[["R", Color.white, Color.grey],
					 ["R", Color.black, Color.new255(250, 100, 90)],
					 ["P", Color.white, Color.black]], 25, 25);
				recButtons[i].action_({|butt|
							var st = butt.value;
							switch(st,
						0, {recordTasks[i].stop; playbackTasks[i].stop;},
					    1, {playbackTasks[i].stop; recordTasks[i].play},
						2, {playbackTasks[i].play; recordTasks[i].stop}
					)});

			slotGrid[i][3].addSpanning(recButtons[i], 0, 12, align: \top);

			//number
			targetPresetNumberBox[i] = defs.nuPGNumberBox(25, 30);

			slotGrid[i][3].addSpanning(targetPresetNumberBox[i],
				0, 14, align: \top);

			2.collect{|l|
				var actions = [
					{targetPresetNumberBox[i].valueAction_(targetPresetNumberBox[i].value - 1)},
					{targetPresetNumberBox[i].valueAction_(targetPresetNumberBox[i].value + 1)}
				];
				slotGrid[i][3].addSpanning(defs.nuPGButton(
					[[">"]], 25, 30).
				action_(actions[l]),
				0, 15+l, align: \top)
			};
			};


		3.collect{|i|
			viewLayout[i].addSpanning(slots[i][0], 0, 0, columnSpan: 3);
			viewLayout[i].addSpanning(slots[i][1], 1, 0, columnSpan: 2);
			viewLayout[i].addSpanning(slots[i][2], 1, 2, columnSpan: 1);
			viewLayout[i].addSpanning(slots[i][3], 2, 0, columnSpan: 2);

		};



		3.collect{|i| stack.add(view[i])};

		//return a window
		//^window.front
	}

	visible {|boolean|
		^window.visible = boolean
	}
}