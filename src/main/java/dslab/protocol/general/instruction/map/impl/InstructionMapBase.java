package dslab.protocol.general.instruction.map.impl;

import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.map.InstructionMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A base implementation of the instruction map.
 *
 * <p>This implementation provides a standard constructor which calls the {@code
 * loadInstructions} method.
 *
 * @see InstructionMap
 */
public abstract class InstructionMapBase implements InstructionMap {
    private final Map<String, Instruction> instructionMap;

    public InstructionMapBase() {
        this.instructionMap = new HashMap<>();
        loadInstructions();
    }

    /**
     * Registers a new instruction.
     *
     * <p>This method adds a new instruction to this instruction map.
     *
     * @param instructionName the name of the instruction. This is also the
     *                        command the instruction will be associated with.
     * @param instruction     the instruction
     */
    public final void registerInstruction(final String instructionName,
                                          final Instruction instruction) {
        put(instructionName, instruction);
    }

    @Override
    public int size() {
        return instructionMap.size();
    }
    
    @Override
    public boolean isEmpty() {
        return instructionMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return instructionMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return instructionMap.containsValue(value);
    }

    @Override
    public Instruction get(final Object key) {
        return instructionMap.get(key);
    }

    @Override
    public Instruction put(final String key, Instruction value) {
        return instructionMap.put(key, value);
    }

    @Override
    public Instruction remove(final Object key) {
        return instructionMap.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Instruction> m) {
        instructionMap.putAll(m);
    }

    @Override
    public void clear() {
        instructionMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return instructionMap.keySet();
    }

    @Override
    public Collection<Instruction> values() {
        return instructionMap.values();
    }

    @Override
    public Set<Entry<String, Instruction>> entrySet() {
        return instructionMap.entrySet();
    }
}
