package com.stuypulse.robot.commands.spindexer;

import com.stuypulse.robot.subsystems.spindexer.Spindexer.SpindexerState;

public class SetSpindexerForward extends SetSpindexerState{
    public SetSpindexerForward(){
        super(SpindexerState.FORWARD);
    }
    
}
