package dslab.protocol.general.parser;

import dslab.protocol.general.exception.InstructionNotFoundException;
import dslab.protocol.general.exception.InvalidInstructionException;

/**
 * This interface provides required methods for a parser for the DMTP or DMAP
 * protocol.
 */
public interface Parser {

    /**
     * This method returns whether a specific instruction is currently set.
     *
     * @param key the name of the instruction
     * @return true if the instruction is set, false otherwise
     */
    boolean isSet(String key);

    /**
     * This method parses a single instruction.
     *
     * <p>This method parses a single instruction of the DMTP or DMAP protocol.
     *
     * @param instruction the whole instruction
     * @throws InvalidInstructionException  if the instruction is somehow
     *                                      faulty, and the process must be
     *                                      stopped.
     * @throws InstructionNotFoundException if the instruction is unknown.
     */
    void parseInstruction(String instruction)
        throws InvalidInstructionException, InstructionNotFoundException;
}
