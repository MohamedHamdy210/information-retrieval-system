package CodeLogic;

import GUI.First_Frame;

public class MainClass {
    public static void main(String[] args) {
        // Start the program, by get instance from PositionalIndex class, to start build it
        PositionalIndex PI = PositionalIndex.getInstance();
        /*-------------- GUI-------------------*/
        First_Frame first_frame = new First_Frame();
    }
}