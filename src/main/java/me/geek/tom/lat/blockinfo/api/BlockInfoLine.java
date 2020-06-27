package me.geek.tom.lat.blockinfo.api;

public class BlockInfoLine {

    private final String line;
    private final int colour;

    public BlockInfoLine(String line, int colour) {
        this.line = line;
        this.colour = colour;
    }

    public String getLine() {
        return line;
    }

    public int getColour() {
        return colour;
    }
}
