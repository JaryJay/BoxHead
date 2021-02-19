package main;

import processing.core.PApplet;

public class BoxHeadApp {

	public static void main(String[] args) {
		String[] processingArgs = { "BoxHead" };
		BoxHeadSketch mySketch = new BoxHeadSketch();
		PApplet.runSketch(processingArgs, mySketch);
	}

}
