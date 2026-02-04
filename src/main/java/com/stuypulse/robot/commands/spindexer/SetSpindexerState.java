import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetSpindexerState extends InstantCommand {
    private final Spindexer spindexer;
    private final Spindexer.SpindexerState state;

    public SetSpindexerState(Spindexer spindexer, Spindexer.SpindexerState state) {
        this.spindexer = spindexer;
        this.state = state;
        addRequirements(spindexer);
    }

    @Override
    public void initialize() {
        spindexer.setSpindexerState(state);
    }
}