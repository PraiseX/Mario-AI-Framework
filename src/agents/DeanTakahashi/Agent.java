package agents.DeanTakahashi;
import java.lang.System;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Agent implements MarioAgent {

    private enum JumpType {
        ENEMY, GAP, WALL, NONE
    }

    private class Rectangle {
        private float x, y, width, height;

        public Rectangle(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(float x, float y) {
            return x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.height;
        }
    }

    // private float prevY = 0;

    private boolean[] action;
    private JumpType jumpType = JumpType.NONE;
    private int jumpCount = 0, jumpSize = -1;

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        // action[MarioActions.RIGHT.getValue()] = true;
        // if ((model.isMarioOnGround() || model.mayMarioJump()) && !jumpType.equals(JumpType.NONE)) {
        //     setJump(JumpType.NONE, -1);
        // }
        // action[MarioActions.JUMP.getValue()] = !jumpType.equals(JumpType.NONE) && jumpCount < jumpSize;
        final float marioSpeed = model.getMarioFloatVelocity()[0];
        final boolean dangerOfEnemy = enemyInRange(model, new Rectangle(-13, -57, 50, 87));
        final int wallHeight = getWallHeight(model.getMarioScreenTilePos()[0], model.getMarioScreenTilePos()[1], model.getScreenSceneObservation());
        // if (model.mayMarioJump()) {
        //     final int wallHeight = getWallHeight(model.getMarioScreenTilePos()[0], model.getMarioScreenTilePos()[1], model.getScreenSceneObservation());
        //     if (marioSpeed <= 1 && wallHeight > 0) {
        //         setJump(JumpType.WALL, wallHeight >= 4 ? wallHeight + 3 : wallHeight);
        //     }
        // }

        if (dangerOfEnemy) {
            setJump(JumpType.ENEMY, 7);
        }
        else if (marioSpeed <= 1) {
            setJump(JumpType.WALL, wallHeight >= 4 ? wallHeight + 3 : wallHeight);
            // System.out.println("This is the wall height: " + wallHeight);
        }

        // final boolean isFalling = prevY < model.getMarioFloatPos()[1] && jumpType.equals(JumpType.NONE);
        action[MarioActions.JUMP.getValue()] = !jumpType.equals(JumpType.NONE) && jumpCount < jumpSize;
        // prevY = model.getMarioFloatPos()[1];
        return action;
    }

    @Override
    public String getAgentName() {
        return "DeanTakahashi";
    }

    private boolean enemyInRange(MarioForwardModel e, Rectangle r) {
        for (int i = 0; i < e.getEnemiesFloatPos().length; i += 3) {
            if (r.contains(e.getEnemiesFloatPos()[i + 1] - e.getMarioFloatPos()[0],
                    e.getMarioFloatPos()[1] - e.getEnemiesFloatPos()[i + 2])) {
                return true;
            }
        }
        return false;
    }

    private int getWallHeight(int tileX, int tileY, int[][] levelScene) {
        int y = tileY + 1, wallHeight = 0;
        while (y-- > 0 && levelScene[tileX + 1][y] != 0) {
            wallHeight++;
        }
        return wallHeight;
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        action = new boolean[MarioActions.numberOfActions()];
        action[MarioActions.RIGHT.getValue()] = true;
        // action[MarioActions.SPEED.getValue()] = true;
    }

    private final void setJump(final JumpType type, final int size) {
        jumpType = type;
        jumpSize = size;
        jumpCount = 0;
    }

}
