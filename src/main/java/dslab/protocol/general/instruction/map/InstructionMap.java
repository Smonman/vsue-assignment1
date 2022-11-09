package dslab.protocol.general.instruction.map;

import dslab.protocol.general.instruction.Instruction;

import java.util.Map;

/**
 * An extension of {@code Map<String, Instruction>}.
 *
 * <p>This provides a way to load and store instructions in memory.
 *
 * @see Map
 * @see Instruction
 */
public interface InstructionMap extends Map<String, Instruction> {

    /**
     * Loads the instructions into memory.
     */
    void loadInstructions();
}
