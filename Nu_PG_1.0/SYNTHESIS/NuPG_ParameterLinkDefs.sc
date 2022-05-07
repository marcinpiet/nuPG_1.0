NuPG_ParameterLinkDefs {

	classvar <> linkMap;
	classvar <> linkTask;

	*new {|linkMap, linkTask|
		^super.newCopyArgs(linkMap, linkTask)
	}

	//mapping function
	linkMap {
		^{|input = 0, mapping = 0|
			//switch between mapping modes
			//0 = linear follow, 1 = linear inverse, 2 = custom from a file

			switch(mapping,
				0, {input},
				1, {input.linlin(0.0,1.0,1.0,0.0)},
				2, {"opening a document with mapping definitions".posln}
		)}
	}

	//x6 tasks - one for each link
	//define local environment - necessary to set arguments
	//for each task and arguments
	//for source and listener - param setting, link mapping function
}
