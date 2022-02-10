//the class connects GUI with data
NuPG_Connection {

	connect {|mainGUI, tablesGUI, pulsaretGUI, envelopeGUI, frequencyGUI, sequencerMicroGUI, controlGUI, ppModulationGUI, sequencerMicroSpeedGUI, pulsaretShaperGUI, scrubberGUI, graphEditorGUI, maskingGUI, wfModulationGUI, modulatorsGUI, parameterLinkGUI, data, buffers, synth, modulators, modMatrix|

		var connection;
		var mainParams = [\trigFreq, \grainFreq, \envRateMult, \pan, \ampMain];

		var reshapeGraphMultisliders = 3.collect{|i|
			5.collect{|l| graphEditorGUI[l].multislider[i] }
		};

		var reshapeGraphScanningSliders = 3.collect{|i|
			5.collect{|l| graphEditorGUI[l].scanningSlider[i] }
		};

		var reshapeGraphRanges = 3.collect{|i| 5.collect{|l| 2.collect{|k| graphEditorGUI[l].numBox[i][k] }.swap(0,1) } };

		connection = ConnectionList.make {
			//iterate over three nuPG trains
			3.collect{|i|
				//format data

				//train control
			/*	data.data_trains[i].connectEach(\value, controlGUI.trainButtons[i], _.valueSlot);
				data.data_trains[i].connectEach(\input, controlGUI.trainButtons[i], _.valueSlot);
				controlGUI.trainButtons[i].connectEach(\valueAction, data.data_trains[i], _.valueSlot);
				controlGUI.trainButtons[i].connectEach(\input, data.data_trains[i], _.valueSlot);*/

				//data main
				data.data_main[i].connectEach(\input, mainGUI.sliders[i], _.valueSlot);
				data.data_main[i].connectEach(\value, mainGUI.numberBoxes[i], _.valueSlot);
				//GUI main
				mainGUI.sliders[i].connectEach(\value, data.data_main[i], _.inputSlot);
				mainGUI.numberBoxes[i].connectEach(\value, data.data_main[i], _.valueSlot);

				//masking
				data.data_masking[i].connectEach(\value, maskingGUI.numberBoxes[i], _.valueSlot);
				maskingGUI.numberBoxes[i].connectEach(\value, data.data_masking[i], _.valueSlot);

				//data ppModulation
				data.data_ppModulation[i].connectEach(\input, ppModulationGUI.sliders[i], _.valueSlot);
				data.data_ppModulation[i].connectEach(\value, ppModulationGUI.numberBoxes[i], _.valueSlot);
				//GUI ppModulation
				ppModulationGUI.sliders[i].connectEach(\value, data.data_ppModulation[i], _.inputSlot);
				ppModulationGUI.numberBoxes[i].connectEach(\value, data.data_ppModulation[i], _.valueSlot);

				//data wf ranges
				4.collect{|l|
					data.data_modulation_wf_ranges[i][l].connectEach(\value,
						modulatorsGUI[l].wf_ranges[i], _.valueSlot);

					modulatorsGUI[l].wf_ranges[i].connectEach(\value,
						data.data_modulation_wf_ranges[i][l], _.valueSlot);
				};


				//modulators
				4.collect{|l|
					data.data_modulators[i][l].connectEach(\input, modulatorsGUI[l].sliders[i], _.valueSlot);
					modulatorsGUI[l].sliders[i].connectEach(\value, data.data_modulators[i][l], _.inputSlot);
					data.data_modulators[i][l].connectEach(\value, modulatorsGUI[l].numBoxes[i], _.valueSlot);
					modulatorsGUI[l].numBoxes[i].connectEach(\value, data.data_modulators[i][l], _.valueSlot);

					data.data_modulatorType[i][l].connectEach(\value, modulatorsGUI[l].modulatorType[i], _.valueSlot);
					modulatorsGUI[l].modulatorType[i].connectEach(\value, data.data_modulatorType[i][l], _.valueSlot);

				};

				7.collect{|l|
					//matrix
					data.data_wfModulation_matrix[i][l].connectEach(\value,
						wfModulationGUI.matrix[i][l], _.valueSlot);
					wfModulationGUI.matrix[i][l].connectEach(\value,
						data.data_wfModulation_matrix[i][l], _.valueSlot);
				};

				5.collect{|l|
				//offset
					data.data_wfModulation_matrix_offset[i][l].connectEach(\value,
						wfModulationGUI.offset[i][l], _.valueSlot);
					wfModulationGUI.offset[i][l].connectEach(\value,
						data.data_wfModulation_matrix_offset[i][l], _.valueSlot);
				};

				7.collect{|l|
					//polarity
					data.data_wfModulation_matrix_polarity[i][l].connectEach(\value,
						wfModulationGUI.polarity[i][l], _.valueSlot);
					wfModulationGUI.polarity[i][l].connectEach(\value,
						data.data_wfModulation_matrix_polarity[i][l], _.valueSlot);
				};

				//data micro sequencer
                data.data_microSequencer[i].connectEach(\input, sequencerMicroGUI.multisliders[i], _.valueSlot);
				data.data_microSequencer[i].connectEach(\value, sequencerMicroGUI.multisliders[i], _.valueSlot);
				//GUI micro sequencer
				sequencerMicroGUI.multisliders[i].connectEach(\value, data.data_microSequencer[i], _.valueSlot);
				//ranges micro sequencer
				5.collect{|l|
					data.data_microSequencerRanges[i][l].connectEach(\value,
						sequencerMicroGUI.ranges[i][l], _.valueSlot);
					sequencerMicroGUI.ranges[i][l].connectEach(\value,
						data.data_microSequencerRanges[i][l], _.valueSlot);
				};
				//graphEditor

				data.data_microSequencer[i].connectEach(\input, reshapeGraphMultisliders[i], _.valueSlot);
				data.data_microSequencer[i].connectEach(\value, reshapeGraphMultisliders[i], _.valueSlot);
				reshapeGraphMultisliders[i].connectEach(\value, data.data_microSequencer[i], _.valueSlot);

				5.collect{|l|
					data.data_microSequencerRanges[i][l].connectEach(\value,
						reshapeGraphRanges[i][l], _.valueSlot);
					reshapeGraphRanges[i][l].connectEach(\value,
						data.data_microSequencerRanges[i][l], _.valueSlot);
				};

				//microSequencer Speed
				data.data_microSpeed.connectEach(\input, sequencerMicroSpeedGUI.slider, _.valueSlot);
				data.data_microSpeed.connectEach(\value, sequencerMicroSpeedGUI.numberBox, _.valueSlot);

				sequencerMicroSpeedGUI.slider.connectEach(\value, data.data_microSpeed, _.inputSlot);
				sequencerMicroSpeedGUI.numberBox.connectEach(\value, data.data_microSpeed, _.valueSlot);



				//TABLES
				tablesGUI.multisliders[i].connectEach(\value, data.data_tables[i], _.valueSlot);
				data.data_tables[i].connectEach(\input, tablesGUI.multisliders[i], _.valueSlot);
				data.data_tables[i].connectEach(\value, tablesGUI.multisliders[i], _.valueSlot);
                 //SYNC TABLES DATA WITH BUFFERS
				data.data_tables[i].do({|val, l|
					//minimum value varies across 3 tables
					var minVal = [-1, 0, 0];
					val.signal(\value).connectTo({

						buffers[i][l].sendCollection([minVal[l],1].asSpec.map(val.value))
				})
				});
				//PULSARET
				pulsaretGUI.multisliders.connectEach(\value, data.data_pulsaret, _.valueSlot);

				data.data_pulsaret.connectEach(\input, pulsaretGUI.multisliders, _.valueSlot);
				data.data_pulsaret.connectEach(\value, pulsaretGUI.multisliders, _.valueSlot);
				//PRESET PULSARET
				data.data_preset_pulsaret.connectEach(\value, pulsaretGUI.presetNumberBox, _.valueSlot);
				pulsaretGUI.presetNumberBox.connectEach(\value, data.data_preset_pulsaret, _.valueSlot);

				data.data_preset_pulsaret_interpolation.connectEach(\input, pulsaretGUI.interpolationSlider, _.valueSlot);
				pulsaretGUI.interpolationSlider.connectEach(\value, data.data_preset_pulsaret_interpolation, _.inputSlot);

				//PULSARET SHAPER
				pulsaretShaperGUI.multislider.connectEach(\value, data.data_pulsaret_shaper, _.valueSlot);

				data.data_pulsaret_shaper.connectEach(\input, pulsaretShaperGUI.multislider, _.valueSlot);
				data.data_pulsaret_shaper.connectEach(\value, pulsaretShaperGUI.multislider, _.valueSlot);

				//ENVELOPE
				envelopeGUI.multisliders.connectEach(\value, data.data_envelope, _.valueSlot);

				data.data_envelope.connectEach(\input, envelopeGUI.multisliders, _.valueSlot);
				data.data_envelope.connectEach(\value, envelopeGUI.multisliders, _.valueSlot);
				//PRESET ENVELOPE
				data.data_preset_envelope.connectEach(\value, envelopeGUI.presetNumberBox, _.valueSlot);
				envelopeGUI.presetNumberBox.connectEach(\value, data.data_preset_envelope, _.valueSlot);

				data.data_preset_envelope_interpolation.connectEach(\input, envelopeGUI.interpolationSlider, _.valueSlot);
				envelopeGUI.interpolationSlider.connectEach(\value, data.data_preset_envelope_interpolation, _.inputSlot);

				//FREQUENCY
				frequencyGUI.multisliders.connectEach(\value, data.data_frequency, _.valueSlot);

				data.data_frequency.connectEach(\input, frequencyGUI.multisliders, _.valueSlot);
				data.data_frequency.connectEach(\value, frequencyGUI.multisliders, _.valueSlot);
				//PRESET FREQUENCY
				data.data_preset_frequency.connectEach(\value, frequencyGUI.presetNumberBox, _.valueSlot);
				frequencyGUI.presetNumberBox.connectEach(\value, data.data_preset_frequency, _.valueSlot);

				data.data_preset_frequency_interpolation.connectEach(\input, frequencyGUI.interpolationSlider, _.valueSlot);
				frequencyGUI.interpolationSlider.connectEach(\value, data.data_preset_frequency_interpolation, _.inputSlot);

				//PRESET CONTROL
				data.data_preset_trains.connectEach(\value, controlGUI.presetNumberBox, _.valueSlot);
				controlGUI.presetNumberBox.connectEach(\value, data.data_preset_trains, _.valueSlot);

				data.data_preset_train_interpolation.connectEach(\input, controlGUI.interpolationSlider, _.valueSlot);
				controlGUI.interpolationSlider.connectEach(\value, data.data_preset_train_interpolation, _.inputSlot);

				//SCRUBBER

				scrubberGUI.slider.do{ |slider, l|
					slider.signal(\value).connectTo({
						5.collect{|k| sequencerMicroGUI.scrubberSlider[l][k].value_(slider.value) }
				})
				};

				scrubberGUI.slider.do{ |slider, l|
					slider.signal(\value).connectTo({

						5.collect{|k| reshapeGraphScanningSliders[l][k].value_(slider.value) }
				})
				};

				data.data_parameterLink.connectEach(\value, parameterLinkGUI.offsetAmount, _.valueSlot);
				parameterLinkGUI.offsetAmount.connectEach(\value, data.data_parameterLink, _.valueSlot);

			};


		};

		3.collect{|i|
		connection.addAll(
				data.data_main[i].connectEach(\value, [
					synth[i].argSlot(\trigFreq),
					synth[i].argSlot(\grainFreq),
					synth[i].argSlot(\envRateMult),
					synth[i].argSlot(\pan),
					synth[i].argSlot(\ampMain)
				])
			);

			connection.addAll(
				data.data_ppModulation[i].connectEach(\value, [
					synth[i].argSlot(\fmAmt),
					synth[i].argSlot(\fmRatio),
					synth[i].argSlot(\allFluxAmt)
				])
			);

			connection.addAll(
				data.data_masking[i].connectEach(\value, [
					synth[i].argSlot(\probability),
					synth[i].argSlot(\burst),
					synth[i].argSlot(\rest),
					synth[i].argSlot(\chanMask),
					synth[i].argSlot(\centerMask)
				])
			);

			connection.addAll(
				data.data_sieveMasking[i].connectEach(\value, [
					synth[i].argSlot(\sieveMaskOn),
					synth[i].argSlot(\sieveMod),
					synth[i].argSlot(\sieveSequence)
				])
			);

			connection.addAll(
				4.collect{|l|

					data.data_modulators[i][l].connectEach(\value, [
						modulators[i][l].argSlot(\modFrequency),
						modulators[i][l].argSlot(\modIndex),
				])
				}
			);

			connection.addAll(
				4.collect{|l|
					data.data_modulation_wf_ranges[i][l].connectEach(\value, [
						modulators[i][l].argSlot(\wfLo),
						modulators[i][l].argSlot(\wfHi)
				])
				}
			);

			connection.addAll(
				4.collect{|l|
					data.data_modulatorType[i][l].connectEach(\value, [
						modulators[i][l].argSlot(\modSel),
				])
				}
			);

			connection.addAll(
				4.collect{|l|
					data.data_wfModulation_matrix_offset[i][l].connectEach(\value, [
						modMatrix[i][l].argSlot(\offset),
				])
				}
			);

			connection.addAll(
				4.collect{|l|
					data.data_wfModulation_matrix_polarity[i][l].connectEach(\value, [
						modMatrix[i][l].argSlot(\polarity),
				])
				}
			);

			connection.addAll(
				7.collect{|l|
					data.data_wfModulation_matrix[i][l].connectEach(\value, [
						modMatrix[i][l].argSlot(\mod1),
						modMatrix[i][l].argSlot(\mod2),
						modMatrix[i][l].argSlot(\mod3),
						modMatrix[i][l].argSlot(\mod4)
				])
				}
			);

		};

		^connection
	}
}