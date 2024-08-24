NuPG_SliderRecordPlaybackTasks {

	var <>arrayScrubb;

	scrubbArray {|n| ^arrayScrubb = Array.fill(n, {Array.new(100000)})}

	scrubbRecordTask {|data, array, n|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = n.collect{|i|

				Tdef(("scrubbRecordSlider_" ++ i).asSymbol, {

					array[i] = Array.new(100000);

					inf.do({
					array[i].add(data.data_scrubber[i].value);
						//data.data_main[i][l].value.postln;
						0.01.wait;
					})
				})
		};
		^tasks;
	}

	scrubbPlaybackTask {|data, array, n|
		var tasks;

		//3x5 tasks, one for each slider
		tasks = n.collect{|i|

				Tdef(("scrubbPlaybackSlider_" ++ i).asSymbol, {

					var sliderData = Pseq(array[i], inf).loop.asStream;

					inf.do({
					data.data_scrubber[i].value = sliderData.next;
						0.01.wait;
					})
				})
		};
		^tasks;
	}
}