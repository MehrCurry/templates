package de.gzockoll.prototype.templates.entity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum TemplateState {
    EDITABLE{
        @Override
        public TemplateState requestApproval() {
            return transitionTo(READY_FOR_APPROVAL);
        }
    }, READY_FOR_APPROVAL{
        @Override
        public TemplateState approve() {
            return transitionTo(APPROVED);
        }
    },APPROVED{
        @Override
        public TemplateState edit() {
            return transitionTo(EDITABLE);
        }
    };

    public TemplateState requestApproval() {
        throw new IllegalStateException(getErrorMessage());
    }

    public TemplateState approve() {
        throw new IllegalStateException(getErrorMessage());
    }

    public TemplateState edit() {
        throw new IllegalStateException(getErrorMessage());
    }

    private String getErrorMessage() {
        return "Illegal transistion while in state " + toString();
    }

    public void onExit() {
        log.debug(toString() + ": onExit()");
    }

    public void onEnter() {
        log.debug(toString() + ": onEnter()");
    }

    protected TemplateState transitionTo(TemplateState newState) {
        if (this!=newState) {
            this.onExit();
            newState.onEnter();
        }
        return newState;
    }
}
