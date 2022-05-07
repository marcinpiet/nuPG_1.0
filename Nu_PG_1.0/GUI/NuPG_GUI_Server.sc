NuPG_GUI_Server {

	var <>window;
	var <>fileName;
	var <>recFolder;
	var <>timeStamp;
	var <>fullRecordingPath;
	var <>numChan = 2;

	build {|data, buffers, pattern, views, map, colorScheme = 0|

		var view, layout, slotGrid, slots, actions;
		var formatList;
		var defs = NuPG_GUI_definitions;
		var server;


		window = Window.new("SERVER",
			Rect.fromArray(defs.nuPGDimensions[6]), resizable: false);
		window.userCanClose = false;
		window.layout_(layout = GridLayout.new().margins_([3,3,3,3]).vSpacing_(2).hSpacing_(2));

		//how many slots?
		slots = 2.collect{defs.nuPGView(colorScheme) };
		slotGrid = 2.collect{GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5) };
		//layout for each view
		//add content to each slot
		2.collect{|i| slots[i].layout_(slotGrid[i])};

		slotGrid[0].addSpanning(defs.nuPGText("_OUTPUT", 20, 70), 0, 0);
		//server output
		server = defs.nuPGMenu(ServerOptions.outDevices, 0)
		.action_({|sl|
			var st = sl.value;
			//select an input device
			Server.default.options.outDevice = ServerOptions.outDevices[st];
			//post selection
			("nuPG OUTPUT: " ++ ServerOptions.outDevices[st]).postln;
		});
		slotGrid[0].addSpanning(server, 0, 1);

		//server boot
		server = defs.nuPGButton(
			[   [ "BOOT", Color.white, Color.grey],
				[ "FREE", Color.black, Color.new255(250, 100, 90)]], 20, 70)
		.action_({|sl| var st = sl.value; st.postln;
			switch(st,
				0, {Server.default.quit},
				1, {Server.default.waitForBoot(
					{
						var connection, names, default, params;


						//allocate required memory on the server for buffers
						3.collect{|i| 3.collect{|l| buffers[i][l].alloc } };
						//set buffers to Ndef definitions
						3.collect{|i|
							Ndef(("train_" ++ (1+i).asString).asSymbol).reshaping_(\elastic)
							.set(
								\pulBuf, buffers[i][0],
								\envBuf, buffers[i][1],
								\freqBuf, buffers[i][2]);
						};

						names = [
								[\trigFreqMicro_1, \grainFreqMicro_1, \envMultMicro_1, \panMicro_1, \ampMicro_1],
								[\trigFreqMicro_2, \grainFreqMicro_2, \envMultMicro_2, \panMicro_2, \ampMicro_2],
								[\trigFreqMicro_3, \grainFreqMicro_3, \envMultMicro_3, \panMicro_3, \ampMicro_3]
							];
						default = [1,1,1,0,1];
						params = 3.collect{|i|
								5.collect{|l|
									currentEnvironment.put(names[i][l], default[l]);

									//currentEnvironment.put(names[i][l], pattern.microSeqData[i][l]);
								}
							};

/*						3.collect{|i|
							var defVal = [1,1,1,1,1,0,1];
							7.collect{|l|
								var param = [
									\trigFreqMod,
									\grainFreqMod,
									\envMod,
									\fmAmtMod,
									\fmRatioMod,
									\panMod,
									\ampMod];
								NuPG_Ndefs.trains[i].set(param[l],

									Ndef(("modParam_"++i++l).asSymbol));

								Ndef(("modParam_"++i++l).asSymbol).set(\modOn, 0);
								Ndef(("modParam_"++i++l).asSymbol).set(\def, defVal[l]);
			};

		};*/
						//DEFAULT VALUES
						/*3.collect{|i|
							//main
							data.data_main[i][0].value = 567.67;
							data.data_main[i][1].value = 1;
							data.data_main[i][2].value = 1;
							data.data_main[i][3].value = 0;
							data.data_main[i][4].value = 0.5;
							//ppModulation
							data.data_ppModulation[i][0].value = 0;
							data.data_ppModulation[i][1].value = 0;
							data.data_ppModulation[i][2].value = 0;
							//modulators
							4.collect{|l|
								data.data_modulators[i][l][0].value = 3;
								data.data_modulators[i][l][1].value = 1;
							};
							//micro sequencer
							names = [
								[\trigFreqMicro_1, \grainFreqMicro_1, \envMultMicro_1, \panMicro_1, \ampMicro_1],
								[\trigFreqMicro_2, \grainFreqMicro_2, \envMultMicro_2, \panMicro_2, \ampMicro_2],
								[\trigFreqMicro_3, \grainFreqMicro_3, \envMultMicro_3, \panMicro_3, \ampMicro_3]
							];

							default = [1,1,1,0,1];
							params = 3.collect{|i|
								5.collect{|l|
									currentEnvironment.put(names[i][l], default[l]);

									//currentEnvironment.put(names[i][l], pattern.microSeqData[i][l]);
								}
							};
					}*/}
				).doWhenBooted({
					//var modulators = NuPG_Ndefs.modulators;
                        //var modulatorsAll = NuPG_Ndefs.modulatorsParam;

                        //set default Ndef synth
						3.collect{|i| NuPG_Ndefs.trains[i][0] = NuPG_Ndefs.nuPG_AdC(2) };
                        map.mapDataToParams(data, NuPG_Ndefs);
					    map.mapDataToSequencers(data);

				});
			})
		});
		slotGrid[0].addSpanning(server, 0, 2);
		//////////////////////////////////////////////////////////////////////////
		//recording
		fileName = {"nuPg"};
		recFolder = {thisProcess.platform.recordingsDir}; //default recording folder

		fullRecordingPath = {
			recFolder.value +/+ fileName.value ++
			"_" ++ Date.getDate.format("(%d.%m.%Y %H:%M)") ++
			"." ++ Server.default.recHeaderFormat};


		slotGrid[1].addSpanning(defs.nuPGButton(
			[[ "RECORD", Color.white, Color.grey],
				[ "STOP", Color.black, Color.new255(250, 100, 90)]], 20, 70)
		.action_({|sl| var st = sl.value; st.postln;
			switch(st,
				0, {Server.default.stopRecording},
				1, {Server.default.record(fullRecordingPath.value)}
			)
		}),
		0, 0);

		formatList = ["aiff", "wav", "caf"];
		slotGrid[1].addSpanning(defs.nuPGMenu(formatList, 0)
			.action_({|menu| Server.default.recHeaderFormat = formatList[menu.value];
				("RECORDING FORMAT SET TO: " ++ formatList[menu.value].asString).postln;
				Server.default.prepareForRecord(fullRecordingPath.value)
			}),
			0, 1);

		slotGrid[1].addSpanning(defs.nuPGButton([["_DIR"]], 20, 35)
			.action_({
				var getPath;

				FileDialog({|path| getPath = path[0];
					recFolder = path[0];
					("NEW ROCORDING DIRECTORY SET TO:" ++ path[0].asString).postln;
				},
				fileMode: 2);




			}),
			0, 3);
		slotGrid[1].addSpanning(defs.nuPGTextField("nuPg", 20, 150)
			.action_({|field|
				fileName = field;
				("FILE NAME CHANGED TO: " ++ field.value.asString).postln;
			})
			,0, 4);


       2.collect{|i| layout.addSpanning(slots[i], i, 0)};

		^window.front

	}

}