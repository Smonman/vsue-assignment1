package dslab.protocol.dmap.instruction.map;

import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.dmap.instruction.impl.DMAPDeleteInstruction;
import dslab.protocol.dmap.instruction.impl.DMAPListInstruction;
import dslab.protocol.dmap.instruction.impl.DMAPLoginInstruction;
import dslab.protocol.dmap.instruction.impl.DMAPLogoutInstruction;
import dslab.protocol.dmap.instruction.impl.DMAPQuitInstruction;
import dslab.protocol.dmap.instruction.impl.DMAPShowInstruction;
import dslab.protocol.general.instruction.map.impl.InstructionMapBase;
import dslab.protocol.general.instruction.validator.impl.ArgumentIntegerValidator;
import dslab.protocol.general.instruction.validator.impl.ArgumentPartMaxValidator;
import dslab.protocol.general.instruction.validator.impl.ArgumentPartMinValidator;
import dslab.protocol.general.instruction.validator.impl.ArgumentRequiredValidator;
import dslab.protocol.general.instruction.validator.impl.NoArgumentValidator;

import java.util.List;

/**
 * A concrete implementation of {@code InstructionMapBase}.
 *
 * @see InstructionMapBase
 */
public final class DMAPInstructionMap extends InstructionMapBase {

    private final Hook<Void, String> deleteHook;
    private final Hook<String, String> listHook;
    private final Hook<Boolean, String> loginHook;
    private final Hook<String, String> showHook;
    private final Hook<Boolean, String> existsHook;

    public DMAPInstructionMap(final Hook<Void, String> deleteHook,
                              final Hook<String, String> listHook,
                              final Hook<Boolean, String> loginHook,
                              final Hook<String, String> showHook,
                              final Hook<Boolean, String> existsHook) {
        super();
        this.deleteHook = deleteHook;
        this.listHook = listHook;
        this.loginHook = loginHook;
        this.showHook = showHook;
        this.existsHook = existsHook;
    }

    @Override
    public void loadInstructions() {
        DMAPLoginInstruction login = new DMAPLoginInstruction();
        DMAPListInstruction list = new DMAPListInstruction();
        DMAPShowInstruction show = new DMAPShowInstruction();
        DMAPDeleteInstruction delete = new DMAPDeleteInstruction();
        DMAPLogoutInstruction logout = new DMAPLogoutInstruction();
        DMAPQuitInstruction quit = new DMAPQuitInstruction();

        login.setDependentInstructions(
            List.of(
                list,
                show,
                delete
            ));
        list.setRequiredInstructions(
            List.of(
                login
            ));
        show.setRequiredInstructions(
            List.of(
                login
            ));
        delete.setRequiredInstructions(
            List.of(
                login
            ));
        logout.setRequiredInstructions(
            List.of(
                login
            ));
        logout.setDependentInstructions(
            List.of(
                login
            ));
        quit.setDependentInstructions(
            List.of(
                login,
                list,
                show,
                delete,
                logout
            ));

        login.setValidators(
            List.of(
                new ArgumentRequiredValidator("login"),
                new ArgumentPartMinValidator("login", " ", 2),
                new ArgumentPartMaxValidator("login", " ", 2)
            ));
        list.setValidators(
            List.of(
                new NoArgumentValidator("list")
            ));
        show.setValidators(
            List.of(
                new ArgumentRequiredValidator("show"),
                new ArgumentIntegerValidator("show")
            ));
        delete.setValidators(
            List.of(
                new ArgumentRequiredValidator("delete"),
                new ArgumentIntegerValidator("delete")
            ));
        logout.setValidators(
            List.of(
                new NoArgumentValidator("logout")
            ));
        quit.setValidators(
            List.of(
                new NoArgumentValidator("quit")
            ));

        login.setExternalHook(loginHook);
        list.setExternalHook(listHook);
        show.setExternalHook(showHook);
        show.setExistsHook(existsHook);
        delete.setExternalHook(deleteHook);
        delete.setExistsHook(existsHook);

        registerInstruction("login", login);
        registerInstruction("list", list);
        registerInstruction("show", show);
        registerInstruction("delete", delete);
        registerInstruction("logout", logout);
        registerInstruction("quit", quit);
    }
}
