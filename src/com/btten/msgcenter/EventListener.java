package com.btten.msgcenter;

public interface EventListener {
	void doEvent(int eventId,Object sender, EventArg e);
}