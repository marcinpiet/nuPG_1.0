NuPG_GUI_Table_Editor_View {

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
	var <>shiftValue;
	var <>parentView;


	draw {|name, dimensions, buffer = 0, n = 1|
		var view, viewLayout, minMaxLabel, tablesMenu;
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
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//define objects
		//generate empty placeholders for objects of size = n
		table = n.collect{};
		buttons = n.collect{ 4.collect{} }; //there are 4 functions = number of buttons required
		tablesMenu = n.collect{};
		minMaxLabel = n.collect{ 2.collect{} }; //there are 2 labels min & max
		minMaxValues = n.collect{ 2.collect{} }; //there are 2 values min & max
		shiftValue = n.collect{};
		n.collect{|i|
			//window.onClose_({ parentView.buttons[i][0].value_(0) });
			table[i] = guiDefinitions.tableView;
			table[i].size = 2048;
			table[i].action_{|ms|
				data[i].value = ms.value.linlin(0, 1, -1, 1);
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
			//S - Saves Table as an audiofile
			//L - Loads wavetable
			//RS - Resize - fit to the view
			//R - reverse (horizontal flip)
			//I - invert (vertical flip)
			//SH - shift

			//shift
			shiftValue[i] = guiDefinitions.nuPGNumberBox(20, 30);
			shiftValue[i].clipLo = 1.0;
			shiftValue[i].clipHi = 2048.0;
			shiftValue[i].value = 150;

			buttons[i] = 11.collect{|l|
				var string = [
					[["S"]],
					[["L"]],
					[["RS"]],
					[["R"]],
					[["I"]],
					[["NM"]],
					[["RM"]],
					[["←"]],
					[["→"]],
						[["↑"]],
						[["↓"]]
					];
				var action = [
					{},
					//save action
					//opens by default the TABLES directory of the app
					{Dialog.openPanel({ arg path;
						var size, file, temp, array;
						file = SoundFile.new;
						file.openRead(path);
						temp = FloatArray.newClear(4096);
						file.readData(temp);
						array = temp.asArray.resamp1(2048).copy;
						//array = array.linlin(-1.0, 1.0, -1.0, 1.0);
						data[i].value_(array);
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
					},{"cancelled".postln}
					)},
					{ var resizeData = data[i].value.linlin(data[i].value.minItem, data[i].value.maxItem, -1, 1);
						data[i].value = resizeData;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(resizeData)});

					},
					//reverse
					{
						var array;

						array = data[i].value.deepCopy;
						array = array.reverse;
						data[i].value = array;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
					},
					//invert
					{
						var array;

						array = data[i].value.deepCopy;
						array = array.invert;
						data[i].value = array;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
					},
					//multiply
					{
						var array;


						array = data[i].value.deepCopy.flat;
						array = (array * 1) % 1;
						array = array.linlin(0, 1, -1, 1);

						data[i].value = array.deepCopy;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array.deepCopy)});
					},
					// ring modulation
					{

						var array, boundaryType = 'wrap';
						var newArr;
						var imprintArr = Signal.welchWindow(2048).as(Array);

						array = data[i].value.deepCopy.flat;

						if (array.size != imprintArr.size, {
							'error: array sizes do not match'.postln;
						},
						{
							newArr = array.flat; // flattening array
							for (0, array.size-1, {
								arg i;
								var x;
								x = boundaryType ++ '(' ++ (newArr[i] * imprintArr[i]).asString  ++ ', 0, 1)';
								newArr[i] = x.interpret;
							});
						});

						data[i].value = newArr;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(newArr)});
					},
					//shift left
					{
						var array;

						array = data[i].value.deepCopy.flat;
						array = array.rotate(-15);

						data[i].value = array;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
					},
					//shift right
					{
						var array;

						array = data[i].value.deepCopy.flat;
						array = array.rotate(15);

						data[i].value = array;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
					},

					//shift up
					{
						var array;

						array = data[i].value.deepCopy.flat;
						array = array.collect{|item| item + 0.1}.wrap(-1, 1);

						data[i].value = array;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
					},
					//shift down
					{
						var array;

						array = data[i].value.deepCopy.flat;
						array = array.collect{|item| item - 0.1}.wrap(-1, 1);

						data[i].value = array;
						if(buffer == 0, {}, {setBuffer[i].sendCollection(array)});
					},
				];

				guiDefinitions.nuPGButton(string[l], 20, 20).action_(action[l]);

			};



			//tables menu
			tablesMenu[i] = guiDefinitions.nuPGMenu(defState: 1, width: 200);
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
				//copy
				if(keycode == 8,
					{dataCopyPaste.copyData(data[i].value);
						"copy data".postln},
					{});
				//paste
				if(keycode == 35,
					{   //update the CV
						//dataCopyPaste.pasteData.postln;
						data[i].value = dataCopyPaste.pasteData;
						if(buffer == 0, {},{setBuffer[i].sendCollection(dataCopyPaste.pasteData)});
						"paste data".postln;
					},
					{})
			});

		};

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		//table view editors
		n.collect{|i|
			viewLayout[i].addSpanning(item: table[i], row: 0, column: 0, rowSpan: 7, columnSpan: 16);
			//buttons
			5.collect{|l|
				viewLayout[i].add(item: buttons[i][l], row: 8, column: 0 + l);
			};

			//shift
			//viewLayout[i].add(item: guiDefinitions.nuPGStaticText("_shift", 20, 30), row: 8, column: 5);
			viewLayout[i].add(item: buttons[i][5], row: 8, column: 6);
			viewLayout[i].add(item: buttons[i][6], row: 8, column: 7);
			viewLayout[i].add(item: buttons[i][7], row: 8, column: 8);
			viewLayout[i].add(item: buttons[i][8], row: 8, column: 9);
			viewLayout[i].add(item: buttons[i][9], row: 8, column: 10);
			viewLayout[i].add(item: buttons[i][10], row: 8, column: 11);


			//tables menu
			viewLayout[i].add(item: tablesMenu[i], row: 8, column: 13);

			//minimum - maximum
			2.collect{|l|
				var shift = [0, 5];
				viewLayout[i].add(item: minMaxLabel[i][l], row: 0 + shift[l], column: 16);
				viewLayout[i].add(item: minMaxValues[i][l], row: 1 + shift[l], column: 16);
			};

		};

		//load views into stacks
		n.collect{|i|
			stack.add(view[i])
		};

		//^window.front;

	}

	visible {|boolean|
		^window.visible = boolean
	}

}