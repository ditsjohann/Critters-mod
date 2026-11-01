package com.critters.entity;

/** Implemented by every critter entity so models/renderers can read the current special action. */
public interface ActionHolder {
    ActionController critterActions();
}
