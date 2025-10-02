package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.guxuede.gm.net.client.registry.pack.PlayerLandingPack;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Paths;

public class CommandSystem extends EntitySystem {

    private static Engine engine;
    private LineReader reader;

    public CommandSystem(Engine engine) {
        this.engine = engine;

        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Create line reader
        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .variable(LineReader.HISTORY_FILE, Paths.get("history.txt"))
                .variable(LineReader.HISTORY_SIZE, 1000) // Maximum entries in memory
                .variable(LineReader.HISTORY_FILE_SIZE, 2000) // Maximum entries in file
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String line = reader.readLine("JLine > ");
                    if(StringUtils.isNoneBlank(line)){
                        try {
                            CommandLine commandLine = new CommandLine(new GameCommand());
                            commandLine.execute(line.split(" "));
                        }catch (RuntimeException e){
                            e.printStackTrace();
                        }
                    }
                    // Print the result
                    System.out.println("You entered: " + line);
                }
            }
        }).start();
    }


    @CommandLine.Command(
            subcommands = {
                    LandingCommand.class,
                    HiCommand.class,
            }
    )
    public class GameCommand implements Runnable{

        @Override
        public void run() {
            System.out.printf("123");
        }
    }


    @CommandLine.Command(
            name = "hi"
    )
    public static class HiCommand implements Runnable{

        public HiCommand() {
        }

        @Override
        public void run() {
            System.out.println("hi");
        }
    }


    @CommandLine.Command(
            name = "landing" // landing character=RPGMarkGreg id=123 x=100 y=200
    )
    public static class LandingCommand implements Runnable{

        @CommandLine.Option(names = {"id"})
        private int id;

        @CommandLine.Option(names = {"x"})
        private int x;

        @CommandLine.Option(names = {"y"})
        private int y;

        @CommandLine.Option(names = {"character"})
        private String character;

        public LandingCommand() {
        }

        @Override
        public void run() {
            PlayerLandingPack playerLandingPack = new PlayerLandingPack(id, x, y, character);
            engine.getSystem(MessageOutboundSystem.class).broadCaseMessage(playerLandingPack);
        }
    }
}
