package dslab.protocol.dmap.parser;

import dslab.protocol.general.instruction.map.InstructionMap;
import dslab.protocol.general.parser.impl.ParserBase;

/**
 * An extension of {@code ParserBase}.
 *
 * <p>This is a parser for the DMAP protocol.
 *
 * @see ParserBase
 */
public final class DMAPParser extends ParserBase {

    public DMAPParser(final InstructionMap instructionMap) {
        super(instructionMap);
    }
}
