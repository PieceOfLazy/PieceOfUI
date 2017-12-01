package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

/**
 * Created by zpdl
 */

class StripOfNotifyParam {
    enum State {
        CHANGED,
        INSERTED,
        REMOVED
    }

    @NonNull
    private final State mState;
    private int mViewPosition;
    private final int mViewCount;

    StripOfNotifyParam(@NonNull State state, int vPos, int vCnt) {
        mState = state;
        mViewPosition = vPos;
        mViewCount = vCnt;
    }

    public State getState() {
        return mState;
    }

    public void setViewPosition(int vPos) {
        mViewPosition = vPos;
    }

    public int getViewPosition() {
        return mViewPosition;
    }

    public int getViewCount() {
        return mViewCount;
    }

    public StripOfNotifyParam composeParam(@NonNull StripOfNotifyParam param) {
        if(mState != param.getState()) {
            return null;
        }

        switch (mState) {
            case CHANGED: {
                return composeChangedParam(param);
            }
            case INSERTED: {
                return composeInsertedParam(param);
            }
            case REMOVED: {
                return composeRemovedParam(param);
            }

        }
        return null;
    }

    private StripOfNotifyParam composeChangedParam(StripOfNotifyParam param) {
        if(mViewPosition <= param.getViewPosition() && mViewPosition + mViewCount >= param.getViewPosition()) {
            int oldMaxPosition = mViewPosition + mViewCount;
            int newMaxPosition = param.getViewPosition() + param.getViewCount();

            if(oldMaxPosition < newMaxPosition) {
                return new StripOfNotifyParam(State.CHANGED, mViewPosition, newMaxPosition - mViewPosition);
            } else {
                return this;
            }
        } else if(param.getViewPosition() <= mViewPosition && param.getViewPosition() + param.getViewCount() >= mViewPosition) {
            int oldMaxPosition = mViewPosition + mViewCount;
            int newMaxPosition = param.getViewPosition() + param.getViewCount();

            if(oldMaxPosition > newMaxPosition) {
                return new StripOfNotifyParam(State.CHANGED, param.getViewPosition(), oldMaxPosition - param.getViewPosition());
            } else {
                return param;
            }
        } else {
            return null;
        }
    }

    private StripOfNotifyParam composeInsertedParam(StripOfNotifyParam param) {
        if(mViewPosition <= param.getViewPosition() && mViewPosition + mViewCount >= param.getViewPosition()) {
            return new StripOfNotifyParam(State.INSERTED, mViewPosition, mViewCount + param.getViewCount());
        } else if(param.getViewPosition() <= mViewPosition && param.getViewPosition() + param.getViewCount() >= mViewPosition) {
            return new StripOfNotifyParam(State.INSERTED, param.getViewPosition(), mViewCount + param.getViewCount());
        } else {
            return null;
        }
    }

    private StripOfNotifyParam composeRemovedParam(StripOfNotifyParam param) {
//        Trace.i("KKH", "composeRemovedParam mViewPosition : "+mViewPosition+" mViewCount : "+mViewCount);
//        Trace.i("KKH", "composeRemovedParam param mViewPosition : "+param.getViewPosition()+" mViewCount : "+param.getViewCount());
        if(mViewPosition + mViewCount == param.getViewPosition()) {
//            Trace.i("KKH", "mViewPosition == param.getViewPosition()");
            return new StripOfNotifyParam(State.REMOVED, mViewPosition, mViewCount + param.getViewCount());
        } else if(param.getViewPosition() + param.getViewCount() == mViewPosition) {
//            Trace.i("KKH", "param.getViewPosition() + param.getViewCount() == mViewPosition");
            return new StripOfNotifyParam(State.REMOVED, param.getViewPosition(), mViewCount + param.getViewCount());
        } else {
            return null;
        }
    }
}