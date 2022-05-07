NuPG_GUI_Graph_Editor {

	var <>window;
	var <>stack;
	var <>multislider;
	var <>scanningSlider;
	var <>numBox;
	var <>menu;
	var <>loadFolder, <>defFolder, <>splitFile;
	var <>dataToPDF;
    var <>tablePath, <>defaultTablePath;

	build {|colorScheme = 0, coordinates, data, paramName, dataSlot|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		var files = {|tablePath| ["/*.wav", "/*.aiff"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
		var fileNames = files.value(defaultTablePath).collect{|i| PathName(i).fileName};
		var dataPlotter = NuPG_Plotter.new;
		var copyData = NuPG_DataCopy;


		window = Window.new(paramName,
			Rect.fromArray(defs.nuPGDimensions[11 + coordinates]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//add copy/paste functionality
		3.collect{|i|
			view[i].keyDownAction_({arg view,char,modifiers,unicode,keycode;
				//copy
				if(keycode == 8,

					{copyData.copyData(data.data_sequencer[i][dataSlot].input);
						"copy data".postln},
					{});
                //paste
				if(keycode == 35,
					{   //update the CV
						data.data_sequencer[i][dataSlot].input = copyData.pasteData;
						"paste data".postln},
					{})
			})
		};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};
    	3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]).spacing_(1))};

        //4 slots for 3 stacks
		slots = 3.collect{ 5.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 5.collect { GridLayout.new().margins_([3,3,3,3]) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 5.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };

		multislider = 3.collect{};
		scanningSlider = 3.collect{};
		numBox = 3.collect{2.collect{}};
		//menus
		menu = 3.collect{};
		//load folder
		loadFolder = 3.collect{};
		defFolder = 3.collect{};
		splitFile = 3.collect{};
		//save data as PDF
		dataToPDF =  3.collect{};

		3.collect{|i|
			var label = ["_max", "_min"];
			var shiftT = [0, 8];
			var shiftN =[9, 1];

			//scanningSlider
			scanningSlider[i] = defs.nuPGSlider()
				.thumbSize_(1)
				.knobColor_(Color.new255(250, 100, 90))
				.background_(Color.gray(1, 0))
				.visible_(0);

			slotGrid[i][0].addSpanning(scanningSlider[i], 0, 0, columnSpan: 20, rowSpan: 10);

			//customise multislider
			multislider[i] = defs.nuPGMultislider()
			.background_(Color.gray(1, 0))
			.action_({|ms|

				data.data_sequencer[i][dataSlot].value = ms.value;

			});

			slotGrid[i][0].addSpanning(multislider[i], 0, 0, columnSpan: 20, rowSpan: 10);

            2.collect{|l|
				slotGrid[i][0].addSpanning(defs.nuPGText(label[l], 20, 30), shiftT[l], 21);
				numBox[i][l] = defs.nuPGNumberBox(20, 30);
			    slotGrid[i][0].addSpanning(numBox[i][l], shiftN[l], 21);
			};



		};


			/*3.collect{|i|

		2.collect{|l|
			numBox[i][l] = defs.nuPGNumberBox(20, 20);
			slotGrid[i][4].addSpanning(numBox[i][l], 0, shiftN[l]);
			}
		};*/


		3.collect{|i|

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
						data.data_sequencer[i][dataSlot].value_(array);
					},{"cancelled".postln}
					)},
					//save action
					//opens by default the TABLES directory of the app
					{Dialog.savePanel({ arg path, recHeaderFormat = "wav";

						},{"cancelled".postln}, path: tablePath
					)}
				];

				slotGrid[i][2].addSpanning(defs.nuPGButton([states[l]], 20, 50)
					.mouseDownAction_(actions[l])
					, 0, 0 + l);

			};

			menu[i] = defs.nuPGMenu(defState: 1);
			slotGrid[i][2].addSpanning(menu[i], 0, 2);
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

				data.data_sequencer[i][dataSlot].value = array;

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
			slotGrid[i][2].addSpanning(loadFolder[i], 0, 3);

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
			slotGrid[i][2].addSpanning(defFolder[i], 0, 4);

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
			slotGrid[i][2].addSpanning(splitFile[i], 0, 5);

			dataToPDF[i] = defs.nuPGButton([["toPDF"]], 20, 50)
			.action_({dataPlotter.saveData(data.data_sequencer[i][dataSlot].value, 400, 100)});

			slotGrid[i][2].addSpanning(dataToPDF[i], 0, 6);

		};


		3.collect{|i|
			//SLOT 2 - norm, reverse, invert

			4.collect{|l|
				var states = ["norm", "R", "I", "F"];
			    var action = [
					//normalize to 0, 1
					{
						var array;

						array = data.data_sequencer[i][dataSlot].value.deepCopy;
						array = array.normalize;
						data.data_sequencer[i][dataSlot].value = [0.0,1.0].asSpec.map(array);
					},
					//reverse
					{
						var array;

						array = data.data_sequencer[i][dataSlot].value.deepCopy;
						array = array.reverse;
						data.data_sequencer[i][dataSlot].value = array;
					},
					//invert
					{
						var array;

						array = data.data_sequencer[i][dataSlot].value.deepCopy;
						array = array.invert;
						data.data_sequencer[i][dataSlot].value = array;
					},
					//flop
					{
						var array;

						array = data.data_sequencer[i][dataSlot].value.deepCopy;
						array = array.linlin(0.0, 1.0, 0.0, -1.0);
						data.data_sequencer[i][dataSlot].value = array.linlin(-1.0, 0.0, 0.0,1.0);
					}
				];


				slotGrid[i][3].addSpanning(defs.nuPGButton([[states[l]]], 20, 50)
					.mouseDownAction_(action[l]), 0, l);
			};

		};



        3.collect{|i|
			viewLayout[i].addSpanning(slots[i][0], 0, 0, columnSpan: 10);
			//viewLayout[i].addSpanning(slots[i][4], 0, 11);
			viewLayout[i].addSpanning(slots[i][2], 1, 0);
			viewLayout[i].addSpanning(slots[i][3], 1, 1);
		};



		3.collect{|i| stack.add(view[i])};


		//^window.front

	}
	visible {|boolean|
		^window.visible = boolean
	}


}