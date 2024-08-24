NuPG_GUI_Table_View {

	var <>window;
	var <>stack;
	var <>table;
	var <>data;
	var <>setBuffer;
	var <>minMaxValues;
	var <>tablePath, <>defaultTablePath;
	var <>pattern;
	var <>patternUpdate;
	var <>buttons;
	var <>editorView;
	var <>tablesMenu;


	draw {|name, dimensions, buffer = 0, n = 1|
		var view, viewLayout, minMaxLabel;
		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		var dataCopyPaste = NuPG_Data_CopyPaste.new;
		//tables menu paths
		var files = {|tablePath| ["/*.wav", "/*.aiff"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
		var fileNames = files.value(defaultTablePath).collect{|i| PathName(i).fileName};

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window(name, dimensions, resizable: false);
		window.userCanClose = false;
		window.view.background_(guiDefinitions.bAndKGreen);
		window.userCanClose = false;
		//window.alwaysOnTop_(true);
		//failed attempt at drawing a grid for display
		//draw
		/*window.drawFunc = {
		Pen.strokeColor = Color.blue;
		Pen.stringAtPoint( "--------------------------------- 2048", (15@0) - (0@0) );
		Pen.stringAtPoint( "---------------------------------       0", (15@105)  - (0@0));
		Pen.strokes

		};*/

		//load stackLayaut to display multiple instances on top of each other
		window.layout_(stack = StackLayout.new() );
		//Unlike other layouts, StackLayout can not contain another layout, but only subclasses of View
		//solution - load a CompositeView and use GridLayout as its layout
		//n = number of instances set a build time, default n = 1, we need at least one instance
		//maximum of instances is 10
		view = n.collect{|i| guiDefinitions.nuPGView(guiDefinitions.colorArray[i])};
		//generate corresponding number of gridLayouts to load in to CompositeView
		//Grid Laayout
		viewLayout = n.collect{|i|
			GridLayout.new()
			.hSpacing_(3)
			.vSpacing_(3)
			.spacing_(1)
			.margins_([5, 5, 5, 5]);
		};
		//load gridLayouts into corresponding views
		n.collect{|i| view[i].layout_(viewLayout[i])};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		data = n.collect{};
		setBuffer = n.collect{};
		pattern = n.collect{};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//define objects
		//generate empty placeholders for objects of size = n
		table = n.collect{};
		buttons = n.collect{ 4.collect{} }; //there are 4 functions = number of buttons required
		tablesMenu = n.collect{};
		minMaxLabel = n.collect{ 2.collect{} }; //there are 2 labels min & max
		minMaxValues = n.collect{ 2.collect{} }; //there are 2 values min & max

		n.collect{|i|
			table[i] = guiDefinitions.tableView;
			table[i].size = 2048;
			table[i].action_{|ms|
				data[i].value = ms.value.linlin(0, 1, -1, 1);
				//pattern[i] = data[i].value;
				if(buffer == 0, {},{setBuffer[i].sendCollection(data[i].value)});
			};
			table[i].mouseDownAction_{|ms| };
			table[i].mouseUpAction_{|ms| };

			//minimum - maximum label and values
			minMaxLabel[i] = 2.collect{|l|
				var string = ["_max", "_min"];
				var label =  guiDefinitions.nuPGStaticText(string[l], 15, 30);
				label;

			};

			minMaxValues[i] = 2.collect{|l|
				var numberBox = guiDefinitions.nuPGNumberBox(15, 30);
				numberBox;
			};

			minMaxValues[i][0].mouseDownAction_{|num|
				var newVal = num.value;
				var resizeData = data[i].value.linlin(data[i].value.minItem, data[i].value.maxItem, data[i].value.minItem, newVal);
				//data[i].value = resizeData;
				if(buffer == 0, {}, {setBuffer[i].sendCollection(resizeData)});

			};
			minMaxValues[i][1].mouseDownAction_{|num|
				var newVal = num.value;
				var resizeData = data[i].value.linlin(data[i].value.minItem, data[i].value.maxItem, newVal, data[i].value.maxItem);
				//data[i].value = resizeData;
				if(buffer == 0, {}, {setBuffer[i].sendCollection(resizeData)});

			};

			minMaxValues[i][0].mouseUpAction_{|num|
				var newVal = num.value;
				var resizeData = data[i].value.linlin(data[i].value.minItem, data[i].value.maxItem, data[i].value.minItem, newVal);
				//data[i].value = resizeData;
				if(buffer == 0, {}, {setBuffer[i].sendCollection(resizeData)});

			};
			minMaxValues[i][1].mouseUpAction_{|num|
				var newVal = num.value;
				var resizeData = data[i].value.linlin(data[i].value.minItem, data[i].value.maxItem, newVal, data[i].value.maxItem);
				//data[i].value = resizeData;
				if(buffer == 0, {}, {setBuffer[i].sendCollection(resizeData)});

			};

			//buttons definition
			//E - Large Table Editor
			//S - Saves Table as an audiofile
			//L - Loads wavetable
			//RS - Resize - fit to the view
			buttons[i] = 4.collect{|l|
				var string = [
					[["E"],["E", guiDefinitions.white, guiDefinitions.darkGreen]],
					[["S"]],
					[["L"]],
					[["RS"]]
				];
				var action = [
					{|butt|
						var st = butt.value; st.postln;
						switch(st,
							0, {editorView.visible(0); n.collect{|l| buttons[l][0].value_(0) }},
							1, {editorView.visible(1); n.collect{|l| buttons[l][0].value_(1) }}
						)
					},
					{
						if(buffer == 0,
							{var tempBuf = Buffer.alloc(Server.default, 2048);
									tempBuf.sendCollection(data[i].value);
								Dialog.savePanel({ arg path, recHeaderFormat = "wav";

									tempBuf.write(
										path: path++"."++ recHeaderFormat,
										headerFormat: "wav",
										sampleFormat: "int24",
										numFrames: 2048);
								});
								tempBuf.clear;
							},
								{
									Dialog.savePanel({ arg path, recHeaderFormat = "wav";

										setBuffer[i].write(
											path: path++"."++ recHeaderFormat,
											headerFormat: "wav",
											sampleFormat: "int24",
											numFrames: 2048);
									},{"cancelled".postln}, path: tablePath
						)}
					)},

							{Dialog.openPanel({ arg path;
								var size, file, temp, array;
								file = SoundFile.new;
								file.openRead(path);
								temp = FloatArray.newClear(2048);
								file.readData(temp);
								array = temp.asArray.resamp1(2048).copy;
								array = array.linlin(-1.0, 1.0, -1.0, 1.0);
								data[i].value_(array);
								if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
							},{"cancelled".postln}
							)},
							{ var resizeData = data[i].value.linlin(data[i].value.minItem, data[i].value.maxItem, -1, 1);
								data[i].value = resizeData;
								if(buffer == 0, {}, {setBuffer[i].sendCollection(resizeData)});}
				];

						guiDefinitions.nuPGButton(string[l], 20, 20).action_(action[l]);

					};

					//tables menu
					tablesMenu[i] = guiDefinitions.nuPGMenu(defState: 1);
					tablesMenu[i].items = [];
					tablesMenu[i].items = fileNames;

					tablePath = defaultTablePath;

					tablesMenu[i].action_({|item|
						var size, dataFile, file, temp, array;
						dataFile = tablePath ++ fileNames[tablesMenu[i].value];
						file = SoundFile.new;
						file.openRead(dataFile);
						temp = FloatArray.newClear(4096);
						file.readData(temp);
						array = temp.asArray.resamp1(2048).copy;
						//array = array.linlin(-1.0, 1.0, 0.0, 1.0);

						data[i].value = array;
						if(buffer == 0, {},{setBuffer[i].sendCollection(data[i].value)});

						fileNames[tablesMenu[i].value].postln;
					});
					view[i].keyDownAction_({arg view,char,modifiers,unicode,keycode;
				//[view,char,modifiers,unicode,keycode].postln;
				//mix with data from clipboard
				if(keycode == 46,
							{var dataFromClipboard = dataCopyPaste.pasteData;
						var currentData = data[i].value;
						var mixedData = currentData * dataFromClipboard;
						data[i].value = mixedData;
								if(buffer == 0, {},{setBuffer[i].sendCollection(mixedData)});
								},
							{});

						//copy
						if(keycode == 8,
							{dataCopyPaste.copyData(data[i].value);
								"copy table".postln},
							{});
						//paste
						if(keycode == 35,
							{   //update the CV
								//dataCopyPaste.pasteData.postln;
								data[i].value = dataCopyPaste.pasteData;
								if(buffer == 0, {},{setBuffer[i].sendCollection(dataCopyPaste.pasteData)});
								"paste table".postln;
							},
							{});
				        //reverse
						if(keycode == 15,
							{   var copy = data[i].value;
								data[i].value = copy.reverse;
								if(buffer == 0, {},{setBuffer[i].sendCollection(copy.reverse)});
								"reverse table".postln;
							},
							{});
				//invert
						if(keycode == 34,
							{   var copy = data[i].value;
								data[i].value = copy.invert;
								if(buffer == 0, {},{setBuffer[i].sendCollection(copy.invert)});
								"invert table".postln;
							},
							{})
					});

				};

				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				//place objects on view
				//table view editors
				n.collect{|i|
					viewLayout[i].addSpanning(item: table[i], row: 0, column: 0, rowSpan: 7, columnSpan: 8);
					//buttons
					4.collect{|l|
						viewLayout[i].add(item: buttons[i][l], row: 8, column: 0 + l);
					};

					//tables menu
					viewLayout[i].add(item: tablesMenu[i], row: 8, column: 4);

					//minimum - maximum
					2.collect{|l|
						var shift = [0, 5];
						viewLayout[i].add(item: minMaxLabel[i][l], row: 0 + shift[l], column: 8);
						viewLayout[i].add(item: minMaxValues[i][l], row: 1 + shift[l], column: 8);
					};

				};



				//load views into stacks
				n.collect{|i|
					stack.add(view[i])
				};
		window.visible_(0);

		^window.front;
			}

			visible {|boolean|
				^window.visible = boolean
			}
		}