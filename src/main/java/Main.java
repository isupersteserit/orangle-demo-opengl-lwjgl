// Main.java
//
// Entry point for the Cube Demo application.
// This class controls program flow: running, looping, and cleanup.
// According to UML: Main depends on Init and Loop.

public class Main {



    public static void main(String[] args) {

        // Create Init object to start program
        Init init = new Init();

        // Run initial setup
        init.progStart();



        // Create Loop object to manage rendering loop
        Loop loop = new Loop(init);

        // Start main render loop
        loop.startLoop();



        // Cleanup after loop finishes
        cleanup(init);

    }



    public static void cleanup(Init init) {

        // Call cleanup routine from Init
        init.cleanup();

    }

}
