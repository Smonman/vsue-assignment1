package dslab.protocol.general.parser.impl;

import dslab.protocol.general.exception.InstructionNotFoundException;
import dslab.protocol.general.exception.InvalidInstructionException;
import dslab.protocol.general.instruction.map.InstructionMap;
import dslab.protocol.general.parser.Parser;

/**
 * A base implementation of Parser.
 *
 * <p>Provides standard methods for {@code parseInstruction} and {@code isSet}.
 *
 * @apiNote The given {@code InstructionMap} is loaded in this construction.
 */
public abstract class ParserBase implements Parser {

    private final InstructionMap instructionMap;
    private String response;

    public ParserBase(final InstructionMap instructionMap) {
        this.instructionMap = instructionMap;
        this.instructionMap.loadInstructions();
        this.response = null;
    }

    protected InstructionMap getInstructionMap() {
        return instructionMap;
    }

    /**
     * Gets the response.
     *
     * <p>Returns the response of the latest {@code parseInstruction} call, or
     * null if {@code parseInstruction} has not yet been called.
     *
     * @return the response of {@code parseInstruction} or {@code null}
     */
    public String getResponse() {
        return response;
    }

    @Override
    public boolean isSet(final String key) {
        return instructionMap.containsKey(key)
            && instructionMap.get(key).isSet();
    }

    /**
     * Parses an instruction.
     *
     * <p>This method parses an instruction of the DMTP or DMAP protocol. The
     * instruction must not be {@code null} and follow the protocol format.
     * The given instruction is split on the first space. The first part will
     * be regarded as the command, and the second part will be regarded as the
     * argument. How the argument is further interpreted is part of the
     * implementation of the instruction itself.
     *
     * @param instruction the whole instruction
     * @throws InvalidInstructionException  if the instruction is somehow
     *                                      faulty, and the process must be
     *                                      stopped.
     * @throws InstructionNotFoundException if the instruction is unknown.
     * @see dslab.protocol.general.instruction.Instruction
     */
    @Override
    public void parseInstruction(final String instruction)
        throws InvalidInstructionException, InstructionNotFoundException {
        String[] parts = instruction.trim().split("\\s", 2);
        if (parts.length == 0 || parts.length > 2) {
            throw new InvalidInstructionException(instruction);
        }
        String command = parts[0].trim();
        String argument = parts.length > 1 ? parts[1] : "";
        if (getInstructionMap().containsKey(command)) {
            response = getInstructionMap()
                .get(command)
                .handleInstruction(argument);
        } else {
            throw new InstructionNotFoundException();
        }
    }
}
