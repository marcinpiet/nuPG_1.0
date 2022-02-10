NuPG_Plotter {

	saveData {|data, width = 2000, height = 2000|
		var saveData;

		saveData = Dialog.savePanel({
			arg path;
			//var data;
			PsPlotter(
				data: data,
				path: path,
				min: 0,
				max: 1,
				samplePoints: 2048,
				duration: 2048 / Server.default.sampleRate,
				width: width,
				height: height,
				xEvery: 25,
				yEvery: 0.4,
				frame: 1,
				yGridOn: true,
				xGridOn: true,
				gridWidth: 0.5,
				frameWidth: 1,
		        curveWidth: 0.9,
				speckleWidth: 1,
				barWidth: 1,
		        gridCol: [0.5, 0.5, 0.5],
		        frameCol: [0.5,0.5,0.5],
				curveCol: [0,0,0],
		        speckleCol: [0.5,0.5,0.5],
		        barCol: [0,0,0],
		        continuousOn: false,
				discreteOn: true,
				barOn: false,
		        fontName: "Courier",
				fontSize: 8,
				fontCol: [0.5, 0.5, 0.5],
		        yLabelOn: true,
				xLabelOn: true,
		        xLabMultiplier: 10,
				yLabMultiplier: 10,
		        xLabRound: 0.1,
				yLabRound: 0.1,
		        xLabStart: 0,
				yLabStart: 0,
		        xLabEvery: 1,
				yLabEvery: 1,
		        zeroWidth: 0.75
		)},{"cancelled".postln});

		saveData;
	}
}