NuPG_Mapping {

	var <>sequencerPatt, <>sequencerTask;
	var <>pattVals;
	var <>scrubberPatt;

	mapDataToGUI {|data, gui = #[]|

		var getAllData = data.getAllData;
		var main, perGrain;
		var sequencer, sequencerSpeed, sequencerRanges;
		var matrix, modulators, wavefold;
		var probabilityMask, burstMask, channelMask, sieveMask, sieveSequence;
		var pulsaret, envelope, frequency, pulsaretShaper;
		var pulsaretFFT;
		var envDftZoom;
		var scrubbPosition;
		var trainConductors = [\con_1, \con_2, \con_3];

		# main, perGrain,
		sequencer, sequencerSpeed, sequencerRanges,
		matrix, modulators, wavefold,
		probabilityMask, burstMask, channelMask, sieveMask, sieveSequence,
		pulsaret, envelope, frequency, pulsaretShaper, pulsaretFFT, envDftZoom, scrubbPosition = getAllData.size.collect{|i| getAllData[i]};


		//mapping
		//3 trains
		^3.collect{|i|
			//main
			5.collect{|l| main[i][l].connect(gui[0].sliders[i][l]);
			                  main[i][l].connect(gui[0].numberBoxes[i][l])
			};
			//pergrain
			3.collect{|l| perGrain[i][l].connect(gui[1].sliders[i][l]);
				              perGrain[i][l].connect(gui[1].numberBoxes[i][l])
			};
			//sequencer
			5.collect{|l| sequencer[i][l].connect(gui[2].multisliders[i][l])};
			//sequencerRanges
			5.collect{|l| 2.collect{|k| sequencerRanges[i][l][k].connect(gui[2].ranges[i][l][k]) }};
			//local scrubber
			5.collect{|l| data.data_scrubberPositionLocal[i][l].connect(gui[2].scrubberSlider[i][l])};
			//sequencerSpeed
			sequencerSpeed[i][0].connect(gui[3].slider[i]);
			sequencerSpeed[i][0].connect(gui[3].numberBox[i]);
			//matrix
			//7.collect{|l| 4.collect{|k| matrix[i][l][k].connect(gui[4].matrix[i][l][k]) } };
			//modulators
			/*4.collect{|l| 2.collect{|k| modulators[i][l][k].connect(gui[5][l].sliders[i][k]) };
				          2.collect{|k| modulators[i][l][k].connect(gui[5][l].numBoxes[i][k]) }
			};
			//wavefold
			4.collect{|l| 2.collect{|k| wavefold[i][l][k].connect(gui[5][l].wf_ranges[i][k]) }};*/
			//probabilityMask
			probabilityMask[i][0].connect(gui[4].probabilityNumberBox[i]);
			//burstMask
			2.collect{|l| burstMask[i][l].connect(gui[4].burstNumberBoxes[i][l]) };
			//channelMask
			channelMask[i][0].connect(gui[4].channelNumberBox[i]);
			//sieve mask
			sieveMask[i][0].connect(gui[5].sieveOnOff[i]);
			//sieve sequence
			//sieveSequence[i][0].connect(gui[7].sieveSequence[i] );
			//tables
			pulsaret[i].connect(gui[6].multisliders[i][0]);
			envelope[i].connect(gui[6].multisliders[i][1]);
			frequency[i].connect(gui[6].multisliders[i][2]);
			//table editors
			//pulsaret
			pulsaret[i].connect(gui[7].multisliders[i]);
			//envelope
			envelope[i].connect(gui[8].multisliders[i]);
			//env dftZoom
			envDftZoom[i][0].connect(gui[8].dftZoomValue[i]);
			//frequency
			frequency[i].connect(gui[9].multisliders[i]);
			//pulsaret shaper
			pulsaretShaper[i][0].connect(gui[10].multislider[i]);
			//sequencer editors
			5.collect{|l|
				sequencer[i][l].connect(gui[11][l].multislider[i]);
				2.collect{|k| sequencerRanges[i][l][k].connect(gui[11][l].numBox[i][k]) }
			};
			//presets
			data.conductor[trainConductors[i].asSymbol].preset.presetCV.connect(gui[12].presetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol].preset.targetCV.connect(gui[12].targetPresetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol].preset.interpCV.connect(gui[12].interpolationSlider[i]);
			//pulsaretFFT
			//pulsaretFFT[i][0].connect(gui[15].multislider[i]);

			//scrubber
			scrubbPosition[i][0].connect(gui[13].slider[i]);
			//local preset - pulsaret waveform editor
			data.conductor[trainConductors[i].asSymbol][\con_pul].preset.presetCV.connect(gui[7].presetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol][\con_pul].preset.targetCV.connect(gui[7].targetPresetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol][\con_pul].preset.interpCV.connect(gui[7].interpolationSlider[i]);
			//local preset - envelope editor
			data.conductor[trainConductors[i].asSymbol][\con_env].preset.presetCV.connect(gui[8].presetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol][\con_env].preset.targetCV.connect(gui[8].targetPresetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol][\con_env].preset.interpCV.connect(gui[8].interpolationSlider[i]);
			//local preset - frequency editor
			data.conductor[trainConductors[i].asSymbol][\con_freq].preset.presetCV.connect(gui[9].presetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol][\con_freq].preset.targetCV.connect(gui[9].targetPresetNumberBox[i]);
			data.conductor[trainConductors[i].asSymbol][\con_freq].preset.interpCV.connect(gui[9].interpolationSlider[i]);


		}
	}

	mapDataToParams {|data, ndefs|

		var getAllData = data.getAllData;
		var main, perGrain;
		var sequencer, sequencerSpeed, sequencerRanges;
		var matrix, modulators, wavefold;
		var probabilityMask, burstMask, channelMask, sieveMask, sieveSequence;
		var pulsaret, envelope, frequency, pulsaretShaper;

		# main, perGrain,
		sequencer, sequencerSpeed, sequencerRanges,
		matrix, modulators, wavefold,
		probabilityMask, burstMask, channelMask, sieveMask, sieveSequence,
		pulsaret, envelope, frequency, pulsaretShaper = getAllData.size.collect{|i| getAllData[i]};

		//nuPG core mapping
		3.collect{|i|
			ndefs.trains[i].setControls([
				trigFreq:  main[i][0],
				grainFreq: main[i][1],
				envRateMult:  main[i][2],
				pan: main[i][3],
				ampMain:  main[i][4],
				fmAmt:  perGrain[i][0],
				fmRatio:  perGrain[i][1],
				allFluxAmt:  perGrain[i][2],
				//masking parameters
			    probability: probabilityMask[i][0],
				burst: burstMask[i][0],
				rest: burstMask[i][1],
				chanMask: channelMask[i][0],
				centerMask: channelMask[i][1],
				sieveMaskOn: sieveMask[i][0],
				sieveMod: sieveMask[i][1],
				sieveSequence: sieveSequence[i][0]
			]);
		}
	}

	mapDataToSequencers {|data|

		var patEvent;

		var defValue = [1,1,1,0,1]!3;

		pattVals = (1!5)!3;

		//three trains
		sequencerPatt = 3.collect{|i|
			//5 parameters of a sequencer
			5.collect{|n|
				Prout({loop{ data.data_sequencer[i][n].value.do(_.yield)}}).linlin(0.0, 1.0,
					data.data_sequencerRanges[i][n][0],
					data.data_sequencerRanges[i][n][1]);
			}
		};


	sequencerTask = 3.collect{|i|
			Tdef(("microTask_"++(1+i).asString).asSymbol, {|ev|

			var names = ["trigFreqMicro_", "grainFreqMicro_", "envMultMicro_", "panMicro_",
			"ampMicro_"];
		    var patt = names.size.collect{|l|
					PL((names[l]++(1+i).asString).asSymbol).loop.asStream};

				inf.do({
					NuPG_Ndefs.trains[i].set(

					\micro_trigFreqMult, patt[0].next,
					\micro_grainMult, patt[1].next,
					\micro_envRateMult, patt[2].next,
					\micro_panMult, patt[3].next,
					\micro_ampMult, patt[4].next);
					pattVals[i] = [patt[0].next, patt[1].next, patt[2].next, patt[3].next, patt[4].next];
					data.data_sequencerSpeed[i][0].value.reciprocal.wait
				})
			})
		}
	}

	task { ^sequencerTask }

	scrubberTask {|data, sequencer|

		var task;

		task = 3.collect{|i|
			Tdef(("scrubTask_"++(1+i).asString).asSymbol, {|ev|

				inf.do({
					//get scrubber position and resize to sequencer size - 2048
					var index = data.data_scrubber[i][0].value;
					var indexScalled = index.linlin(0,1, 0, 2047);
					NuPG_Ndefs.trains[i].set(
						\micro_trigFreqMult, switch(ev.param1,
							0, {1},
							1, {data.data_sequencer[i][0].value.linlin(0, 1, data.data_sequencerRanges[i][0][0].value, data.data_sequencerRanges[i][0][1].value)[indexScalled];
								//display the scrubb slider
								//data.data_scrubberPositionLocal[i][0].value = index;
						}),
						\micro_grainMult, switch(ev.param2,
							0, {1},
							1, {data.data_sequencer[i][1].value.linlin(0, 1, data.data_sequencerRanges[i][1][0].value, data.data_sequencerRanges[i][1][1].value)[indexScalled];
								//display the scrubb slider
								//data.data_scrubberPositionLocal[i][1].value = index;
						}),
						\micro_envRateMult, switch(ev.param3,
							0, {1},
							1, {data.data_sequencer[i][2].value.linlin(0, 1, data.data_sequencerRanges[i][2][0].value, data.data_sequencerRanges[i][2][1].value)[indexScalled];
								//display the scrubb slider
								//data.data_scrubberPositionLocal[i][2].value = index;
						}),
						\micro_panMult, switch(ev.param4,
							0, {0},
							1, {data.data_sequencer[i][3].value.linlin(0, 1, data.data_sequencerRanges[i][3][0].value, data.data_sequencerRanges[i][3][1].value)[indexScalled];
								//display the scrubb slider
								//data.data_scrubberPositionLocal[i][3].value = index;
						}),
						\micro_ampMult, switch(ev.param5,
							0, {1},
							1, {data.data_sequencer[i][4].value.linlin(0, 1, data.data_sequencerRanges[i][4][0].value, data.data_sequencerRanges[i][4][1].value)[indexScalled];
								//display the scrubb slider
								//data.data_scrubberPositionLocal[i][4].value = index;
						})
					);
					0.05.wait
				})
			})
		};

		^task
	}

	pulsaretDataToBufferUpdater {|data, buffers|
		var task;

		task = 3.collect{|i|
			Tdef(("dataToBufferUpdaterTask_"++(1+i).asString).asSymbol, {|ev|

				inf.do({
					buffers[i][0].sendCollection(data.data_pulsaret[i].value);
					0.1.wait
				})
			})
		};

		^task
	}
}

