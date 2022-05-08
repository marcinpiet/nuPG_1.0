NuPG_SliderPalyer {
	classvar <>array;
	classvar <>arrayMod;
	classvar <>arrayScrubb;
	classvar <>arrayPulsaretPreset;
	classvar <>arrayEnvelopePreset;
	classvar <>arrayFrequencyPreset;

	//2d array to store values from slider
	*sliderArray { ^array = Array.fill2D(3, 5, {Array.new(100000)})}
	*ppModArray { ^arrayMod = Array.fill2D(3, 3, {Array.new(100000)})}
	*scrubbArray { ^arrayScrubb = Array.fill(3, {Array.new(100000)})}
	*pulsaretArray { ^arrayPulsaretPreset = Array.fill(3, {Array.new(100000)})}
	*envelopeArray { ^arrayEnvelopePreset = Array.fill(3, {Array.new(100000)})}
	*frequencyArray { ^arrayFrequencyPreset = Array.fill(3, {Array.new(100000)})}

	*recordTask {|data, array|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|
			5.collect{|l|



				Tdef(("recordSlider_" ++ i ++ l).asSymbol, {

					array[i][l] = Array.new(100000);

					inf.do({
						array[i][l].add(data.data_main[i][l].value);
						//data.data_main[i][l].value.postln;
						0.05.wait;
					})
				})
				}
		};
		^tasks;
	}

	*playbackTask {|data, array|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|
			5.collect{|l|

				Tdef(("playbackSlider_" ++ i ++ l).asSymbol, {

					var sliderData = Pseq(array[i][l], inf).loop.asStream;

					inf.do({
						data.data_main[i][l].value = sliderData.next;
						0.05.wait;
					})
				})
				}
		};
		^tasks;
	}

	*ppModRecordTask {|data, array|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|
			3.collect{|l|



				Tdef(("ppModRecordSlider_" ++ i ++ l).asSymbol, {

					array[i][l] = Array.new(100000);

					inf.do({
						array[i][l].add(data.data_perGrainModulation[i][l].value);
						//data.data_main[i][l].value.postln;
						0.05.wait;
					})
				})
				}
		};
		^tasks;
	}

	*ppModPlaybackTask {|data, array|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|
			3.collect{|l|

				Tdef(("ppModPlaybackSlider_" ++ i ++ l).asSymbol, {

					var sliderData = Pseq(array[i][l], inf).loop.asStream;

					inf.do({
						data.data_perGrainModulation[i][l].value = sliderData.next;
						0.05.wait;
					})
				})
				}
		};
		^tasks;
	}

	*scrubbRecordTask {|data, array|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|

				Tdef(("scrubbRecordSlider_" ++ i).asSymbol, {

					array[i] = Array.new(100000);

					inf.do({
					array[i].add(data.data_scrubber[i][0].value);
						//data.data_main[i][l].value.postln;
						0.05.wait;
					})
				})
		};
		^tasks;
	}

	*scrubbPlaybackTask {|data, array|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|

				Tdef(("scrubbPlaybackSlider_" ++ i).asSymbol, {

					var sliderData = Pseq(array[i], inf).loop.asStream;

					inf.do({
					data.data_scrubber[i][0].value = sliderData.next;
						0.05.wait;
					})
				})
		};
		^tasks;
	}

	*pulsaretSliderRecordTask {|data, array|
		var tasks;
		var conductors = [\con_1, \con_2, \con_3];

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|

				Tdef(("pulsaretRecordSlider_" ++ i).asSymbol, {

					array[i] = Array.new(100000);

					inf.do({
					array[i].add(data.conductor[conductors[i].asSymbol][\con_pul].preset.interpCV.value);
						//data.data_main[i][l].value.postln;
						0.1.wait;
					})
				})
		};
		^tasks;
	}

	//added data to buffer updater
	*pulsaretSliderPlaybackTask {|data, array, buffers|
		var tasks;
		var conductors = [\con_1, \con_2, \con_3];

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|

				Tdef(("pulsaretPlaybackSlider_" ++ i).asSymbol, {

					var sliderData = Pseq(array[i], inf).loop.asStream;

					inf.do({
					data.conductor[conductors[i].asSymbol][\con_pul].preset.interpCV.value = sliderData.next;
					//data to buffer updater
					buffers[i][0].sendCollection(data.data_pulsaret[i].value);
						0.07.wait;
					})
				})
		};
		^tasks;
	}

	*envelopeSliderRecordTask {|data, array|
		var tasks;
		var conductors = [\con_1, \con_2, \con_3];

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|

				Tdef(("envelopeRecordSlider_" ++ i).asSymbol, {

					array[i] = Array.new(100000);

					inf.do({
					array[i].add(data.conductor[conductors[i].asSymbol][\con_env].preset.interpCV.value);
						//data.data_main[i][l].value.postln;
						0.05.wait;
					})
				})
		};
		^tasks;
	}

	*envelopeSliderPlaybackTask {|data, array, buffers|
		var tasks;
		var conductors = [\con_1, \con_2, \con_3];

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|

				Tdef(("envelopePlaybackSlider_" ++ i).asSymbol, {

					var sliderData = Pseq(array[i], inf).loop.asStream;

					inf.do({
					data.conductor[conductors[i].asSymbol][\con_env].preset.interpCV.value = sliderData.next;
					buffers[i][1].sendCollection(data.data_envelope[i].value);
					0.07.wait;
					})
				})
		};
		^tasks;
	}

	*frequencySliderRecordTask {|data, array|
		var tasks;
		var conductors = [\con_1, \con_2, \con_3];

		//3x5 tasks, one for each slider
		tasks = 3.collect{|i|

				Tdef(("frequencyRecordSlider_" ++ i).asSymbol, {

					array[i] = Array.new(100000);

					inf.do({
					array[i].add(data.conductor[conductors[i].asSymbol][\con_freq].preset.interpCV.value);
					//data.data_main[i][l].value.postln;
					0.05.wait;
					})
				})
		};
		^tasks;
	}

	*frequencySliderPlaybackTask {|data, array, buffers|
		var tasks;
		var conductors = [\con_1, \con_2, \con_3];

		//3x5 tasks, one for each sliders
		tasks = 3.collect{|i|

				Tdef(("frequencyPlaybackSlider_" ++ i).asSymbol, {

					var sliderData = Pseq(array[i], inf).loop.asStream;

					inf.do({
					data.conductor[conductors[i].asSymbol][\con_freq].preset.interpCV.value = sliderData.next;
					buffers[i][2].sendCollection(data.data_frequency[i].value);
						0.07.wait;
					})
				})
		};
		^tasks;
	}
}