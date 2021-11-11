package agents.pogAgent;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Agent implements MarioAgent {

    private enum JumpType {
        ENEMY, GAP, WALL, NONE
    }

    private boolean[] action;
    private JumpType jumpType = JumpType.NONE;
    private int jumpCount = 0, jumpSize = -1;

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        // action[MarioActions.RIGHT.getValue()] = true;
        if ((model.isMarioOnGround() || model.mayMarioJump()) && !jumpType.equals(JumpType.NONE)) {
            setJump(JumpType.NONE, -1);
        }
        action[MarioActions.JUMP.getValue()] = !jumpType.equals(JumpType.NONE) && jumpCount < jumpSize;
        return action;
    }

    @Override
    public String getAgentName() {
        return "pogAgent";
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        action = new boolean[MarioActions.numberOfActions()];
        // action[MarioActions.RIGHT.getValue()] = true;
        // action[MarioActions.SPEED.getValue()] = true;
    }

    private final void setJump(final JumpType type, final int size) {
        jumpType = type;
        jumpSize = size;
        jumpCount = 0;
    }

}
