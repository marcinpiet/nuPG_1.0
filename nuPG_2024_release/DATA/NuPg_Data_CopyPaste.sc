NuPG_Data_CopyPaste {

	classvar <>copy;

	copyData{|array|
		copy = array;

		^copy
	}

	pasteData{

		var paste = copy;

		^paste
	}
}